package com.dagf.presentlogolib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.dagf.presentlogolib.present.PresentActivity;
import com.squareup.picasso.Picasso;

public class WineHelper {


    /**
     typeVideo:
     0 => Default Neon
     1 => luminoso
     **/
    public static void openPresentation(Context cc, int typeVideo){
        if(typeVideo >= 0 && typeVideo <= 3)
        PresentActivity.what_video = typeVideo;
        cc.startActivity(new Intent(cc, PresentActivity.class));
    }


    public static boolean shouldPresent(Activity ac){
        return !PresentActivity.isAlreadyViewed(ac);
    }

    public static void openPresentation(Context cc){
        cc.startActivity(new Intent(cc, PresentActivity.class));
    }


    public static void loadImageLogo(ImageView img){
        Picasso.get().load(Uri.parse("http://wineberryhalley.com/about_us/id.png")).into(img);

    }

    public static void loadCompanyCompleteLogo(ImageView img){
        Picasso.get().load(Uri.parse("http://wineberryhalley.com/about_us/wbh_ic.png")).into(img);
    }


}
