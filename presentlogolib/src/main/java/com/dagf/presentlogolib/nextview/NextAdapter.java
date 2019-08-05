package com.dagf.presentlogolib.nextview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dagf.presentlogolib.R;

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
if(obj.thumb != null) {



    Glide.with(c).load(obj.thumb).apply(new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(nextHolder.img);

    nextHolder.play_ic.setVisibility(View.VISIBLE);
    nextHolder.bar.setVisibility(View.GONE);
}else{
    nextHolder.play_ic.setVisibility(View.GONE);
    nextHolder.bar.setVisibility(View.VISIBLE);
}


    }

    @Override
    public int getItemCount() {
        return nextViewItems.size();
    }

    public class NextHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView neim;
        private ImageView play_ic;
        private ProgressBar bar;

        public NextHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.thumb);
            neim = itemView.findViewById(R.id.title_next_view);
            play_ic = itemView.findViewById(R.id.playicon);
            bar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
