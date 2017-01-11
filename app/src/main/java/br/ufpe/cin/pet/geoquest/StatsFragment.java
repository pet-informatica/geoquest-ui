package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.cin.pet.geoquest.classes.Stats;

public class StatsFragment extends Fragment{

    public static Stats stats = new Stats();

    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        getActivity().getActionBar().setTitle("Geoquest");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//{int posicao, string categoria, int porcentagem, lista<string, int> categorias_porcentagem}
        getStatistics(rootView);

/*
        Stats.PairAreaPercentage pairGeo = new Stats.PairAreaPercentage("Geologia", 67);
        Stats.PairAreaPercentage pairHidro = new Stats.PairAreaPercentage("Hidrografia", 35);
        Stats.PairAreaPercentage pairPol = new Stats.PairAreaPercentage("Política", 88);

        Vector<Stats.PairAreaPercentage> pairs = new Vector<Stats.PairAreaPercentage>();
        pairs.add(pairGeo);
        pairs.add(pairHidro);
        pairs.add(pairPol);
*/

        return rootView;
    }


    private void getStatistics(final View rootView){

        //adicionar o Config.Key na url

        String url = "http://www.mocky.io/v2/587668bc100000190c8b5c8c";

        Log.i("Statistics", "Enviando requisição das statistics");

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    stats.rankingPos = jsonResponse.getInt(Stats.rankingPosKey);
                    stats.favoriteArea = jsonResponse.getString(Stats.favoriteAreaKey);
                    stats.percentageCompleted = jsonResponse.getInt(Stats.percentageCompletedKey);

                
                    JSONObject categoriesResponse = jsonResponse.getJSONObject(Stats.areasKey);
                    int categorie_percentage;
                    stats.areas.clear();
                    for(String categorie : Stats.CATEGORIES){
                        categorie_percentage = 0;
                        if(categoriesResponse.has(categorie.toLowerCase())){
                            categorie_percentage = categoriesResponse.getInt(categorie.toLowerCase());
                        }
                        stats.areas.add(new Stats.PairAreaPercentage(categorie,categorie_percentage));
                    }

                    populateViewWithStats(rootView);

                    progressDialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ParserError", "Can't parse response into json object");
                }
                //   waitProfileLoad();
                // response.



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("Error", "TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Error", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Error", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Error", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Error", "ParseError");
                }

                Log.e("Error", " Code " + error.networkResponse);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                
                return params;
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestSingleton.getInstance(this.getActivity()).addToRequestQueue(sr);
    }


    private void populateViewWithStats(final View rootView){
        // jsonResponse.get

        TextView rankingPosition = (TextView) rootView.findViewById(R.id.rankingPosition);
        TextView favoriteArea = (TextView) rootView.findViewById(R.id.favoriteArea);
        TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
        ProgressBar progressBarPercentageCompleted = (ProgressBar) rootView.findViewById(R.id.progressBarPercentageCompleted);

        rankingPosition.setText(stats.rankingPos + "° posição");
        favoriteArea.setText(stats.favoriteArea);
        progressBarText.setText(stats.percentageCompleted + "% ");
        progressBarPercentageCompleted.setProgress(stats.percentageCompleted);

        AdapterStats adapter = new AdapterStats(getActivity().getApplicationContext(), stats.areas);

        stats.sortAreas();


        ListView listView = (ListView) rootView.findViewById(R.id.listViewStats);
        listView.setAdapter(adapter);
    }

}
