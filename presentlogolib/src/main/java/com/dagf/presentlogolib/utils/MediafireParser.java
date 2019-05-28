package com.dagf.presentlogolib.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MediafireParser {
    private MediafireResponse ls;
    private Context mm;


    public static String generateMediaf(String fhash){
        return fhash.replace("alien-media", "http://www.mediafire.com/file")+".mkv/file";
    }

    public MediafireParser(Context m, String urr, final MediafireResponse listener){

        this.ls = listener;
        this.mm = m;

        RequestQueue queue = Volley.newRequestQueue(m);


  //      Log.e("MAIN", "MediafireParser: "+urr);
        StringRequest request = new StringRequest(Request.Method.GET, urr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
HandleResultPage(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
ls.Failed(error.getMessage());
            }
        });

        queue.add(request);

    }


    // ===================================== AHORA SI VIENE LO CHIDO ================================================ //

    private int limit = 120;
    private int since = 9;

    private void HandleResultPage(String r){

      //   Log.e("MAIN", "HandleResultPage: "+r);

        String[] variosone = r.split("href=\"");
String scr = "";

        for(int i=0; i < variosone.length; i++){
            if(i >= since) {
                for(int o =0; o < variosone[i].length(); o++) {
                    scr = scr + variosone[i].charAt(o);
                    if (o > limit) {
                        break;
                    }
                }
            }
        }

      //Log.e("MAIN", "HandleResultPage: "+scr);
       // Log.e("MAIN", "HandleResultPage: "+r);
      // generateNoteOnSD(mm, "test", r);


        String[] variostwo = scr.split("\"input\"");

        String another = "";

        for(int i=0; i < variostwo.length; i++){

                for(int o =0; o < variostwo[i].length(); o++) {
                    if (variostwo[i].charAt(o) == '\"') {
                       // Toast.makeText(mm, "CORTE", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    another = another + variostwo[i].charAt(o);

                }

        }

        another = another.replace("#\"", "");
        if(another.contains("#")) {
            another = another.replace("#", "");
        }

        if(another.contains("\n")){
            another = another.replace("\n", "");
        }

        if(another.contains(" ")){
            another = another.replace(" ", "");
        }

      //  Log.e("MAIN", "HandleResultPage: ANOTHER = "+another);

        ls.Loaded(another);

    }

    // =============================================================================================================== //

    public interface MediafireResponse{
        void Loaded(String url_complete);

        void Failed(String errno);
    }
    

}
