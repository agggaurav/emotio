package com.example.gaurav.card;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gaurav on 04-10-2016.
 */

public class OneFragment extends Fragment {

    private static int SPLASH_TIME_OUT = 15000;
    public String actionbar_title;
    private CircleRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private String userChoosenTask;
    private PostAdapter adapter;
    private ImageView ivImage;
    private Uri filePath;
    private List<Post> postList;
    private List<String> likedpost;
    SessionManager session;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    String url = Constants.ip+"/post/?format=json";
    String like_url=Constants.ip;
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;
    private NetworkImageView imageView;
    private ImageLoader imageLoader;
    private String session_email;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    Toolbar imagetitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        imageView=(NetworkImageView) view.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout);
        imagetitle=(Toolbar) view.findViewById(R.id.toolbar2);
        //imagetitle.setTitle("image name");
        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        session_email = user.get(SessionManager.KEY_EMAIL);
        like_url=like_url+"/like/"+session_email+"/?format=json";
       // Toast.makeText(getApplicationContext(),session_email,Toast.LENGTH_SHORT).show();
        likedpost=new ArrayList<>();
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        //Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int ypos = recyclerView.computeVerticalScrollOffset();
                if (ypos == 50) {
                    //          Toast.makeText(getContext(), "tttt", Toast.LENGTH_SHORT).show();
                }
                int visibleItemCount = recyclerView.getChildCount();
                if (dy > 0) {
                    int firstVisibleItem = mRecyclerViewHelper.findFirstCompletelyVisibleItemPosition();

                    Log.e("up", String.valueOf(firstVisibleItem));
                    if (firstVisibleItem != -1) {
                        actionbar_title = adapter.gettitle(firstVisibleItem);
                        imagetitle.setTitle(actionbar_title);
                        //updatetitle();
                    }
                } else if (dy < 0) {
                    int lastVisibleItem = mRecyclerViewHelper.findLastCompletelyVisibleItemPosition();
                    Log.e("down", String.valueOf(lastVisibleItem));
                    if (lastVisibleItem != -1) {
                        actionbar_title = adapter.gettitle(lastVisibleItem);
                        imagetitle.setTitle(actionbar_title);

                    }
                }

            }
        });


        try {
            //     Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getlikedpost();
        //Toast.makeText(getContext(), "liked post id"+likedpost.get(0),Toast.LENGTH_SHORT).show();
        prepareAlbums();
        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        // do something when refresh starts

                        adapter.notifyDataSetChanged();

                        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                mRefreshLayout.finishRefreshing();
                                // close this activity

                            }
                        }, SPLASH_TIME_OUT);
                        prepareAlbums();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void completeRefresh() {
                        // do something when refresh complete
                        Toast.makeText(getContext(),"complete refresh",Toast.LENGTH_SHORT).show();
                    }
                });
return view;
    }

    private void prepareAlbums() {

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue=null;
        //Toast.makeText(getContext(),"requestQueue",Toast.LENGTH_SHORT).show();
       JsonArrayRequest arrayreq = new JsonArrayRequest(url,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {

          //                      Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();
                                JSONObject postObj = response.getJSONObject(i);
                                String id=postObj.getString("id");
                                String email_id=postObj.getString("email_id");
                               String name=postObj.getString("username");
                               String imageurl=postObj.getString("image_posted");
            //                    Toast.makeText(getContext(),"4",Toast.LENGTH_SHORT).show();
                               BitmapDrawable img= loadImage(imageurl);
              //                  Toast.makeText(getContext(),"5",Toast.LENGTH_SHORT).show();
                               //BitmapDrawable drawable=new BitmapDrawable(img);
                               Post a = new Post(name,img,imageurl,id,email_id);
                               if(likedpost.contains(id)) {
                                   a.setlike(true);
                               }
                                else
                               {
                                   a.setlike(false);
                               }
                                //Toast.makeText(getContext(),"aaaa"+a.getName(),Toast.LENGTH_SHORT).show();
                                postList.add(a);
                //                Toast.makeText(getContext(),"6",Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();

                            }

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(arrayreq);
        adapter.notifyDataSetChanged();
       // Toast.makeText(getContext(),"7",Toast.LENGTH_SHORT).show();

    }



    private void getlikedpost() {

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue=null;
        //Toast.makeText(getContext(),"requestQueue",Toast.LENGTH_SHORT).show();
        JsonArrayRequest arrayreq = new JsonArrayRequest(like_url,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                //                      Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();
                                JSONObject postObj = response.getJSONObject(i);
                                String id=postObj.getString("post_id");
                               // Toast.makeText(getContext(),id, Toast.LENGTH_LONG).show();
                               likedpost.add(id);
                            }

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(arrayreq);
        adapter.notifyDataSetChanged();
        // Toast.makeText(getContext(),"7",Toast.LENGTH_SHORT).show();

    }



    private BitmapDrawable loadImage(String media){

        url=Constants.ip;
        url=url+media;
        if(url.equals("")){
            Toast.makeText(getContext(),"Please enter a URL", Toast.LENGTH_LONG).show();
            return null;
        }

        imageLoader = CustomVolleyRequest.getInstance(this.getContext()).getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView, R.drawable.image, android.R.drawable.ic_dialog_alert));

      //  Drawable bp = null;//=imageLoader.get(url, ImageLoader.getImageListener(imageView, R.drawable.image, android.R.drawable.ic_dialog_alert)).getBitmap();

        imageView.setImageUrl(url, imageLoader);
        BitmapDrawable bp=null;
        return bp;
    }




    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}