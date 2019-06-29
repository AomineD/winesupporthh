package com.dagf.presentlogolib.nextview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;

public class NextAdapter extends RecyclerView.Adapter<NextAdapter.NextHolder> {

    private ArrayList<NextViewItem> nextViewItems = new ArrayList<>();
    private Context c;

    public NextAdapter(Context j, @NonNull ArrayList<NextViewItem> items){
        this.c = j;
        this.nextViewItems = items;
    }



    @NonNull
    @Override
    public NextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(c).inflate(R.layout.next_view_item, viewGroup);

        return new NextHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NextHolder nextHolder, int i) {

        final NextViewItem obj = nextViewItems.get(i);

        nextHolder.neim.setText(obj.getName());

        Picasso.get().load(Uri.parse(obj.getUrlthumb())).fit().into(nextHolder.img);

        nextHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    i.setPackage(c.getPackageName());

                    String ur = obj.getUrlmedia();
                    if(!ur.contains("http://") || ur.contains("https://"))
                    ur = "http://"+ur;

                    i.setDataAndType(Uri.parse(ur), "video/*");
                    i.putExtra("title", "Alien Media");
                    c.startActivity(i);
                }catch (Exception e){
                    Log.e("MAIN", "onClick: "+e.getMessage());
                }
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
}
