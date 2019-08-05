package com.dagf.presentlogolib.nextview;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class NextViewItem implements Parcelable {
    protected NextViewItem(Parcel in) {
        name = in.readString();
        frameX = in.readLong();
        urlmedia = in.readString();
        thumb = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    public NextViewItem(){

    }

    public void setlistener(LoadBit l){
        this.loadBit = l;
    }

    public interface LoadBit{
        void onBitLoaded(int pos, @Nullable Bitmap bit);
    }


    public LoadBit loadBit;
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

        if(thumb != null){
            return;
        }

        Random rand = new Random();

            frameX = (long)rand.nextInt((2800 - 450) + 1) + 450;

            frameX = frameX * 1000000;

        try {

            thumb = retriveVideoFrameFromVideo(getUrlmedia(), frameX, pos, loadBit);
        } catch (Throwable throwable) {
            Log.e(TAG, "loadFrame: "+throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    public Bitmap thumb;
    private String name;
private String urlmedia;
    public int pos = 0;

    public static Bitmap retriveVideoFrameFromVideo(String videoPath, long tim, int pos, LoadBit loadBit)throws Throwable
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
            if(Build.VERSION.SDK_INT < 27)
            bitmap = mediaMetadataRetriever.getFrameAtTime(tim, MediaMetadataRetriever.OPTION_CLOSEST);
            else
                bitmap = mediaMetadataRetriever.getScaledFrameAtTime(tim, MediaMetadataRetriever.OPTION_CLOSEST, 120, 120);
            loadBit.onBitLoaded(pos, bitmap);

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
        Log.e(TAG, "retriveVideoFrameFromVideo: "+pos);
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
        dest.writeValue(thumb);


    }
}
