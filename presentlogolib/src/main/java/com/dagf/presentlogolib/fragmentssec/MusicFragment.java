package com.dagf.presentlogolib.fragmentssec;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.dagf.presentlogolib.R;
import com.dagf.presentlogolib.eq.EqualizerFragment;
import com.dagf.presentlogolib.models.ItemMyPlayList;
import com.dagf.presentlogolib.models.ItemSong;
import com.dagf.presentlogolib.models.MessageEvent;
import com.dagf.presentlogolib.utils.Constant;
import com.dagf.presentlogolib.utils.DBHelper;
import com.dagf.presentlogolib.utils.GlobalBus;
import com.dagf.presentlogolib.utils.Methods;
import com.dagf.presentlogolib.utils.PausableRotateAnimation;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnClickListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Methods methods;
    private TabLayout tabLayout;
    public static SlidingUpPanelLayout mLayout;
    public static DBHelper dbHelper;

    public static int getAudioSessionID(){
        return PlayerService.vrgachamo;
    }

    public void setDrawableb(int drawableb) {
        Drawableb = drawableb;
    }

    private int Drawableb = 0;

    DrawerLayout drawer;
    public ViewPager viewpager;
    ImagePagerAdapter adapter;
    AudioManager am;
    Toolbar toolbar;
    Boolean isExpand = false, isRotateAnim = false;
    BottomSheetDialog dialog_desc;
    Dialog dialog_rate;
    RelativeLayout rl_min_header;
    LinearLayout ll_max_header;
    RelativeLayout rl_music_loading;
    private Handler seekHandler = new Handler();
    private Handler adHandler = new Handler();
    PausableRotateAnimation rotateAnimation;
    String deviceId;

    RatingBar ratingBar;
    SeekBar seekBar_music, seekbar_min;
    View view_playlist, view_download, view_rate, view_round;
    TextView tv_min_title, tv_min_artist, tv_max_title, tv_max_artist, tv_music_title, tv_music_artist, tv_song_count,
            tv_current_time, tv_total_time;
    RoundedImageView iv_max_song, iv_min_song, imageView_pager;
    ImageView iv_music_bg, iv_min_previous, iv_min_play, iv_min_next, iv_max_fav, iv_max_option, iv_music_shuffle,
            iv_music_repeat, iv_music_previous, iv_music_next, iv_music_play, iv_music_add2playlist, iv_music_share,
            iv_music_download, iv_music_rate, iv_music_volume, imageView_heart;

    LinearLayout ll_adView;
    Boolean isBannerLoaded = false;
    Animation anim_slideup, anim_slidedown, anim_slideup_music, anim_slidedown_music;


    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment

        View v = inflater.inflate(R.layout.fragment_musicp, container, false);


        Oncreate(v);
        Constant.isAppOpen = true;
        EqualizerFragment.setupAudioSession(getAudioSessionID());

        EqualizerFragment.actEq(getActivity());

        return v;
    }



    private void Oncreate(View v) {


      //  FrameLayout contentFrameLayout = v.findViewById(R.id.content_frame);
      //  getLayoutInflater().inflate(R.layout.activity_offline_music, contentFrameLayout);

      //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

      //  methods.forceRTLIfSupported(getActivity().getWindow());
        methods = new Methods(getActivity());


        dbHelper = new DBHelper(getActivity().getApplicationContext());
   //     toolbar.setVisibility(View.GONE);
        //Toolbar toolbar_off = v.findViewById(R.id.toolbar_offline);
        mLayout = v.findViewById(R.id.sliding_layout);

      //  toolbar_off.setTitle(getString(R.string.music_library));
       /* setSupportActionBar(toolbar_off);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);*/

        //Log.e(TAG, "Oncreate: LLEGO A ONCREATE(VIEW) , dbhelper es => "+(dbHelper!=null));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        Log.e(TAG, "Oncreate: LLEGO MAS ABAJO DE ADAPTER");

        mViewPager = v.findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);

        tabLayout = v.findViewById(R.id.tabs);

        if (checkPer()) {
            initTabs();
//            new LoadOfflineSongs().execute();
        }

        ffTOPAY(v);

    }

    private void initTabs() {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    //    Log.e(TAG, "initTabs: "+(mSectionsPagerAdapter != null) +" <= NO ES NULL, SIZE => "+(mViewPager.getVisibility() == View.VISIBLE));

        mViewPager.setCurrentItem(1);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            try {
                super.restoreState(state, loader);
            }catch (Exception e){
                Log.e(TAG, "restoreState: "+e.getMessage());
            }
        }

        SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
          //  Log.e(TAG, "SectionsPagerAdapter: lol");
        }

        @Override
        public Fragment getItem(int position) {
        //    Log.e(TAG, "getItem: si entro en adapter => "+position);
            switch (position) {
                case 0:
                   // Log.e(TAG, "getItem:  RETORNANDO PRIMERA "+position);
                    return FragmentOFSongs.newInstance(position);
                case 1:
                    return FragmentOFPlaylist.newInstance(position);
                case 2:
                    return FragmentOFArtist.newInstance(position);
                case 3:
                    return FragmentOFAlbums.newInstance(position);
                default:
                    return FragmentOFSongs.newInstance(position);
            }
        }


        @Override
        public int getCount() {
            return 4;
        }
    }

    /*@Override
    public void onBackPressed() {
        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }*/

    public Boolean checkPer() {

        if ((ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"}, 1);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                    initTabs();
//                    new LoadOfflineSongs().execute();
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(getContext(), getResources().getString(R.string.err_cannot_use_features), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }








    /**  ====================================================================================  **/

private View vj;

    public void ffTOPAY(View v){
vj = v;

        Constant.context = getActivity();
        deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        methods = new Methods(getActivity());
        dbHelper = new DBHelper(getActivity());
       // dbHelper.getAbout();
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ll_adView = v.findViewById(R.id.ll_adView);

        mLayout = v.findViewById(R.id.sliding_layout);
      //  toolbar = v.findViewById(R.id.toolbar_offline_music);
       // setSupportActionBar(toolbar);
       // methods.forceRTLIfSupported(getWindow());

        drawer = v.findViewById(R.id.drawer_layout);
      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
*/
        adapter = new ImagePagerAdapter();

       // navigationView = v.findViewById(R.id.nav_view);
        viewpager = v.findViewById(R.id.viewPager_song);
        rl_min_header = v.findViewById(R.id.rl_min_header);
        ll_max_header = v.findViewById(R.id.ll_max_header);
        rl_music_loading = v.findViewById(R.id.rl_music_loading);
        ratingBar = v.findViewById(R.id.rb_music);
        seekBar_music = v.findViewById(R.id.seekbar_music);
        seekbar_min = v.findViewById(R.id.seekbar_min);
        seekbar_min.setPadding(0, 0, 0, 0);

        RelativeLayout rl = v.findViewById(R.id.rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iv_music_bg = v.findViewById(R.id.iv_music_bg);

        if(Drawableb != 0){
            iv_music_bg.setImageDrawable(getResources().getDrawable(Drawableb));
        }

        iv_music_play = v.findViewById(R.id.iv_music_play);
        iv_music_next = v.findViewById(R.id.iv_music_next);
        iv_music_previous = v.findViewById(R.id.iv_music_previous);
        iv_music_shuffle = v.findViewById(R.id.iv_music_shuffle);
        iv_music_repeat = v.findViewById(R.id.iv_music_repeat);
        iv_music_add2playlist = v.findViewById(R.id.iv_music_add2playlist);
        iv_music_share = v.findViewById(R.id.iv_music_share);
        iv_music_download = v.findViewById(R.id.iv_music_download);
        iv_music_rate = v.findViewById(R.id.iv_music_rate);
        iv_music_volume = v.findViewById(R.id.iv_music_volume);

        view_rate = v.findViewById(R.id.view_music_rate);
        view_download = v.findViewById(R.id.view_music_download);
        view_playlist = v.findViewById(R.id.view_music_playlist);
        view_round = v.findViewById(R.id.vBgLike);

        iv_min_song = v.findViewById(R.id.iv_min_song);
        iv_max_song = v.findViewById(R.id.iv_max_song);
        iv_min_previous = v.findViewById(R.id.iv_min_previous);
        iv_min_play = v.findViewById(R.id.iv_min_play);
        iv_min_next = v.findViewById(R.id.iv_min_next);
        iv_max_fav = v.findViewById(R.id.iv_max_fav);
        iv_max_option = v.findViewById(R.id.iv_max_option);
        imageView_heart = v.findViewById(R.id.ivLike);

        tv_current_time = v.findViewById(R.id.tv_music_time);
        tv_total_time = v.findViewById(R.id.tv_music_total_time);
        tv_song_count = v.findViewById(R.id.tv_music_song_count);
        tv_music_title = v.findViewById(R.id.tv_music_title);
        tv_music_artist = v.findViewById(R.id.tv_music_artist);
        tv_min_title = v.findViewById(R.id.tv_min_title);
        tv_min_artist = v.findViewById(R.id.tv_min_artist);
        tv_max_title = v.findViewById(R.id.tv_max_title);
        tv_max_artist = v.findViewById(R.id.tv_max_artist);

        iv_max_option.setColorFilter(Color.WHITE);

        iv_max_fav.setOnClickListener(this);
        iv_max_option.setOnClickListener(this);

        iv_min_play.setOnClickListener(this);
        iv_min_next.setOnClickListener(this);
        iv_min_previous.setOnClickListener(this);

        iv_music_play.setOnClickListener(this);
        iv_music_next.setOnClickListener(this);
        iv_music_previous.setOnClickListener(this);
        iv_music_shuffle.setOnClickListener(this);
        iv_music_repeat.setOnClickListener(this);
        iv_music_add2playlist.setOnClickListener(this);
        iv_music_share.setOnClickListener(this);
        iv_music_download.setOnClickListener(this);
        iv_music_rate.setOnClickListener(this);
        iv_music_volume.setOnClickListener(this);

        ImageView iv_white_blur = v.findViewById(R.id.iv_music_white_blur);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (50 * methods.getScreenHeight() / 100));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        iv_white_blur.setLayoutParams(params);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 0.0f) {
                    isExpand = false;

                    rl_min_header.setVisibility(View.VISIBLE);
                    ll_max_header.setVisibility(View.INVISIBLE);
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    rl_min_header.setVisibility(View.VISIBLE);
                    ll_max_header.setVisibility(View.VISIBLE);

                    if (isExpand) {
                        rl_min_header.setAlpha(1.0f - slideOffset);
                        ll_max_header.setAlpha(0.0f + slideOffset);
                    } else {
                        rl_min_header.setAlpha(1.0f - slideOffset);
                        ll_max_header.setAlpha(slideOffset);
                    }
                } else {
                    isExpand = true;

                    rl_min_header.setVisibility(View.INVISIBLE);
                    ll_max_header.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    try {
                        viewpager.setCurrentItem(Constant.playPos);
                    } catch (Exception e) {
                        adapter.notifyDataSetChanged();
                        viewpager.setCurrentItem(Constant.playPos);
                    }
                }
            }
        });

        seekBar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                try {
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_SEEKTO);
                    intent.putExtra("seekto", methods.getSeekFromPercentage(progress, methods.calculateTime(Constant.arrayList_play.get(Constant.playPos).getDuration())));
                    getActivity().startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        newRotateAnim();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Constant.isScrolled = true;
            }

            @Override
            public void onPageSelected(int position) {
                changeTextPager(Constant.arrayList_play.get(position));

                View view = viewpager.findViewWithTag("myview" + position);
                if (view != null) {
                    ImageView iv = view.findViewById(R.id.iv_vp_play);
                    if (Constant.playPos == position) {
                        iv.setVisibility(View.GONE);
                    } else {
                        iv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_current_time.setText("00:00");


        if (Constant.pushSID.equals("0")) {
            if (Constant.arrayList_play.size() == 0) {
                Constant.arrayList_play.addAll(dbHelper.loadDataRecent(true));
                if (Constant.arrayList_play.size() > 0) {
                    GlobalBus.getBus().postSticky(Constant.arrayList_play.get(Constant.playPos));
                }
            }
        } else {

        }

        setUpBannerAdonMusic();
        startAdTimeCount();
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            /*case R.id.menu_search_home:
//                searchView.openSearch();
                break;*/

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_min_play) {
            playPause();
        } else if (i == R.id.iv_music_play) {
            playPause();
        } else if (i == R.id.iv_min_next) {
            next();
        } else if (i == R.id.iv_music_next) {
            next();
        } else if (i == R.id.iv_min_previous) {
            previous();
        } else if (i == R.id.iv_music_previous) {
            previous();
        } else if (i == R.id.iv_music_shuffle) {
            setShuffle();
        } else if (i == R.id.iv_music_repeat) {
            setRepeat();
        } else if (i == R.id.iv_max_option) {/*
            if (Constant.arrayList_play.size() > 0) {
             //   showBottomSheetDialog();
            }*/
        } else if (i == R.id.iv_max_fav) {
            if (Constant.arrayList_play.size() > 0) {
                if (Constant.isOnline) {
                    methods.animateHeartButton(view);
//                        methods.animatePhotoLike(view_round, imageView_heart);
                    view.setSelected(!view.isSelected());
                    vj.findViewById(R.id.ivLike).setSelected(view.isSelected());
                    fav();
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.iv_music_share) {
            shareSong();
        } else if (i == R.id.iv_music_add2playlist) {
            if (Constant.arrayList_play.size() > 0) {
                methods.openPlaylists(Constant.arrayList_play.get(viewpager.getCurrentItem()), Constant.isOnline);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.iv_music_download) {


                            if (checkPer()) {
                                download();
                            } else {
                                checkPer();
                            }


        } else if (i == R.id.iv_music_rate) {
            if (Constant.arrayList_play.size() > 0) {
                //openRateDialog();
            }
        } else if (i == R.id.iv_music_volume) {
            changeVolume();
        }
    }

    private boolean isRewerd;

    /*public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.layout_desc, null);

        dialog_desc = new BottomSheetDialog(getActivity());
        dialog_desc.setContentView(view);
        dialog_desc.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog_desc.show();

        AppCompatButton button = dialog_desc.findViewById(R.id.button_detail_close);
        TextView textView = dialog_desc.findViewById(R.id.tv_desc_title);
        textView.setText(Constant.arrayList_play.get(Constant.playPos).getTitle());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_desc.dismiss();
            }
        });

        WebView webview_song_desc = dialog_desc.v.findViewById(R.id.webView_bottom);
        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        String text = "<html><head>"
                + "<style> body{color: #000 !important;text-align:left}"
                + "</style></head>"
                + "<body>"
                + Constant.arrayList_play.get(Constant.playPos).getDescription()
                + "</body></html>";

        webview_song_desc.loadData(text, mimeType, encoding);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };*/

    public void seekUpdation() {
        try {
            seekbar_min.setProgress(methods.getProgressPercentage(PlayerService.exoPlayer.getCurrentPosition(), methods.calculateTime(Constant.arrayList_play.get(Constant.playPos).getDuration())));
            seekBar_music.setProgress(methods.getProgressPercentage(PlayerService.exoPlayer.getCurrentPosition(), methods.calculateTime(Constant.arrayList_play.get(Constant.playPos).getDuration())));
            tv_current_time.setText(methods.milliSecondsToTimer(PlayerService.exoPlayer.getCurrentPosition()));


            seekBar_music.setSecondaryProgress(PlayerService.exoPlayer.getBufferedPercentage());
     //       Log.e(TAG, "seekUpdation: "+Constant.isAppOpen);
            if (PlayerService.exoPlayer.getPlayWhenReady() && Constant.isAppOpen) {
                seekHandler.removeCallbacks(run);
                seekHandler.postDelayed(run, 1000);
              //  Log.e("MAIN", "seekUpdation: "+methods.milliSecondsToTimer(PlayerService.exoPlayer.getCurrentPosition()));

            }
        } catch (Exception e) {
            Log.e("MAIN", "ERROR EM PLAY "+e);
            e.printStackTrace();
        }
    }

    public void playPause() {
        if (Constant.arrayList_play.size() > 0) {
            Intent intent = new Intent(getActivity(), PlayerService.class);
            if (Constant.isPlayed) {
                intent.setAction(PlayerService.ACTION_TOGGLE);
                getActivity().startService(intent);
            } else {
                if (!Constant.isOnline || methods.isNetworkAvailable()) {
                    intent.setAction(PlayerService.ACTION_PLAY);
                    getActivity().startService(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void next() {
        if (Constant.arrayList_play.size() > 0) {
            if (!Constant.isOnline || methods.isNetworkAvailable()) {
                isRotateAnim = false;
                Intent intent = new Intent(getActivity(), PlayerService.class);
                intent.setAction(PlayerService.ACTION_NEXT);
                getActivity().startService(intent);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void previous() {
        if (Constant.arrayList_play.size() > 0) {
            if (!Constant.isOnline || methods.isNetworkAvailable()) {
                isRotateAnim = false;
                Intent intent = new Intent(getActivity(), PlayerService.class);
                intent.setAction(PlayerService.ACTION_PREVIOUS);
                getActivity().startService(intent);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void setRepeat() {
        if (Constant.isRepeat) {
            Constant.isRepeat = false;
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.mipmap.ic_repeat));
        } else {
            Constant.isRepeat = true;
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.mipmap.ic_repeat_hover));
        }
    }

    public void setShuffle() {
        if (Constant.isSuffle) {
            Constant.isSuffle = false;
            iv_music_shuffle.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            Constant.isSuffle = true;
            iv_music_shuffle.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }
    }

    private void shareSong() {
        if (Constant.arrayList_play.size() > 0) {

            if (Constant.isOnline) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_song));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Constant.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_song)));
            } else {
                if (checkPer()) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/mp3");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Constant.arrayList_play.get(viewpager.getCurrentItem()).getUrl()));
                    share.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Constant.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    startActivity(Intent.createChooser(share, getResources().getString(R.string.share_song)));
                }
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void fav() {
        if (dbHelper.checkFav(Constant.arrayList_play.get(Constant.playPos).getId())) {
            dbHelper.removeFromFav(Constant.arrayList_play.get(Constant.playPos).getId());
            Toast.makeText(getActivity(), getResources().getString(R.string.removed_fav), Toast.LENGTH_SHORT).show();
            changeFav(false);
        } else {
            dbHelper.addToFav(Constant.arrayList_play.get(Constant.playPos));
            Toast.makeText(getActivity(), getResources().getString(R.string.added_fav), Toast.LENGTH_SHORT).show();
            changeFav(true);
        }
    }

    public void changeFav(Boolean isFav) {
        if (isFav) {
            iv_max_fav.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fav_hover));
        } else {
            iv_max_fav.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fav));
        }
    }


    private void changeVolume() {

        final RelativePopupWindow popupWindow = new RelativePopupWindow(getActivity());

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_dailog_volume, null);
        ImageView imageView1 = view.findViewById(R.id.iv1);
        ImageView imageView2 = view.findViewById(R.id.iv2);
        imageView1.setColorFilter(Color.BLACK);
        imageView2.setColorFilter(Color.BLACK);

        VerticalSeekBar seekBar = view.findViewById(R.id.seekbar_volume);
        seekBar.getThumb().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        seekBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        final AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(volume_level);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setContentView(view);
        popupWindow.showOnAnchor(iv_music_volume, RelativePopupWindow.VerticalPosition.ABOVE, RelativePopupWindow.HorizontalPosition.CENTER);
    }

    private void download() {
        if (Constant.arrayList_play.size() > 0) {

            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name));
            if (!root.exists()) {
                root.mkdirs();
            }

            File file = new File(root, Constant.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + ".mp3");

            if (!file.exists()) {

                String url = Constant.arrayList_play.get(viewpager.getCurrentItem()).getUrl();

              /*  if (!DownloadService.getInstance().isDownloading()) {
                    Intent serviceIntent = new Intent(getActivity(), DownloadService.class);
                    serviceIntent.setAction(DownloadService.ACTION_START);
                    serviceIntent.putExtra("downloadUrl", url);
                    serviceIntent.putExtra("file_path", root.toString());
                    serviceIntent.putExtra("file_name", file.getName());
                    getActivity().startService(serviceIntent);
                } else {
                    Intent serviceIntent = new Intent(getActivity(), DownloadService.class);
                    serviceIntent.setAction(DownloadService.ACTION_ADD);
                    serviceIntent.putExtra("downloadUrl", url);
                    serviceIntent.putExtra("file_path", root.toString());
                    serviceIntent.putExtra("file_name", file.getName());
                    getActivity().startService(serviceIntent);
                }

                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String json = JsonUtils.okhttpGET(Constant.URL_DOWNLOAD_COUNT + Constant.arrayList_play.get(viewpager.getCurrentItem()).getId());
                        return null;
                    }
                }.execute();*/
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.already_download), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void newRotateAnim() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
        rotateAnimation = new PausableRotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(Constant.rotateSpeed);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
    }

    public void changeImageAnimation(Boolean isPlay) {
        try {
            if (!isPlay) {
                playAnimt.pauseAnimation();
                rotateAnimation.pause();
            } else {
                if (!isRotateAnim) {
                    isRotateAnim = true;
                    if (imageView_pager != null) {
                        imageView_pager.setAnimation(null);
                    }
                    View view_pager = viewpager.findViewWithTag("myview" + Constant.playPos);
                    newRotateAnim();
                    imageView_pager = view_pager.findViewById(R.id.image);
                    playAnimt = view_pager.findViewById(R.id.play_animation);
                    playAnimt.playAnimation();
                  //  imageView_pager.startAnimation(rotateAnimation);
                } else {
                    playAnimt.playAnimation();
                    rotateAnimation.resume();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LottieAnimationView playAnimt;

    public void changeTextPager(ItemSong itemSong) {
        ratingBar.setRating(Integer.parseInt(itemSong.getAverageRating()));

        tv_music_artist.setText(itemSong.getArtist());
        tv_music_title.setText(itemSong.getTitle());
        tv_song_count.setText((viewpager.getCurrentItem() + 1) + "/" + Constant.arrayList_play.size());
    }

    public void changeText(final ItemSong itemSong, final String page) {

        tv_min_title.setText(itemSong.getTitle());
        tv_min_artist.setText(itemSong.getArtist());

        tv_max_title.setText(itemSong.getTitle());
        tv_max_artist.setText(itemSong.getArtist());

        ratingBar.setRating(Integer.parseInt(itemSong.getAverageRating()));
        tv_music_title.setText(itemSong.getTitle());
        tv_music_artist.setText(itemSong.getArtist());

        tv_song_count.setText(Constant.playPos + 1 + "/" + Constant.arrayList_play.size());
        tv_total_time.setText(itemSong.getDuration());

        changeFav(dbHelper.checkFav(itemSong.getId()));

        if (Constant.isOnline) {
            Picasso.get()
                    .load(itemSong.getImageSmall())
                    .placeholder(R.drawable.placeholder_song)
                    .into(iv_min_song);

            Picasso.get()
                    .load(itemSong.getImageSmall())
                    .placeholder(R.drawable.placeholder_song)
                    .into(iv_max_song);

            if (ratingBar.getVisibility() == View.GONE) {
                ratingBar.setVisibility(View.VISIBLE);
                iv_max_fav.setVisibility(View.VISIBLE);

                iv_music_rate.setVisibility(View.VISIBLE);
                view_rate.setVisibility(View.VISIBLE);
            }

            if (Constant.isSongDownload) {
                iv_music_download.setVisibility(View.VISIBLE);
                view_download.setVisibility(View.VISIBLE);
            } else {
                iv_music_download.setVisibility(View.GONE);
                view_download.setVisibility(View.GONE);
            }
        } else {
            Picasso.get()
                    .load(methods.getAlbumArtUri(Integer.parseInt(itemSong.getImageSmall())))
                    .placeholder(R.drawable.placeholder_song)
                    .into(iv_min_song);

            Picasso.get()
                    .load(methods.getAlbumArtUri(Integer.parseInt(itemSong.getImageSmall())))
                    .placeholder(R.drawable.placeholder_song)
                    .into(iv_max_song);

            if (ratingBar.getVisibility() == View.VISIBLE) {
                ratingBar.setVisibility(View.GONE);
                iv_max_fav.setVisibility(View.GONE);

                iv_music_rate.setVisibility(View.GONE);
                view_rate.setVisibility(View.GONE);

                iv_music_download.setVisibility(View.GONE);
                view_download.setVisibility(View.GONE);
            }
        }

        if (!page.equals("")) {
            viewpager.setAdapter(adapter);
            viewpager.setOffscreenPageLimit(Constant.arrayList_play.size());
        }
        viewpager.setCurrentItem(Constant.playPos);
    }

    public void changePlayPauseIcon(Boolean isPlay) {
        if (!isPlay) {
            iv_music_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
            iv_min_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play_grey));

            if(playAnimt != null){
                playAnimt.pauseAnimation();
            }
        } else {
            if(playAnimt != null){
                playAnimt.playAnimation();
            }
            iv_music_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            iv_min_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_grey));
        }
        seekUpdation();
    }

    public void isBuffering(Boolean isBuffer) {
        Constant.isPlaying = !isBuffer;
        if (isBuffer) {
            rl_music_loading.setVisibility(View.VISIBLE);
            iv_music_play.setVisibility(View.INVISIBLE);
        } else {
            rl_music_loading.setVisibility(View.INVISIBLE);
            iv_music_play.setVisibility(View.VISIBLE);
            changePlayPauseIcon(!isBuffer);
//            seekUpdation();
        }
        iv_music_next.setEnabled(!isBuffer);
        iv_music_previous.setEnabled(!isBuffer);
        iv_min_next.setEnabled(!isBuffer);
        iv_min_previous.setEnabled(!isBuffer);
        iv_music_download.setEnabled(!isBuffer);
        iv_min_play.setEnabled(!isBuffer);
        seekBar_music.setEnabled(!isBuffer);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        private ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return Constant.arrayList_play.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View imageLayout = inflater.inflate(R.layout.layout_viewpager, container, false);
            assert imageLayout != null;
            RoundedImageView imageView = imageLayout.findViewById(R.id.image);
            final ImageView imageView_play = imageLayout.findViewById(R.id.iv_vp_play);
            final ProgressBar spinner = imageLayout.findViewById(R.id.loading);

            if (Constant.playPos == position) {
                imageView_play.setVisibility(View.GONE);
            }

            if (Constant.isOnline) {
                Picasso.get()
                        .load(Constant.arrayList_play.get(position).getImageBig())
                        .resize(300, 300)
                        .placeholder(R.drawable.cd)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                spinner.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                spinner.setVisibility(View.GONE);
                            }
                        });
            } else {
                Picasso.get()
                        .load(methods.getAlbumArtUri(Integer.parseInt(Constant.arrayList_play.get(position).getImageBig())))
                        .placeholder(R.drawable.placeholder_song)
                        .into(imageView);
                spinner.setVisibility(View.GONE);
            }

            imageView_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.playPos = viewpager.getCurrentItem();
                    isRotateAnim = false;
                    if (!Constant.isOnline || methods.isNetworkAvailable()) {
                        Intent intent = new Intent(getActivity(), PlayerService.class);
                        intent.setAction(PlayerService.ACTION_PLAY);
                        getActivity().startService(intent);
                        imageView_play.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (position == 0) {
                isRotateAnim = false;
                imageView_pager = imageView;
            }

            imageLayout.setTag("myview" + position);
            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private void openOptionPopUp() {
        PopupMenu popup = new PopupMenu(getActivity(), iv_max_option);
        popup.getMenuInflater().inflate(R.menu.popup_base_option, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_base_share) {
                    if (Constant.isOnline) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_song));
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Constant.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_song)));
                    } else {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("audio/mp3");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Constant.arrayList_play.get(viewpager.getCurrentItem()).getUrl()));
                        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Constant.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_song)));
                    }
                }
                return true;
            }
        });
        popup.show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onSongChange(ItemSong itemSong) {
        changeText(itemSong, "home");
        Constant.context = getActivity();
        changeImageAnimation(PlayerService.getInstance().getIsPlayling());
//        GlobalBus.getBus().removeStickyEvent(itemSong);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBufferChange(MessageEvent messageEvent) {

        if (messageEvent.message.equals("buffer")) {
            isBuffering(messageEvent.flag);
        } else {
            changePlayPauseIcon(messageEvent.flag);
        }
    }

    @Override
    public void onDestroy() {
        //Constant.isAppOpen = false;
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onViewPagerChanged(ItemMyPlayList itemMyPlayList) {
        adapter.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemMyPlayList);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }



    @Override
    public void onPause() {
       // seekHandler.removeCallbacks(run);
Log.e("MAIN", " ======================================PAUSEE========================================");
        super.onPause();
    }

    private void showHideAd(Boolean isAdShow) {
        if (isAdShow) {
            ll_adView.startAnimation(anim_slideup);
            rl_min_header.startAnimation(anim_slidedown_music);
        } else {
            ll_adView.startAnimation(anim_slidedown);
            rl_min_header.startAnimation(anim_slideup_music);
        }
    }

    private void startAdTimeCount() {
     //   adHandler.removeCallbacks(runnableAd);
       // adHandler.postDelayed(runnableAd, Constant.bannerAdShowTime);
    }

    public void setUpBannerAdonMusic() {

        setUpAnim();
    }

    private void setUpAnim() {
        anim_slideup = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        anim_slidedown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        anim_slideup.setInterpolator(new OvershootInterpolator(0.5f));
        anim_slidedown.setInterpolator(new OvershootInterpolator(0.5f));

        anim_slideup_music = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        anim_slidedown_music = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        anim_slideup_music.setInterpolator(new OvershootInterpolator(0.5f));
        anim_slidedown_music.setInterpolator(new OvershootInterpolator(0.5f));

        anim_slidedown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_adView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_slideup.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll_adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim_slidedown_music.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_min_header.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_slideup_music.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rl_min_header.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    
}
