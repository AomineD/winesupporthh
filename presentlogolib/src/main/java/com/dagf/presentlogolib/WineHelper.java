package com.dagf.presentlogolib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.dagf.presentlogolib.fragmentssec.PlayerService;
import com.dagf.presentlogolib.present.PresentActivity;
import com.dagf.presentlogolib.utils.MediafireParser;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class WineHelper {

    public static final  String urltest = "http://www.mediafire.com/file/909q425vqvf888c/Game2.10%255BFabianLeyva%255D";



    public static final int keypermission = 394;

    public static void setPlayerServiceS(Activity bs, int mipmapicon){

        PlayerService.ConfigureT(bs, mipmapicon);
    }

    public static boolean checkPermissionWrite(Activity as){
        return ActivityCompat.checkSelfPermission(as, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionWrite(Activity a){
        if(ActivityCompat.checkSelfPermission(a, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, keypermission);
        }
    }

    /**
     typeVideo:
     0 => Default Neon
     1 => luminoso
     2 => Explosivo
     3 => Luces veloces
     **/
    public static void openPresentation(Context cc, int typeVideo){
        if(typeVideo >= 0 && typeVideo <= 3)
        PresentActivity.what_video = typeVideo;
        cc.startActivity(new Intent(cc, PresentActivity.class));
    }


    public static boolean shouldPresent(Activity ac){
        return !PresentActivity.isAlreadyViewed(ac);
    }

    public static void openPresentation(Context cc){
        cc.startActivity(new Intent(cc, PresentActivity.class));
    }


    public static void loadImageLogo(ImageView img){
        Picasso.get().load(Uri.parse("http://wineberryhalley.com/about_us/id.png")).into(img);

    }

    public static void loadCompanyCompleteLogo(ImageView img){
        Picasso.get().load(Uri.parse("http://wineberryhalley.com/about_us/wbh_ic.png")).into(img);
    }

    public static void getMediafireFile(Context m, String ur,MediafireParser.MediafireResponse listener){
        new MediafireParser(m, ur, listener);
    }


    /** ========================= AQUI EMPIEZA LO CHIDO ================================= **/




public static File saln (String namepath) throws IOException {

    if(!namepath.endsWith(".aln/file")){
        return null;
    }


    File f = null;

    try {
            InputStream in = new URL(namepath).openStream();

             f = File.createTempFile("_AUDIO_", ".mp4");
            FileOutputStream out = new FileOutputStream(f);
            IOUtils.copy(in, out);
            // play f

    } catch (IOException e) {
        Log.e("MAIN", "saln: "+e.getMessage());
    }


   // Log.e("MAIN", "saln: FILE PATH => "+ff.getAbsolutePath());

    //ZipArchive.unzip(ff.getAbsolutePath(),ff.getParent(),"00dwinpass");


    return f;

}


    private static void getFileToPlay(String pathFile) throws IOException {
        ZipFile zipFile = new ZipFile("C:/test.zip");

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
        }
    }



    public static boolean existFolder(String app, Activity thisActivity){

        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        494);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+app);


       // Log.e("MAIN", "existFolder: "+f.getAbsolutePath());

        if(!f.exists() && !f.isDirectory())
        {
            // create empty directory
            if (f.mkdirs())
            {
                Log.i("MAIN","App dir created");
            }
            else
            {
                Log.w("MAIN","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("MAIN","App dir already exists");
        }

    return f.exists();
        }


        public static File getPathOf(String app){
    return new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+app);
        }



    private static File changeExtension(File f, String newExtension) throws IOException {
        int i = f.getName().lastIndexOf('.');
        String name = f.getName().substring(0,i);

        File fta = new File(f.getParent() + "/" + name + newExtension);

        InputStream in = new FileInputStream(f);
        try {
            OutputStream out = new FileOutputStream(fta);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }


        return fta;
    }


    public static List<File> getAllFiles(String app) throws IOException {
        File dir = getPathOf(app);
        String[] extensions = new String[]{"aln"};
        Log.e("MAIN", "Getting all aln files in " + dir.getCanonicalPath()
                + " including those in subdirectories");
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        for (File file : files) {
            Log.e("MAIN","file: " + file.getCanonicalPath());
        }

        return files;
    }
}
