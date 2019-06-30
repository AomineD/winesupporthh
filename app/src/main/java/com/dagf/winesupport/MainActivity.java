package com.dagf.winesupport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
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
import com.dagf.presentlogolib.nextview.NextViewDagf;
import com.dagf.presentlogolib.nextview.NextViewItem;
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



    private NextViewDagf nextViewDagf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WineHelper.setPlayerServiceS(this, R.mipmap.home);

        EqualizerFragment.themeColor = getResources().getColor(R.color.colorPrimary);

nextViewDagf = findViewById(R.id.nextview);

ArrayList<NextViewItem> nextViewItems = new ArrayList<>();

NextViewItem item = new NextViewItem();

item.setName("Tom clancys 1");

item.setUrlmedia("http://www.mediafire.com/file/en29m56ia03t5zz/01_J_R_T1_CS.mp4");

        NextViewItem item2 = new NextViewItem();

        item2.setName("Tom clancys 2");

        item2.setUrlmedia("http://www.mediafire.com/file/479410cg6qoe1cb/02_J_R_T1_CS.mp4");

        NextViewItem item3 = new NextViewItem();

        item3.setName("Tom clancys 3");

        item3.setUrlmedia("http://www.mediafire.com/file/l079glsu1g5xkct/03_J_R_T1_CS.mp4");

nextViewItems.add(item);
nextViewItems.add(item2);
nextViewItems.add(item3);


nextViewDagf.setItems(nextViewItems, new NextViewDagf.OnClickNextView() {
    @Override
    public void clicked(NextViewItem obj) {
        Toast.makeText(MainActivity.this, obj.getUrlmedia(), Toast.LENGTH_SHORT).show();
    }
});


        final TabLayout tablay = findViewById(R.id.tablay);


        tablay.addTab(tablay.newTab().setIcon(R.drawable.alien).setText("Inicio"));

        tablay.addTab(tablay.newTab().setIcon(R.drawable.alien).setText("Musica"));

        tablay.addTab(tablay.newTab().setIcon(R.drawable.alien).setText("Equalizador"));
        WineHelper.setPlayerServiceS(this, R.mipmap.ic_launcher);
      final MusicFragment  frMusic = new MusicFragment();

        tablay.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case 0:
                     // loadFragmentNavDrawer(fragment, "home");
                        break;
                    case 1:


                        FragmentManager frm = getSupportFragmentManager();

                        frm.beginTransaction().replace(R.id.frm, frMusic).commit();

                        break;

                    case 2:

                        EqualizerFragment.setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tablay.getTabAt(1).select();
                            }
                        });
                        EqualizerFragment.setupAudioSession(MusicFragment.getAudioSessionID());

                        EqualizerFragment.themeColor = getResources().getColor(R.color.colorPrimaryDark);

                        EqualizerFragment eq = new EqualizerFragment();

                        FragmentManager frm2 = getSupportFragmentManager();

                        frm2.beginTransaction().replace(R.id.frm, eq).commit();

                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tablay.getTabAt(1).select();

    /*    if(WineHelper.shouldPresent(this))
      //  WineHelper.openPresentation(this, 1);



if(WineHelper.checkPermissionWrite(this))
        getSupportFragmentManager().beginTransaction().add(R.id.frm, new EqualizerFragment()).commitAllowingStateLoss();
else
    WineHelper.requestPermissionWrite(this);
*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == NextViewDagf.code_permision && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            nextViewDagf.showAndReload();
        }

        if(requestCode == WineHelper.keypermission && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getSupportFragmentManager().beginTransaction().add(R.id.frm, new MusicFragment()).commitAllowingStateLoss();
        }

    }
}
