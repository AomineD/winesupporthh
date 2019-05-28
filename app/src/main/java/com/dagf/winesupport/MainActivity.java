package com.dagf.winesupport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dagf.presentlogolib.WineHelper;
import com.dagf.presentlogolib.utils.MediafireParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    ArrayList<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(WineHelper.shouldPresent(this))
        WineHelper.openPresentation(this, 0);



        String urr = "http://www.mediafire.com/file/9sucjp894lieou3/T_P_Lt_T1c2-1";


        WineHelper.getMediafireFile(this, urr, new MediafireParser.MediafireResponse() {
            @Override
            public void Loaded(String url_complete) {
                Log.e("MAIN", "Loaded: "+url_complete);
            }

            @Override
            public void Failed(String errno) {
                Log.e("MAIN", "Failed: "+errno);
            }
        });




    }



}
