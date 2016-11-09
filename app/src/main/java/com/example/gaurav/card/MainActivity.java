package com.example.gaurav.card;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public String actionbar_title;
    private RecyclerView recyclerView;
    public String value;
    private  RecyclerViewPositionHelper mRecyclerViewHelper;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManager session;
    private Uri filePath;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private Context context;
    ImageButton logout;
    private String session_password,session_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        //finish();
        HashMap<String, String> user = session.getUserDetails();

        // name
        session_email = user.get(SessionManager.KEY_EMAIL);
        Toast.makeText(getApplicationContext(),session_email,Toast.LENGTH_SHORT).show();
        // email
        session_password = user.get(SessionManager.KEY_PASSWORD);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        initCollapsingToolbar();

//Check Login Status


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        BottomBar bottomBar;//=new BottomBar(this);
        bottomBar=(BottomBar) findViewById(R.id.bottomBar);
        //BottomBarTab b=(BottomBarTab) findViewById(R.id.tab_search);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_profile) {
                    Toast.makeText(getApplication(), "profile", Toast.LENGTH_SHORT).show();
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_camera) {
                    selectImage();
                    //  MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(context)
                    //        .setTitle("Awesome!")
                    //      .setDescription("What can we improve? Your feedback is always welcome.")
                    //    .build();
                    //dialog.show();
                } else if (tabId == R.id.tab_search) {
                        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Logout");
        menu.getItem(0).setIcon(R.mipmap.ic_launcher);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
        LoginManager.getInstance().logOut();
        session.logoutUser();
        finish();
                return super.onOptionsItemSelected(item);
        }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Insta");
        adapter.addFragment(new SecondFragment(), "Music");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getActionBar();
            //ab.setTitle(actionbar_title);
          //  ab.setSubtitle("sub-title");
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getApplicationContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        filePath = getImageUri(this, thumbnail);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialogcustom, null);
        ivImage=(ImageView) customView.findViewById(R.id.dialogimage);
        ivImage.setImageBitmap(thumbnail);
        EditText sub=(EditText) customView.findViewById(R.id.dialogtext);
        sub.setText("texting");
        Button dialogsubmit=(Button) customView.findViewById(R.id.dialogButtonOK);
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(context)
                .setTitle("Awesome!")
             // .setDescription("What can we improve? Your feedback is always welcome.")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setCustomView(customView)
                .setCancelable(true)
                .withDialogAnimation(true)
            .build();

        dialog.show();
        dialogsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //registerUser();
                uploadMultipart(value);
            }
        });
        //
    }
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            filePath = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialogcustom, null);
        ivImage=(ImageView) customView.findViewById(R.id.dialogimage);
        ivImage.setImageBitmap(bm);
        EditText sub=(EditText) customView.findViewById(R.id.dialogtext);
        sub.setText("texting");
        Button dialogsubmit=(Button) customView.findViewById(R.id.dialogButtonOK);
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(context)
                .setTitle("Awesome!")
                        // .setDescription("What can we improve? Your feedback is always welcome.")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setCustomView(customView)
                .setCancelable(true)
                .withDialogAnimation(true)
                .build();

        dialog.show();
        dialogsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerUser();
                uploadMultipart(value);
            }
        });


        //ivImage.setImageBitmap(bm);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void uploadMultipart(String value) {
        String url=Constants.ip;
        url=url+"/postuserdetail/";
        String path=null;

        //getting the actual path of the image
        try {
            path = getPath(filePath);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(getApplicationContext(), uploadId,url)
                    .addFileToUpload(path, "image_posted") //Adding file
                    .addParameter("email_id",session_email) //Adding text parameter to the request
                    .addParameter("username","gaurav")          //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public String getPath(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = this.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    private void registerUser() {
        final String email = session_email;
        //final String password2 = password.getText().toString().trim();
        //final String cnfpass = cnpassword.getText().toString().trim();
        //final String email_id = email.getText().toString().trim();



            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ip+"/postuserdetail/",
                    new Response.Listener<String>() {
                        //sdd
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email_id", email);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */



    public void updatetitle()
    {
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle(actionbar_title);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//collapsingToolbar.setTitleColor(Color.WHITE);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    collapsingToolbar.setCollapsedTitleTextColor(1);
                    collapsingToolbar.setTitle("Emotio");
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("Emotio");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */

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