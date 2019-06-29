package com.dagf.presentlogolib.nextview;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

public class NextViewItem {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getUrlthumb() {
        return urlthumb;
    }


    public String getUrlmedia() {
        return urlmedia;
    }

    public void setUrlmedia(String urlmedia) {
        try {
            Random rand = new Random();

            long d = (long)rand.nextInt((180 - 30) + 1) + 30;
           // Log.e("MAIN", "setUrlmedia: "+d*10000);
            urlthumb = retriveVideoFrameFromVideo(urlmedia, d);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Log.e("MAIN", "setUrlmedia: "+throwable.getMessage() );
        }
        this.urlmedia = urlmedia;
    }

    private String name;
private Bitmap urlthumb;
private String urlmedia;

    public static Bitmap retriveVideoFrameFromVideo(String videoPath, long tim)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(tim * 10000000, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
