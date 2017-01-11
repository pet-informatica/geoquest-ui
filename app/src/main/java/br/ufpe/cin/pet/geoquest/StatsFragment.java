package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import br.ufpe.cin.pet.geoquest.classes.Stats;

public class StatsFragment extends Fragment{

    public static Stats stats = new Stats();

    private ProgressDialog progressDialog;

    private final OkHttpClient client = new OkHttpClient();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        getActivity().getActionBar().setTitle("Estatísticas");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        getStatistics(rootView);

        return rootView;
    }


    private void getStatistics(final View rootView){

        try {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    populateViewWithStats(rootView);
                    progressDialog.hide();
                }
            }.execute();

        } catch (Exception e) {
            Log.i("ERROR", "Não foi possível obter as suas estatísticas");
            e.printStackTrace();
        }
    }

    private void run() throws Exception {

        String url = "http://www.mocky.io/v2/5876809b100000f70e8b5cb7";
        //String backUrl = getResources().getString(R.string.base_url)+"users/stats/";

        Request request = new Request.Builder()
                .url(url)
                .header("TOKEN", Config.key)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            stats.rankingPos = jsonResponse.getInt(Stats.rankingPosKey);
            stats.favoriteArea = jsonResponse.getString(Stats.favoriteAreaKey);
            stats.percentageCompleted = jsonResponse.getDouble(Stats.percentageCompletedKey);

            JSONObject categoriesResponse = jsonResponse.getJSONObject(Stats.areasKey);
            double categorie_percentage;
            stats.areas.clear();
            for(String categorie : Stats.CATEGORIES){
                categorie_percentage = 0;
                if(categoriesResponse.has(categorie.toLowerCase())){
                    categorie_percentage = categoriesResponse.getInt(categorie.toLowerCase());
                }
                stats.areas.add(new Stats.PairAreaPercentage(categorie,categorie_percentage));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JSONError", "Erro na formatação do response");
        }
    }


    private void populateViewWithStats(final View rootView) {

        TextView rankingPosition = (TextView) rootView.findViewById(R.id.rankingPosition);
        TextView favoriteArea = (TextView) rootView.findViewById(R.id.favoriteArea);
        TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);

        ProgressBar progressBarPercentageCompleted = (ProgressBar) rootView.findViewById(R.id.progressBarPercentageCompleted);

        rankingPosition.setText(stats.rankingPos + "° posição");
        favoriteArea.setText(stats.favoriteArea);
        progressBarText.setText(stats.percentageCompleted + "% ");
        progressBarPercentageCompleted.setProgress((int)stats.percentageCompleted);

        AdapterStats adapter = new AdapterStats(getActivity().getApplicationContext(), stats.areas);

        stats.sortAreas();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewStats);
        listView.setAdapter(adapter);
    }

}
