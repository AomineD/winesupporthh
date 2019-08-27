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

import org.json.JSONException;
import org.json.JSONObject;

public class Updater {

    public static boolean ThereUpdate;

    public static UpdateDialog updateDialog;

    public static void check(final Activity mc, String urlBase) throws PackageManager.NameNotFoundException {
        RequestQueue queue = Volley.newRequestQueue(mc);

        String pack = "com.reva.app.pelispluschromecast";//mc.getPackageName();
        urlBase = urlBase.replace("/api", "");
      //  Log.e("MAIN", "check: "+urlBase );
       final String basen = urlBase;

        urlBase = urlBase + "/updater/get_update?package=" + pack + "&ver="+"1.0.20";//mc.getPackageManager().getPackageInfo(pack, 0).versionName;
//Log.e("MAIN", "url "+urlBase);
        StringRequest request = new StringRequest(Request.Method.GET, urlBase, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                   // Log.e("MAIN", "onResponse: "+response );
                    JSONObject object = new JSONObject(response);

                    if(object.has("1")){
                        if(object.getString("1").equals("update")){

                            updateDialog = new UpdateDialog(mc, basen+object.getString("url"));

                            updateDialog.show();

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
}
