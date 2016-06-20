package br.ufpe.cin.pet.geoquest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import br.ufpe.cin.pet.geoquest.Utils.CropImage;
import br.ufpe.cin.pet.geoquest.classes.Badge;
import br.ufpe.cin.pet.geoquest.classes.Stats;

public class MyBadgesFragment extends Fragment {

	private ProgressDialog progressDialog;
	private String picture;
	private String username;
	private String area;
	private double percentage;
	private String[] badge_tipos = {"caseiro", "flash", "dev", "badge3", "badge4",
									"badge6", "badge7", "lerdo", "badge9"};
	private Map<String, Number> has_badge;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		final View rootView = inflater.inflate(R.layout.fragment_badges, container, false);

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		getBadges(rootView);

		/*
		String name = "Maria Gabriela";
		getActivity().getActionBar().setTitle(name);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
        Vector<String> badges = new Vector<String>();
        badges.add("caseiro");
        badges.add("flash");
        badges.add("lerdo");
        badges.add("dev");
             
        
        for (int i = 0; i < badges.size(); ++i){
        	final ImageView img = (ImageView) rootView.findViewById(getIdBadge(badges.get(i)));
        	img.setAlpha(1f);
        	final String desc = getBadgeDesc(badges.get(i));
        	
        	img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            		dialog.setMessage(desc);
            		dialog.show();
                }
            });  
        }
        */

		return rootView;
    }

	private void getBadges(final View rootView) {

		String url = "http://www.mocky.io/v2/57662d790f0000be0ae36fb0";

		Log.i("Badges", "Enviando requisição das badges");

		StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("Response", response);
				try {
					JSONObject jsonResponse = new JSONObject(response);
					picture = jsonResponse.getString("picture");
					username = jsonResponse.getString("name");
					area = jsonResponse.getString("area");
					percentage = jsonResponse.getDouble("percentage");
					JSONObject badgeresponse = jsonResponse.getJSONObject("badges");

					for(String categorie : badge_tipos) {
						if (badgeresponse.has(categorie)) has_badge.put(categorie, 1);
						else has_badge.put(categorie, 0);
					}

					populateView(rootView);

					progressDialog.hide();
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

	class ViewHolder {
		protected TextView title;
		protected TextView descript;
		protected ImageView token;
	}

	private void populateView(final View rootView) {

		getActivity().getActionBar().setTitle(username);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		ImageView userImg = (ImageView) rootView.findViewById(R.id.userBadgesImgBorda);
		TextView description = (TextView) rootView.findViewById(R.id.descUserBadges);
		ImageView star1 = (ImageView) rootView.findViewById(R.id.star1Badges);
		ImageView star2 = (ImageView) rootView.findViewById(R.id.star2Badges);
		ImageView star3 = (ImageView) rootView.findViewById(R.id.star3Badges);
		ImageView star4 = (ImageView) rootView.findViewById(R.id.star4Badges);
		ImageView star5 = (ImageView) rootView.findViewById(R.id.star5Badges);

		try {
			InputStream in = new URL(picture).openStream();
			Bitmap bm = BitmapFactory.decodeStream(in);
			CropImage cim = new CropImage(200, bm);
			userImg.setImageBitmap(cim.getCroppedBitmap());
		} catch (MalformedURLException malex) {
			Log.i("Bitmap", "Erro na formatação do bitmap");
			malex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		description.setText("Especialista em "+area);
		if (percentage >= 20.0) star1.setImageResource(R.drawable.star_full);
		else star1.setImageResource(R.drawable.star);
		if (percentage >= 40.0) star2.setImageResource(R.drawable.star_full);
		else star2.setImageResource(R.drawable.star);
		if (percentage >= 60.0) star3.setImageResource(R.drawable.star_full);
		else star3.setImageResource(R.drawable.star);
		if (percentage >= 80.0) star4.setImageResource(R.drawable.star_full);
		else star4.setImageResource(R.drawable.star);
		if (percentage == 100.0) star5.setImageResource(R.drawable.star_full);
		else star5.setImageResource(R.drawable.star);
	}

	/*
	
	public int getIdBadge(String x){
		if (x.equals("caseiro")) return R.id.badge1;
		if (x.equals("dev")) return R.id.badge2;
		if (x.equals("badge3")) return R.id.badge3;
		if (x.equals("badge4")) return R.id.badge4;
		if (x.equals("flash")) return R.id.badge5;
		if (x.equals("badge6")) return R.id.badge6;
		if (x.equals("badge7")) return R.id.badge7;
		if (x.equals("lerdo")) return R.id.badge8;
		if (x.equals("badge9")) return R.id.badge9;
		return 0;
	}
	
	public static int getIdBadgeImg(String badge){
		if (badge.equals("caseiro")) return R.drawable.badge_caseiro;
		if (badge.equals("flash")) return R.drawable.badge_flash;
		if (badge.equals("lerdo")) return R.drawable.badge_lerdo;
		if (badge.equals("dev")) return R.drawable.badge_dev;
		return 0;
	}
	
	public static String getNomeBadge(String badge){
		if (badge.equals("caseiro")) return "\"Caseiro\"";
		if (badge.equals("flash")) return "\"The Flash\"";
		if (badge.equals("lerdo")) return "\"Lerdo\"";
		if (badge.equals("dev")) return "\"Dev\"";
		return "";
	}
	
	
	public String getBadgeDesc(String badge){
		if (badge.equals("caseiro")) return "Caseiro: participou de uma sa�da onde n�o houve sugest�es.";
		if (badge.equals("flash")) return "Flash: clicou rapidamente.";
		if (badge.equals("lerdo")) return "Lerdo: clicou lentamente.";
		if (badge.equals("dev")) return "Desenvolvedor: participou do desenvolvimento do Geoquest.";	
		return "";
	}
	*/
	
}