package com.dagf.winesupport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dagf.presentlogolib.WineHelper;
import com.dagf.presentlogolib.eq.EqualizerFragment;
import com.dagf.presentlogolib.fragmentssec.MusicFragment;
import com.dagf.presentlogolib.fragmentssec.PlayerService;
import com.dagf.presentlogolib.utils.MediafireParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WineHelper.setPlayerServiceS(this, R.mipmap.home);

        MediaPlayer mediaPlayer = new MediaPlayer();

        int aja = mediaPlayer.getAudioSessionId();

        EqualizerFragment.audiosessionid = aja;
        EqualizerFragment.themeColor = getResources().getColor(R.color.colorPrimary);

        EqualizerFragment.setupAudioSession(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "BACK", Toast.LENGTH_SHORT).show();
            }
        }, mediaPlayer);

        if(WineHelper.shouldPresent(this))
      //  WineHelper.openPresentation(this, 1);



if(WineHelper.checkPermissionWrite(this))
        getSupportFragmentManager().beginTransaction().add(R.id.frm, new EqualizerFragment()).commitAllowingStateLoss();
else
    WineHelper.requestPermissionWrite(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == WineHelper.keypermission && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getSupportFragmentManager().beginTransaction().add(R.id.frm, new MusicFragment()).commitAllowingStateLoss();
        }

    }
}
