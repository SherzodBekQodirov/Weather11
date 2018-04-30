package ru.startandroid.weather;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
public class ImageByter {
    public static byte[] imageByter(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = (InputStream) url1.getContent();
        } catch (IOException e) {
            // Кирадиган стрим яралмаса дальше юришдан фойда йук.
            Log.e("ImageByter", "Error: ", e);
        }
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }
    public static Bitmap creatBitmap (String url){
        if (TextUtils.isEmpty(url)) {
            Log.e("ImageByter", "Empty url: ");
            return null;
        }
        byte[] bytes = imageByter(url);
        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bm;
    }
}