package br.ufpe.cin.pet.geoquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Raking;

/**
 * Created by rbb3 on 06/06/16.
 */
public class RakingRequest extends AsyncTask<String, Void, List<Raking> > {

    private final String url;

    private HttpResponse response;

    public RakingRequest(String url) {
        this.url = url;
    }

    @Override
    protected List<Raking> doInBackground(String... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        String sresponse = "";
        this.response = null;
        try {
            setResponse(httpclient.execute(httpget));
            sresponse = EntityUtils.toString(this.response.getEntity());
            Log.i("Server response", sresponse);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Exception", "Error");
        }

        List<Raking> items = new ArrayList<Raking>();

        try {
            JSONObject obj = new JSONObject(sresponse);
            JSONArray arr = obj.getJSONArray("rank");
            Bitmap bm = null;

            InputStream in;

            int tam = arr.length();
            for(int i = 0; i < tam; i++) {
                JSONObject object = arr.getJSONObject(i);

                String nome = object.getString("name");
                String foto = object.getString("picture");

                in = new URL(foto).openStream();
                bm = BitmapFactory.decodeStream(in);
                Raking raking = new Raking(Integer.toString(i+1), nome, bm);
                items.add(raking);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JSONError", "Erro na formatação do response");
        }
        return items;
    }

    public void setResponse(HttpResponse r) {
        this.response = r;
    }
}