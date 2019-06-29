package com.dagf.presentlogolib.nextview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dagf.presentlogolib.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NextViewDagf extends RelativeLayout {

    public NextViewDagf(Context context) {
        super(context);
      //  initAll();
    }

    public NextViewDagf(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setItems(ArrayList<NextViewItem> items){
arrayList = items;
        initAll();
    }




    private void initAll(){
        View inflater = LayoutInflater.from(getContext()).inflate(R.layout.next_view, (ViewGroup) getRootView(), false);

        setupNextView(inflater);

        addView(inflater);
    }

    private RecyclerView recyclerView;
    private ArrayList<NextViewItem> arrayList = new ArrayList<>();

    private void setupNextView(View inflater) {

        View first = inflater.findViewById(R.id.item_main);
        setupFirst(first);

        recyclerView = inflater.findViewById(R.id.rec_list);

        NextAdapter adapter = new NextAdapter(getContext(), arrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

    }

    /** SETEAR EL SIGUIENTE **/
    private void setupFirst(View first) {

        ImageView t = first.findViewById(R.id.thumb);
        TextView f = first.findViewById(R.id.title_next_view);

        Picasso.get().load(Uri.parse(arrayList.get(0).getUrlthumb())).fit().into(t);

        f.setText(arrayList.get(0).getName());


        first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    i.setPackage(getContext().getPackageName());

                    String ur = arrayList.get(0).getUrlmedia();
                    if(!ur.contains("http://") || ur.contains("https://"))
                        ur = "http://"+ur;

                    i.setDataAndType(Uri.parse(ur), "video/*");
                    i.putExtra("title", "Alien Media");
                   getContext().startActivity(i);
                }catch (Exception e){
                    Log.e("MAIN", "onClick: "+e.getMessage());
                }


            }
        });

    }
}
