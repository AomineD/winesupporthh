package com.dagf.presentlogolib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.provider.Settings;
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

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class UpdateDialog extends AlertDialog {
    public static final int MY_PERMISSIONS_INSTALLFROM_UNKWONSOURCES = 252;
    public static final int key_unnistall = 953;

    protected UpdateDialog(Context context) {
        super(context);
    }


    private String urlT = "";
    View click;
    private Activity activity;
    public UpdateDialog(Activity context, String urlTO){
        super(context);
        this.activity = context;
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

        click = findViewById(R.id.installit);


        View skipView= findViewById(R.id.skip);

        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipListener.onSkip();
                dismiss();
            }
        });

        textView = findViewById(R.id.texting);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

DownloadNow();



            }
        });
    }


    public void downloadAgain(){
        DownloadNow();
    }

    public void DownloadNow(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getContext().getPackageManager().canRequestPackageInstalls()) {
                activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getContext().getPackageName()))), MY_PERMISSIONS_INSTALLFROM_UNKWONSOURCES);
                return;
            }
        }

        textView.setText("Descargando...");

        Log.e(TAG, "DownloadNow: "+urlT);

        DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(urlT));


        String fileName = URLUtil.guessFileName(urlT, null, MimeTypeMap.getFileExtensionFromUrl(urlT));

        namefil = fileName;

        String s  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/"+namefil;
        File file = new File(s);

        if(file.exists()){
            file.delete();
        }

        dmr.setTitle(fileName);
        dmr.setDescription("Actualizacion de "+getContext().getString(R.string.app_name)); //optional
        dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        dmr.setVisibleInDownloadsUi(true);
        manager.enqueue(dmr);
        click.setEnabled(false);
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

            textView.setText("¡Descargado!");

            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dismiss();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            openApk();

                            }
                        });

                    }
                }
            };
            timer.start();


        }
    };

    String namefil = "";
    public void openApk() {
        try {
            String s  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/"+namefil;
            File file = new File(s);//name here is the name of any string you want to pass to the method

            Log.e(TAG, "openApk: " + file.getAbsolutePath());

        //    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            Intent downloadIntent;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String MY_PROVIDER = getContext().getApplicationContext().getPackageName() + ".fileprovider";
                Uri apkUri = FileProvider.getUriForFile(getContext(),  MY_PROVIDER, file);



                getContext().grantUriPermission(MY_PROVIDER, apkUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContext().grantUriPermission(MY_PROVIDER, apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContext().grantUriPermission(MY_PROVIDER, apkUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);


                downloadIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                downloadIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                //intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                downloadIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
               downloadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                downloadIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                Log.e(TAG, "openApk: "+apkUri);
                downloadIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                downloadIntent = new Intent(Intent.ACTION_VIEW);
                downloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                downloadIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }

            getContext().startActivity(downloadIntent);
            activity.finish();
            Log.e(TAG, "openApk new: "+ downloadIntent.getData().toString());
        }catch (Exception e){
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "openApk ERROR: "+e.getMessage());
        }
    }

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
   //         Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onNotificationClick);
        getContext().unregisterReceiver(onComplete);
        super.onDetachedFromWindow();
    }

    private Updater.OnSkipListener skipListener;
    public void setSkipListener(Updater.OnSkipListener listener) {
this.skipListener = listener;
    }
}
