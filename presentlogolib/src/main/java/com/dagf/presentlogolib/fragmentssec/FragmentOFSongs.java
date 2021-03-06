package com.dagf.presentlogolib.fragmentssec;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dagf.presentlogolib.R;
import com.dagf.presentlogolib.models.ItemAlbums;
import com.dagf.presentlogolib.utils.ClickListenerPlayList;
import com.dagf.presentlogolib.utils.Constant;
import com.dagf.presentlogolib.utils.GlobalBus;
import com.dagf.presentlogolib.utils.InterAdListener;
import com.dagf.presentlogolib.utils.Methods;
import com.dagf.presentlogolib.utils.adapters.AdapterOFSongList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;

public class FragmentOFSongs extends Fragment {

    Methods methods;
    RecyclerView rv;
    AdapterOFSongList adapter;
    CircularProgressBar progressBar;

    FrameLayout frameLayout;
    String errr_msg = "";
    SearchView searchView;

    public static FragmentOFSongs newInstance(int sectionNumber) {
        FragmentOFSongs fragment = new FragmentOFSongs();
    //    Log.e(TAG, "newInstance: INSTANCIANDO FRAGMENT OF SONGS");
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_song_by_cat, container, false);

    //    Log.e(TAG, "onCreateView: ONCREATE VIEW SONGS");

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Constant.isOnline = false;
                Constant.arrayList_play.clear();
                Constant.arrayList_play.addAll(Constant.arrayListOfflineSongs);
                Constant.playPos = position;

                Intent intent = new Intent(getActivity(), PlayerService.class);
                intent.setAction(PlayerService.ACTION_PLAY);
                getActivity().startService(intent);
            }
        });
        errr_msg = getString(R.string.err_no_songs_found);

        progressBar = rootView.findViewById(R.id.pb_song_by_cat);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        AdView adView = rootView.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());


        rv = rootView.findViewById(R.id.rv_song_by_cat);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        Log.e(TAG, "onCreateView: SI SI SI ENTRO EN OF SONGS");

        new LoadOfflineSongs().execute();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapter != null) {
                if (!searchView.isIconified()) {
                    adapter.getFilter().filter(s);
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
    };

    class LoadOfflineSongs extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            frameLayout.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (Constant.arrayListOfflineSongs.size() == 0) {
                methods.getListOfflineSongs();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (getActivity() != null) {
                setAdapter();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void setAdapter() {
        adapter = new AdapterOFSongList(getActivity(), Constant.arrayListOfflineSongs, new ClickListenerPlayList() {
            @Override
            public void onClick(int position) {
                methods.showInterAd(position, "");
            }

            @Override
            public void onItemZero() {

            }
        }, "");


        rv.setAdapter(adapter);
        setEmpty();
    }

    public void setEmpty() {
        if (Constant.arrayListOfflineSongs.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = inflater.inflate(R.layout.layout_err_nodata, null);

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);
            myView.findViewById(R.id.btn_empty_try).setVisibility(View.GONE);


            frameLayout.addView(myView);
        }

        Log.e(TAG, "setAdapter: "+Constant.arrayListOfflineSongs.size() + " es visible "+(rv.getVisibility() == View.VISIBLE));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEquilizerChange(ItemAlbums itemAlbums) {
        adapter.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemAlbums);
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
}