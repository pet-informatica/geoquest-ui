package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
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
import com.facebook.AccessToken;

import br.ufpe.cin.pet.geoquest.classes.Stats;

public class StatsFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        getActivity().getActionBar().setTitle("Geoquest");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//{int posicao, string categoria, int porcentagem, lista<string, int> categorias_porcentagem}
        getStatistics();
        Stats.PairAreaPercentage pairGeo = new Stats.PairAreaPercentage("Geologia", 67);
        Stats.PairAreaPercentage pairHidro = new Stats.PairAreaPercentage("Hidrografia", 35);
        Stats.PairAreaPercentage pairPol = new Stats.PairAreaPercentage("Política", 88);

        Vector<Stats.PairAreaPercentage> pairs = new Vector<Stats.PairAreaPercentage>();
        pairs.add(pairGeo);
        pairs.add(pairHidro);
        pairs.add(pairPol);

        Stats stats = new Stats(2, "Geologia", 56, pairs);

        TextView rankingPosition = (TextView) rootView.findViewById(R.id.rankingPosition);
        TextView favoriteArea = (TextView) rootView.findViewById(R.id.favoriteArea);
        TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
        ProgressBar progressBarPercentageCompleted = (ProgressBar) rootView.findViewById(R.id.progressBarPercentageCompleted);

        rankingPosition.setText(stats.rankingPos + "° posição");
        favoriteArea.setText(stats.favoriteArea);
        progressBarText.setText(stats.percentageCompleted + "% ");
        progressBarPercentageCompleted.setProgress(stats.percentageCompleted);

        AdapterStats adapter = new AdapterStats(getActivity().getApplicationContext(), stats.areas);

        ListView listView = (ListView) rootView.findViewById(R.id.listViewStats);
        listView.setAdapter(adapter);

        return rootView;
    }


    private void getStatistics(){

        String url = getResources().getString(R.string.base_url) + "statistics/";

        Log.i("Statistics", "Enviando requisição das statistics");

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
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
                // botar a token do servidor auqi
                return params;
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestSingleton.getInstance(this.getActivity()).addToRequestQueue(sr);
    }


}
