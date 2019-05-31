package com.dagf.presentlogolib.utils;

import android.content.Context;

import com.dagf.presentlogolib.models.ItemAlbums;
import com.dagf.presentlogolib.models.ItemArtist;
import com.dagf.presentlogolib.models.ItemSong;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String SERVER_URL = "";
    public static final String URL_SONG_2 = "&device_id=";
 //   public static ItemAbout itemAbout;

    public static final String URL_SONG_1 = SERVER_URL + "api.php?mp3_id=";

    public static int playPos = 0;
    public static ArrayList<ItemSong> arrayList_play = new ArrayList<>();
    public static ArrayList<ItemSong> arrayListOfflineSongs = new ArrayList<>();
    public static ArrayList<ItemAlbums> arrayListOfflineAlbums = new ArrayList<>();
    public static ArrayList<ItemArtist> arrayListOfflineArtist = new ArrayList<>();

    public static Boolean isRepeat = false, isSuffle = false, isPlaying = false,
            isPlayed = false, isFromNoti = false, isFromPush = false, isAppOpen = false, isOnline = true, isBannerAd = true,
            isInterAd = true, isSongDownload = false;
    public static Context context;
    public static int volume = 25;
    public static String pushSID = "0", pushCID = "0", pushCName = "", pushArtID = "0", pushArtNAME = "", pushAlbID = "0", pushAlbNAME = "", search_item = "";

    public static int rotateSpeed = 25000; //in milli seconds

    public static int bannerAdShowTime = 7000; //in milli seconds

    public static int adCount = 0;
    public static int adDisplay = 5;

    public static String ad_publisher_id = "";
    public static String ad_banner_id = "";
    public static String ad_inter_id = "";
}