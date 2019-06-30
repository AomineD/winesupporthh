package com.dagf.presentlogolib.nextview;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class NextViewItem implements Parcelable {
    protected NextViewItem(Parcel in) {
        name = in.readString();
        frameX = in.readLong();
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

    private long frameX;


    public String getUrlmedia() {
        return urlmedia;
    }

    public void setUrlmedia(String urlmedia) {

        this.urlmedia = urlmedia;
    }

    public Long getFramex(){
        return frameX;
    }


    public void loadFrame(){
            Random rand = new Random();

            frameX = (long)rand.nextInt((380 - 150) + 1) + 150;

            frameX = frameX * 1000000;

        try {
            thumb = retriveVideoFrameFromVideo(getUrlmedia(), frameX);
        } catch (Throwable throwable) {
            Log.e(TAG, "loadFrame: "+throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    public Bitmap thumb;
    private String name;
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
            bitmap = mediaMetadataRetriever.getFrameAtTime(tim, MediaMetadataRetriever.OPTION_CLOSEST);
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
        dest.writeLong(frameX);
        dest.writeString(urlmedia);


    }
}
