package com.example.gaurav.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gaurav on 18-10-2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    public OneFragment mContext;
    public List<Post> postList;
    public ImageLoader imageLoader;
    public String URL=Constants.ip+"/like/";
    public SessionManager session;
    private String LoggedUserEmail;
    private CallbackManager callbackManager;
    private LoginManager manager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView  overflow,like,share;

        public NetworkImageView thumbnail;
        public String imgurl;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //count = (TextView) view.findViewById(R.id.count);
            thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            like=(ImageView) view.findViewById(R.id.like);
            share=(ImageView) view.findViewById(R.id.share);
        }
    }

    public String gettitle(int a)
    {
        String rname;
        if(a>0) {
            rname = postList.get(a - 1).getName();
        }
        else
            rname=postList.get(a).getName();
        return rname;
    }

    public PostAdapter(OneFragment mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        FacebookSdk.sdkInitialize(mContext.getContext());
        session = new SessionManager(mContext.getContext());
        HashMap<String, String> user = session.getUserDetails();
        LoggedUserEmail = user.get(SessionManager.KEY_EMAIL);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Post post = postList.get(position);
        holder.title.setText(post.getName());

        Log.e("image loading", "qwwwwwwwwwwwwwwwwwwww");
     //   Glide.with(mContext).load(post.getThumbnail()).placeholder(post.getThumbnail()).into(holder.thumbnail);
        (holder.thumbnail).setImageDrawable(post.getThumbnail());
        final ImageView thumbnails=holder.thumbnail;
        thumbnails.setImageDrawable(post.getThumbnail());
        loadImage(post.getImageUrl(), holder.thumbnail);
        if(post.getlike()==true)
        {
            holder.like.setImageDrawable(ContextCompat.getDrawable(mContext.getContext(), R.drawable.like));
        }
        else
        {
            holder.like.setImageDrawable(ContextCompat.getDrawable(mContext.getContext(),R.drawable.heart));
        }
        callbackManager = CallbackManager.Factory.create();
        thumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Glide.with(mContext).load(R.drawable.album11).placeholder(R.mipmap.ic_launcher).crossFade(1500).into(holder.thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(holder.overflow);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageDrawable(ContextCompat.getDrawable(mContext.getContext(), R.drawable.like));
                likeimage(post);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    shareimage(post);

            }
        });
    }

public void shareimage(final Post post)
{
    List<String> permissionNeeds = Arrays.asList("publish_actions");

    //this loginManager helps you eliminate adding a LoginButton to your UI
    manager = LoginManager.getInstance();

    manager.logInWithPublishPermissions(mContext.getActivity(), permissionNeeds);

    manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    sharePhotoToFacebook(post);
                }

                @Override
                public void onCancel() {
                    System.out.println("onCancel");
                }

                @Override
                public void onError(FacebookException e) {

                }
            }
    );
    }
    private void sharePhotoToFacebook(Post post){
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("burppp!!!")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    public void likeimage(final Post p)
    {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        //sdd
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(mContext.getContext(), response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("post_id", p.getId());
                    params.put("liked_by", LoggedUserEmail);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(mContext.getContext());
            requestQueue.add(stringRequest);
        }


    /**
     * Showing popup menu when tapping on 3 dots
     */

    private void loadImage(String media, NetworkImageView imageView){

        String url=Constants.ip;
        url=url+media;
        if(url.equals("")){
            Toast.makeText(mContext.getContext(),"Please enter a URL", Toast.LENGTH_LONG).show();

        }

        imageLoader = CustomVolleyRequest.getInstance(mContext.getContext()).getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView,R.drawable.image, android.R.drawable.ic_dialog_alert));
        imageView.setImageUrl(url, imageLoader);
    }



    private void showPopupMenu(View view) {
        Toast.makeText(view.getContext(), "overflow", Toast.LENGTH_SHORT).show();
        // inflate menu
      /*  PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();*/
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    // Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    // Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}