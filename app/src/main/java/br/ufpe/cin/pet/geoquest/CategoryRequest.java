package br.ufpe.cin.pet.geoquest;

/**
 * Created by rbb3 on 14/09/16.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;
import br.ufpe.cin.pet.geoquest.classes.Raking;

public class CategoryRequest extends AsyncTask<String, Void, List<Category> > {

    private final String url;

    private HttpResponse response;

    public CategoryRequest(String url) {
        this.url = url;
    }

    @Override
    protected List<Category> doInBackground(String... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Authorization", "token " + Config.key);
        Log.i("TOKEN", Config.key);

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

        List<Category> items = new ArrayList<Category>();

        try {
            JSONArray obj = new JSONArray(sresponse);

            int tam = obj.length();
            for(int i = 0; i < tam; i++) {
                JSONObject object = obj.getJSONObject(i);

                String nome = object.getString("nome");
                String descricao = object.getString("descricao");
                int total = object.getInt("total");
                int done = object.getInt("done");

                Category cat = new Category(nome, descricao, done, total);
                items.add(cat);
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
