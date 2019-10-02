package com.dagf.presentlogolib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dagf.presentlogolib.WineHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Updater {

    public static boolean ThereUpdate;

    public interface OnSkipListener{
        void onSkip();
    }

    public static UpdateDialog updateDialog;

    public static void check(final Activity mc, String urlBase, final OnSkipListener listener) throws PackageManager.NameNotFoundException {
        RequestQueue queue = Volley.newRequestQueue(mc);

        String pack = mc.getPackageName();
    //    Log.e("MAIN", "ches: "+urlBase );
        urlBase = urlBase.replace("api", "");
     //   Log.e("MAIN", "check: "+urlBase );
       final String basen = urlBase;

        urlBase = urlBase + "updater/get_update?package=" + pack + "&ver="+mc.getPackageManager().getPackageInfo(pack, 0).versionName;
//Log.e("MAIN", "url "+urlBase);
        StringRequest request = new StringRequest(Request.Method.GET, urlBase, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                   // Log.e("MAIN", "onResponse: "+response );
                    JSONObject object = new JSONObject(response);

                    if(object.has("1")){
                        if(object.getString("1").equals("update")){


if(WineHelper.checkPermissionWrite(mc)) {
    //    Log.e("MAIN", "onResponse: "+basen+object.getString("url") );
    updateDialog = new UpdateDialog(mc, basen + object.getString("url"));

    updateDialog.setSkipListener(listener);

    updateDialog.show();

    ThereUpdate = true;
}else
    WineHelper.requestPermissionWrite(mc);
                        }else
                            {
                            ThereUpdate = false;
                        }
                    }else{
                        ThereUpdate = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MAIN", "UPDATER "+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           ThereUpdate = false;
            }
        });


        queue.add(request);
    }


    public static void NowOpenAPK(){
        if(updateDialog != null){
            updateDialog.openApk();
        }
    }

    public static void TryDownloadAgain() {

        if (updateDialog != null) {
     updateDialog.downloadAgain();
        }
    }
}
