package com.example.gaurav.card;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Gaurav on 18-10-2016.
 */
public class Post {
    public String name;
    public BitmapDrawable thumbnail;
    public String imageurl;
    public String Id,Email;
    public boolean liked;



    public Post(String name, BitmapDrawable thumbnail,String imageurl,String id,String email) {
        this.name = name;
        this.imageurl=imageurl;
        this.thumbnail = thumbnail;
        this.Email=email;
        this.Id=id;
        if(thumbnail==null)
        {
            Log.e("empty","emptyyyyyyyyyyyyy");
        }
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageurl;
    }
    public String getId(){
        return Id;
    }
    public String getEmail()
    {
        return Email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setImageUrl(String imageurl) {
        this.imageurl = imageurl;
    }
    public void setlike(boolean a)
    {
        liked=a;
    }
    public boolean getlike()
    {
        return liked;

    }


    public BitmapDrawable getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(BitmapDrawable thumbnail) {
        this.thumbnail = thumbnail;
    }
}
