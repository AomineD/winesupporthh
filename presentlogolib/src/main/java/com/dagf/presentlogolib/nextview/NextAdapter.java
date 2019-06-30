package com.dagf.presentlogolib.nextview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dagf.presentlogolib.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class NextAdapter extends RecyclerView.Adapter<NextAdapter.NextHolder> {

    private ArrayList<NextViewItem> nextViewItems = new ArrayList<>();
    private Context c;
    private NextViewDagf.OnClickNextView clickNextView;

    public void setClickListener(NextViewDagf.OnClickNextView s){
        if(clickNextView == null){
            this.clickNextView = s;
        }
    }

    public NextAdapter(Context j, @NonNull ArrayList<NextViewItem> items, @NonNull NextViewDagf.OnClickNextView list){
        this.c = j;
        this.nextViewItems = items;
        this.clickNextView = list;
    }



    @NonNull
    @Override
    public NextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(c).inflate(R.layout.next_view_item, viewGroup, false);

        return new NextHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NextHolder nextHolder, final int i) {

        final NextViewItem obj = nextViewItems.get(i);

        nextHolder.neim.setText(obj.getName());

        nextHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickNextView.clicked(obj);
            }
        });


       // Log.e(TAG, "onBindViewHolder: "+obj.getFramex());
if(obj.thumb != null)
        nextHolder.img.setImageBitmap(obj.thumb);



    }

    @Override
    public int getItemCount() {
        return nextViewItems.size();
    }

    public class NextHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView neim;

        public NextHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.thumb);
            neim = itemView.findViewById(R.id.title_next_view);
        }
    }
}
