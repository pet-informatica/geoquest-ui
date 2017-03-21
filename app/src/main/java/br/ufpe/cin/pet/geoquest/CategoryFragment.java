package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
public class CategoryFragment extends Fragment {

    private CategoryAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();
    private ProgressDialog progressDialog;
    private TextView title;
    private ImageView app_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        app_back = (ImageView) rootView.findViewById(R.id.app_back);
        title = (TextView) rootView.findViewById(R.id.tv_title);

        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "cooper-black.ttf"));

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

                    GridView gridView = (GridView) rootView.findViewById(R.id.gridViewCategories);
                    gridView.setAdapter(adapter);
                    progressDialog.hide();
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        setOnClickListeners(getView());
        return rootView;
    }

    private void setOnClickListeners(View rootView) {
        app_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new MainFragment());
                ft.addToBackStack("main_fragment");
                ft.commit();
            }
        });

    }

        private List<Category> run() throws Exception {

        String url = "http://www.mocky.io/v2/58b8edd70f00003604f09b6b";
        //String url = getResources().getString(R.string.base_url)+"questions/categories";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Config.getKey())
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        List<Category> items = new ArrayList<Category>();
        try {
            JSONArray obj = new JSONArray(response.body().string());

            int tam = obj.length();
            for (int i = 0; i < tam; i++) {
                JSONObject object = obj.getJSONObject(i);

                String nome = object.getString("name");
                String descricao = object.getString("description");
                int total = object.getInt("total");
                int done = object.getInt("done");
                int min_level = object.getInt("min");
                int max_level = object.getInt("max");

                Category cat = new Category(nome, descricao, done, total, min_level, max_level);
                items.add(cat);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JSONError", "Erro na formatação do response");
        }
        return items;
    }


}
