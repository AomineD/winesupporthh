package com.dagf.presentlogolib.present;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

import com.dagf.presentlogolib.R;

import java.util.prefs.Preferences;

public class PresentActivity extends AppCompatActivity {
    public static boolean isAlreadyViewed(Activity j) {
        preferences = j.getPreferences(MODE_PRIVATE);
        return preferences.getInt(k_f, 0) == 1;
    }

    private static SharedPreferences preferences;
    public static final String k_f = "JSDASDJASDSD";

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        //preferences = getPreferences(MODE_PRIVATE);
    videoView = findViewById(R.id.video_ra);

      Uri ii =  Uri.parse("android.resource://" + getPackageName() + "/"
                + getVideoId());

    videoView.setVideoURI(ii);

    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          //  Log.e("MAIN", "onCompletion: finish");
            FinishPresent();
        }
    });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null)
        getSupportActionBar().hide();

    videoView.start();
    }

    private void FinishPresent() {
        preferences.edit().putInt(k_f, 1).commit();
        finish();
    }

    public static int what_video = 0;

    private int getVideoId(){
        switch (what_video){
            case 0:
                return R.raw.splash;
            case 1:
                return R.raw.splash_2;
            case 2:
                return R.raw.splash_3;
            case 3:
                return R.raw.splash_4;
                default:
                    return R.raw.splash;
        }
    }
}
