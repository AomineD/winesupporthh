package com.dagf.presentlogolib.utils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.dagf.presentlogolib.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class UpdateDialog extends AlertDialog {
    protected UpdateDialog(Context context) {
        super(context);
    }


    private String urlT = "";

    public UpdateDialog(Context context, String urlTO){
        super(context);
this.urlT = urlTO;
    }

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        getContext().registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        getContext().registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(getContext(), R.color.transparent));

        final View click = findViewById(R.id.installit);


        textView = findViewById(R.id.texting);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
DownloadNow();
textView.setText("Descargando...");
click.setEnabled(false);
            }
        });
    }



    public void DownloadNow(){
        DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(urlT));

// If you know file name
       // String fileName = getContext().getPackageName()+".apk";

//Alternative if you don't know filename
        String fileName = URLUtil.guessFileName(urlT, null, MimeTypeMap.getFileExtensionFromUrl(urlT));

        namefil = fileName;
        dmr.setTitle(fileName);
        dmr.setDescription("apk file"); //optional
        dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        dmr.setVisibleInDownloadsUi(true);
        manager.enqueue(dmr);
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
        //    Log.e("MAIN", "onReceive: yeah" );

            textView.setText("¡Descargado!");
           // Toast.makeText(ctxt, "Descargado...", Toast.LENGTH_SHORT).show();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    openApk();
                }
            }, 2000);

        }
    };

    String namefil = "";
    private void openApk() {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/"+namefil);//name here is the name of any string you want to pass to the method
           // Log.e(TAG, "openApk: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+namefil);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }catch (Exception e){
            Log.e(TAG, "openApk ERROR: "+e.getMessage());
        }
    }

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onNotificationClick);
        getContext().unregisterReceiver(onComplete);
        super.onDetachedFromWindow();
    }
}
