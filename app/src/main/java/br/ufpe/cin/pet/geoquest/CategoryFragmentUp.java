package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rbb3 on 29/08/16.
 */
public class CategoryFragmentUp extends Fragment {

    private CategoryAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            new AsyncTask<Void, Void, Void>() {
                List<Category> items = new ArrayList<Category>();

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        items = run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    adapter = new CategoryAdapter(getActivity().getApplicationContext(), items, getActivity());

                    ListView listView = (ListView) rootView.findViewById(R.id.listViewCategories);
                    listView.setAdapter(adapter);
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private List<Category> run() throws Exception {
        Request request = new Request.Builder()
                .url("http://www.mocky.io/v2/5810e66e3a0000780760985d")
                .header("TOKEN", Config.key)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        List<Category> items = new ArrayList<Category>();
        try {
            JSONArray obj = new JSONArray(response.body().string());

            int tam = obj.length();
            for (int i = 0; i < tam; i++) {
                JSONObject object = obj.getJSONObject(i);

                String nome = object.getString("nome");
                String descricao = object.getString("descricao");
                int total = object.getInt("total");
                int done = object.getInt("done");
                int id = object.getInt("id");

                Category cat = new Category(nome, descricao, done, total, id);
                items.add(cat);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JSONError", "Erro na formatação do response");
        }
        return items;
    }


}
