package com.dagf.presentlogolib.nextview;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

public class NextViewItem implements Parcelable {
    protected NextViewItem(Parcel in) {
        name = in.readString();
        urlthumb = in.readParcelable(Bitmap.class.getClassLoader());
        urlmedia = in.readString();
    }

    public NextViewItem(){

    }

    public static final Creator<NextViewItem> CREATOR = new Creator<NextViewItem>() {
        @Override
        public NextViewItem createFromParcel(Parcel in) {
            return new NextViewItem(in);
        }

        @Override
        public NextViewItem[] newArray(int size) {
            return new NextViewItem[size];
        }
    };

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

        this.urlmedia = urlmedia;
    }


    public void loadFrame(){
        try {
            Random rand = new Random();

            long d = (long)rand.nextInt((180 - 30) + 1) + 30;
            // Log.e("MAIN", "setUrlmedia: "+d*10000);
            urlthumb = retriveVideoFrameFromVideo(urlmedia, d);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Log.e("MAIN", "setUrlmedia: "+throwable.getMessage() );
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeValue(urlthumb);
        dest.writeString(urlmedia);


    }
}
