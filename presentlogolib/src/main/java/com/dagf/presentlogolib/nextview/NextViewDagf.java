package com.dagf.presentlogolib.nextview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dagf.presentlogolib.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class NextViewDagf extends RelativeLayout {

    public NextViewDagf(Context context) {
        super(context);
      //  initAll();
    }

    public static final int code_permision = 838;

    public NextViewDagf(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private long timeMilis = 1800;

    public void setTimeMilis(float seconds){
        this.timeMilis = (long) (seconds * 1000);
    }

    private OnClickNextView clickNextView;
    public void setItems(ArrayList<NextViewItem> items, OnClickNextView litener){
arrayList.addAll(items);
if(arrayList.size() > 0 && arrayList.size() < 6) {
    //Log.e("MAIN", "setItems: "+(litener!=null));
    this.clickNextView = litener;

    initView();
    loadFramesOnePerOne();

}else{
    new Throwable().printStackTrace();
}
    }



    public void hide(){
        if(viewerAll != null){
            viewerAll.setVisibility(GONE);
        }
    }

    public void showNextView(){
        if(viewerAll != null){
            viewerAll.setVisibility(VISIBLE);
        }
    }

    public void showAndReload(){

      if(viewerAll != null) {
          viewerAll.setVisibility(VISIBLE);
          initAll();
      }

    }


    private View viewerAll;

    int index = 0;
    private ArrayList<Boolean> readys = new ArrayList<>();

    public boolean isReadyPerIndex(int index){
        if(index < readys.size()) {
           // Log.e(TAG, "isReadyPerIndex: "+index + " READYS ES "+readys.get(index));
            return readys.get(index);
        }
        else{
            return false;
        }
    }

    private void loadFramesOnePerOne(){

        if(arrayList.size() < 1)
            return;

  final Timer t = new Timer();



      t.schedule(new TimerTask() {
          @Override
          public void run() {
             // Log.e(TAG, "run: "+index+ " size "+arrayList.size() );
                      if(index < arrayList.size()) {
                          arrayList.get(index).loadBit = new NextViewItem.LoadBit() {
                              @Override
                              public void onBitLoaded(int pos, @Nullable Bitmap bit) {
                                  ((Activity)getContext()).runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          readys.add(true);
                                          adapter.notifyDataSetChanged();
                                      }
                                  });

                              }
                          };

                          arrayList.get(index).loadFrame();
                          index++;
                      }else {
                          t.cancel();
                      }



          }
      }, timeMilis, timeMilis);





    }

    private void initView(){
        viewerAll = LayoutInflater.from(getContext()).inflate(R.layout.next_view, (ViewGroup) getRootView(), false);

        for(int i=0; i < arrayList.size(); i++){
            arrayList.get(i).pos = i;
        }
        adapter = new NextAdapter(getContext(), arrayList, clickNextView);
    }

    private void initAll(){

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code_permision);


            return;
        }





        addView(viewerAll);
        setupNextView(viewerAll);
    }

    private RecyclerView recyclerView;
    private ArrayList<NextViewItem> arrayList = new ArrayList<>();

    private NextAdapter adapter;
    private void setupNextView(View inflater) {

        View first = inflater.findViewById(R.id.item_main);
        setupFirst(first);

        recyclerView = inflater.findViewById(R.id.rec_list);


        Log.e("MAIN", "setupNextView: "+(clickNextView != null) );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

    }

    /** SETEAR EL SIGUIENTE **/
    private void setupFirst(View first) {

        final ImageView t = first.findViewById(R.id.thumb);
        TextView f = first.findViewById(R.id.title_next_view);
        final ProgressBar bar = first.findViewById(R.id.progress_bar);
        final ImageView icon = first.findViewById(R.id.playicon);

        final NextViewItem obj = arrayList.get(0);
        obj.loadBit = new NextViewItem.LoadBit() {
            @Override
            public void onBitLoaded(int pos, Bitmap bit) {
                if(pos == 0) {

              if (bit != null) {
                  icon.setVisibility(View.VISIBLE);
                  bar.setVisibility(View.GONE);
                  Glide.with(getContext()).load(bit).apply(new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(t);

              }
          }
            }
        };

if(obj.thumb != null) {
    Glide.with(getContext()).load(obj.thumb).apply(new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(t);
    icon.setVisibility(View.VISIBLE);
    bar.setVisibility(View.GONE);
}else {
    icon.setVisibility(View.GONE);
    bar.setVisibility(View.VISIBLE);
    obj.loadFrame();
}
      //  Glide.get(getContext()).clearDiskCache();
        f.setText(arrayList.get(0).getName());


        first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               if(clickNextView != null){
                   clickNextView.clicked(arrayList.get(0));
               }


            }
        });

    }

    public interface OnClickNextView{
        void clicked(NextViewItem obj);
    }
}
