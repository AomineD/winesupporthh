package com.dagf.winesupport;

import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dagf.presentlogolib.WineHelper;
import com.dagf.presentlogolib.nextview.NextViewDagf;
import com.dagf.presentlogolib.utils.MediafireParser;
import com.dagf.presentlogolib.utils.UpdateDialog;
import com.dagf.presentlogolib.utils.Updater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }



    private NextViewDagf nextViewDagf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      //  WineHelper.setPlayerServiceS(this, R.mipmap.home);



      //  WineHelper.setPlayerServiceS(this, R.mipmap.ic_launcher);

      /* if(WineHelper.shouldPresent(this))
      //  WineHelper.openPresentation(this, 1);
*/

        if(WineHelper.checkPermissionWrite(this)){
            /*try {
                Updater.check(this, "https://moviesapp.website/secure/cpanels/admin-movie-tv/api", new Updater.OnSkipListener() {
                    @Override
                    public void onSkip() {

                    }
                });
                Log.e("MAIN", "onCreate: LISTO" );
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MAIN", "onCreate: "+e.getMessage());
                e.printStackTrace();
            }*/
        }else{
            WineHelper.requestPermissionWrite(this);
        }


new MediafireParser(this, "http://www.mediafire.com/file/ybo90s674b5mt63/B0590.mp4/file", new MediafireParser.MediafireResponse() {
    @Override
    public void Loaded(String url_complete) {
       // Log.e("MAIN", "Loaded: "+url_complete );
    }
    @Override
    public void Failed(String errno) {

    }
});

       // AdsController.InitializeAppo(this, "bacc6cca6df83e71cb5e0aa778526952df968f11d0164d1f");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UpdateDialog.MY_PERMISSIONS_INSTALLFROM_UNKWONSOURCES){
            Log.e("MAIN", "onActivityResult: SI ES ACT");
            try {
                Updater.check(this, "https://moviesapp.website/secure/cpanels/admin-movie-tv/api/", new Updater.OnSkipListener() {
                    @Override
                    public void onSkip() {

                    }
                });

            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MAIN", "onCreate: "+e.getMessage() );
                e.printStackTrace();
            }
        }

        if(requestCode == UpdateDialog.key_unnistall){
            Updater.NowOpenAPK();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == NextViewDagf.code_permision && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            nextViewDagf.showAndReload();
        }

        if(requestCode == WineHelper.keypermission && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            try {
                Updater.check(this, "https://moviesapp.website/secure/cpanels/admin-movie-tv/api/", new Updater.OnSkipListener() {
                    @Override
                    public void onSkip() {

                    }
                });

            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MAIN", "onCreate: "+e.getMessage() );
                e.printStackTrace();
            }        }

    }
}
