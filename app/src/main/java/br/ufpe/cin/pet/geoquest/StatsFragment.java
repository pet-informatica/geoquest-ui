package br.ufpe.cin.pet.geoquest;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
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

    private AdapterStats adapter;

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

        //String url = "http://www.mocky.io/v2/5878fc682600008d081c35d6";
        String url = getResources().getString(R.string.base_url)+"users/stats/";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Config.getKey())
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            stats.rankingPos = jsonResponse.getInt(Stats.rankingPosKey);
            stats.favoriteArea = jsonResponse.getString(Stats.favoriteAreaKey);
            stats.percentageCompleted = jsonResponse.getDouble(Stats.percentageCompletedKey);

            JSONArray categoriesResponse = jsonResponse.getJSONArray(Stats.areasKey);
            double categorie_percentage;
            double l1, l2, l3;
            stats.areas.clear();
            for(String cat : Stats.CATEGORIES) {
                categorie_percentage = 0;
                l1 = 0;
                l2 = 0;
                l3 = 0;
                for(int i = 0; i < categoriesResponse.length(); i++) {
                    JSONObject area = categoriesResponse.getJSONObject(i);
                    if (area.getString(Stats.areaNameKey).toLowerCase().equals(cat.toLowerCase())) {
                        categorie_percentage = area.getDouble(Stats.areaPercKey);
                        l1 = area.getDouble(Stats.level1Key);
                        l2 = area.getDouble(Stats.level2Key);
                        l3 = area.getDouble(Stats.level3Key);
                        break;
                    }
                }
                stats.areas.add(new Stats.PairAreaPercentage(cat, categorie_percentage, l1, l2, l3));
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

        adapter = new AdapterStats(getActivity().getApplicationContext(), stats.areas);

        stats.sortAreas();

        final ListView listView = (ListView) rootView.findViewById(R.id.listViewStats);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_level_stats);

                Stats.PairAreaPercentage area = (Stats.PairAreaPercentage) listView.getItemAtPosition(pos);

                TextView l1_name = (TextView) dialog.findViewById(R.id.level1_name);
                TextView l1_perc = (TextView) dialog.findViewById(R.id.level1_perc);
                ImageView l1_img = (ImageView) dialog.findViewById(R.id.level1_img);

                TextView l2_name = (TextView) dialog.findViewById(R.id.level2_name);
                TextView l2_perc = (TextView) dialog.findViewById(R.id.level2_perc);
                ImageView l2_img = (ImageView) dialog.findViewById(R.id.level2_img);

                TextView l3_name = (TextView) dialog.findViewById(R.id.level3_name);
                TextView l3_perc = (TextView) dialog.findViewById(R.id.level3_perc);
                ImageView l3_img = (ImageView) dialog.findViewById(R.id.level3_img);

                l1_name.setText("Nível 1");
                l2_name.setText("Nível 2");
                l3_name.setText("Nível 3");

                l1_perc.setText((int)area.l1+"%");
                l2_perc.setText((int)area.l2+"%");
                l3_perc.setText((int)area.l3+"%");

                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);

                l1_img.setColorFilter(filter);
                l2_img.setColorFilter(filter);
                l3_img.setColorFilter(filter);

                l1_img.setImageResource(R.drawable.lock_full);
                l1_img.setAlpha(0.25f);
                l2_img.setImageResource(R.drawable.lock_full);
                if (area.l1 >= 75) l2_img.setAlpha(0.25f);
                l3_img.setImageResource(R.drawable.lock_full);
                if (area.l2 >= 75) l3_img.setAlpha(0.25f);

                dialog.show();
            }
        });
    }

}
