package com.dagf.presentlogolib.nextview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dagf.presentlogolib.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

    private OnClickNextView clickNextView;
    public void setItems(ArrayList<NextViewItem> items, OnClickNextView litener){
arrayList.addAll(items);
if(arrayList.size() > 0 && arrayList.size() < 6) {
    //Log.e("MAIN", "setItems: "+(litener!=null));
    this.clickNextView = litener;
    initAll();


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
        if(viewerAll != null){
            viewerAll.setVisibility(VISIBLE);
            initAll();
        }
    }


    private View viewerAll;

    private void initAll(){

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code_permision);


            return;
        }

        viewerAll = LayoutInflater.from(getContext()).inflate(R.layout.next_view, (ViewGroup) getRootView(), false);



        addView(viewerAll);
        setupNextView(viewerAll);
    }

    private RecyclerView recyclerView;
    private ArrayList<NextViewItem> arrayList = new ArrayList<>();

    private void setupNextView(View inflater) {

        for(int i=0; i < arrayList.size(); i++){
            arrayList.get(i).loadFrame();
        }

        View first = inflater.findViewById(R.id.item_main);
        setupFirst(first);

        recyclerView = inflater.findViewById(R.id.rec_list);

        NextAdapter adapter = new NextAdapter(getContext(), arrayList, clickNextView);
        Log.e("MAIN", "setupNextView: "+(clickNextView != null) );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

    }

    /** SETEAR EL SIGUIENTE **/
    private void setupFirst(View first) {

        ImageView t = first.findViewById(R.id.thumb);
        TextView f = first.findViewById(R.id.title_next_view);

        NextViewItem obj = arrayList.get(0);



        RequestOptions options = new RequestOptions().frame(obj.getFramex());
       // Log.e(TAG, "setupFirst: "+obj.getUrlmedia()+ " "+obj.getFramex() );
        Glide.with(getContext()).load(obj.getUrlmedia()).apply(options).into(t);

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


   /* private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
try {
    return Uri.parse(path);
}catch (Exception e){
    return Uri.parse("");
}
}*/

    public interface OnClickNextView{
        void clicked(NextViewItem obj);
    }
}
