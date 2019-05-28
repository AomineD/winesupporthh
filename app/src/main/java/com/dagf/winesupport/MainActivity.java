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







       // Log.e("MAIN", "NUEVO URL => "+MediafireParser.generateMediaf(urr));




    }



}
