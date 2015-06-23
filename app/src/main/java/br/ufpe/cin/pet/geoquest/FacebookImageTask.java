package br.ufpe.cin.pet.geoquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Tomer Simis on 19/04/2015.
 */
public class FacebookImageTask extends AsyncTask<String, Void, Void> {

    private ImageView view;

    private Bitmap image;

    private MainActivity activity;

    public FacebookImageTask(ImageView view, MainActivity activity){
        this.view = view; this.activity = activity;
    }
    @Override
    protected Void doInBackground(String... urls) {
        String urldisplay = urls[0];
        Log.i("FacebookImageTask", urldisplay);
        try {
            URL image_value = new URL(urldisplay);
            image = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
            Log.i("FacebookImageTask", image.toString());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        view.setImageBitmap(image);
        view.invalidate();
        activity.setUserImage(image);
    }
}
