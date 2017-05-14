package pl.lodz.p.eletel.coffemenu;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by student_pl on 14.05.17.
 */

public class MyImageRepository {


    WebDownloader webDownloader;
    File directory;
    Activity myActivty;

    public MyImageRepository(Activity activity){

        myActivty = activity;
        directory = myActivty.getDir("obrazki", Context.MODE_PRIVATE);
        //new File(myActivty.getFilesDir() + File.pathSeparator + "obrazki");
        directory.mkdir();
        webDownloader = new WebDownloader();
    }

    public byte[] getImage(String uri){

        byte[] ret = null;

        File image = null;
        try {
            image = new File(directory, SHA1(uri));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(image.exists()){
            try {
                Log.i("DOWNLOADER", "returning image from cache");
                FileInputStream fis = new FileInputStream(image);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int contentLength = (int)image.length();
                ret = new byte[contentLength];

                int count = 0;
                byte[] buffer = new byte[2048];
                int progress = 0;
                while ((count = bis.read(buffer, 0, 2048)) != -1) {
                    System.arraycopy(buffer, 0, ret, progress, count);
                    progress += count;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Log.i("DOWNLOADER", "downloading image from web");
            ret = webDownloader.downloadFileSync(uri);
            saveFile(ret, uri);
        }

        return ret;

    }

    private void saveFile(byte[] ret, String uri) {


        File image = null;
        try {
            image = new File(directory, SHA1(uri));

//            image.createNewFile();

            FileOutputStream fos = new FileOutputStream(image);

            fos.write(ret);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
