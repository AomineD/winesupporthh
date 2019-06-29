package com.dagf.presentlogolib.nextview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dagf.presentlogolib.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull NextHolder nextHolder, int i) {

        final NextViewItem obj = nextViewItems.get(i);

        nextHolder.neim.setText(obj.getName());

        Uri thumb = getImageUri(c, obj.getUrlthumb());

        Picasso.get().load(thumb).fit().into(nextHolder.img);

        nextHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickNextView.clicked(obj);
            }
        });

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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
