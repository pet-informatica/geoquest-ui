package br.ufpe.cin.pet.geoquest.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rbb3 on 11/01/17.
 */
public class BitmapFromURL {

    private String src;

    public BitmapFromURL(String str) {
        this.src = str;
    }

    public Bitmap getBitmapFromURL() {

        try {
            Log.e("src",this.src);
            URL url = new URL(this.src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
