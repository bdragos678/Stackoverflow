package com.example.bdrag.stackoverflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageHelper {

    public static Bitmap getImageBitmap(String imageUrl){

        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap image = null;

        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection)url.openConnection();
            is = connection.getInputStream();
            image = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(connection != null){
                connection.disconnect();
            }

            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return image;
    }
}
