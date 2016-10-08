package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.cin.pet.geoquest.classes.Stats;

/**
 * Created by rbb3 on 03/10/16.
 */
public class TransitionFragment extends Fragment {

    private double v1;
    private double v2;
    private double v3;
    private String cat;
    private String lev;

    static class ViewHolder {
        protected TextView txt1;
        protected TextView txt2;
        protected TextView txt3;
        protected TextView titulo;
        protected TextView nivel;
        protected ImageButton gofront;
        protected ImageButton goback;
        protected ImageView feedback;
        protected ProgressBar pb1;
        protected ProgressBar pb2;
        protected ProgressBar pb3;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transition, container, false);

        cat = "Hidrografia";
        lev = "1";

        getData(view);

        return view;
    }

    private void getData(final View rootView){

        String url = "http://www.mocky.io/v2/57f9297e0f000054155a7ce5";

        Log.i("Transition", "Enviando requisição da transicao");

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    v1 = jsonResponse.getDouble("valor1");
                    v2 = jsonResponse.getDouble("valor2");
                    v3 = jsonResponse.getDouble("valor3");
                    //lidar com badges

                    JSONArray categoriesResponse = jsonResponse.getJSONArray("badges");

                    populate(rootView);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ParserError", "Can't parse response into json object");
                }
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

    void populate(View view) {

        ViewHolder vh = new ViewHolder();


        vh.txt1 = (TextView) view.findViewById(R.id.textView3);
        vh.txt2 = (TextView) view.findViewById(R.id.textView4);
        vh.txt3 = (TextView) view.findViewById(R.id.textView5);
        vh.pb1 = (ProgressBar) view.findViewById(R.id.progressBar);
        vh.pb2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        vh.pb3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        vh.goback = (ImageButton) view.findViewById(R.id.returnMenoButton);
        //setar onclick
        vh.gofront = (ImageButton) view.findViewById(R.id.goFowardButton);
        //setar onclick
        vh.titulo = (TextView) view.findViewById(R.id.trasitionTitle);
        vh.nivel = (TextView) view.findViewById(R.id.transitionLevel);
        vh.feedback = (ImageView) view.findViewById(R.id.feedbackImg);

        vh.goback.setImageResource(R.drawable.voltar);
        vh.gofront.setImageResource(R.drawable.continuar);
        vh.txt1.setText(v1+"% de acertos no bloco");
        vh.txt2.setText(v2+"% da categoria concluída");
        vh.txt3.setText(v3+"% do jogo concluído");
        vh.pb1.setProgress((int)v1);
        vh.pb2.setProgress((int)v2);
        vh.pb3.setProgress((int)v3);
        vh.titulo.setText(cat); //puxar da activity
        vh.nivel.setText("Nível "+lev); //puxar da activity

        if (v1 <= 50) vh.feedback.setImageResource(R.drawable.fail);
        else if (v1 <= 80) vh.feedback.setImageResource(R.drawable.normal);
        else vh.feedback.setImageResource(R.drawable.victory);
    }
}
