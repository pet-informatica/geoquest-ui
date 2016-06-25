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
import android.widget.ListView;
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
import org.w3c.dom.Text;

import br.ufpe.cin.pet.geoquest.Utils.CropImage;
import br.ufpe.cin.pet.geoquest.classes.Badge;
import br.ufpe.cin.pet.geoquest.classes.Stats;

public class MyBadgesFragment extends Fragment {

	private ProgressDialog progressDialog;
	private String area;
	private double percentage;
	private String[] badge_tipos = {"caseiro", "flash", "dev", "badge3", "badge4",
									"badge6", "badge7", "lerdo", "badge9"};
	private List<Badge> lista;
	private AdapterBadge adapter;

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

		return rootView;
    }

	private void getBadges(final View rootView) {

		String url = "http://www.mocky.io/v2/576caef810000027005fdb69";

		Log.i("Badges", "Enviando requisição das badges");

		StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("Response", response);
				try {
					JSONObject jsonResponse = new JSONObject(response);
					area = jsonResponse.getString("area");
					percentage = jsonResponse.getDouble("percentage");
					JSONObject badgeresponse = jsonResponse.getJSONObject("badges");

					Badge badge = null;
					List<Badge> list = new ArrayList<Badge>();
					boolean found;
					for(int i = 0; i < badge_tipos.length; i++) {
						found = false;
						for(int j = 0; j < badgeresponse.length(); j++) {
							if (badgeresponse.get((j+1)+"").toString().equalsIgnoreCase(badge_tipos[i])) {
								badge = new Badge(getIdBadgeImg(badge_tipos[i]), badge_tipos[i], "Voce possui essa badge!", true);
								found = true;
								break;
							}
						}
						if (found == false) badge = new Badge(getIdBadgeImg(badge_tipos[i]), "Desconhecido", "Voce não possui essa badge!", false);
						list.add(badge);
					}
					lista = list;

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

	private void populateView(View rootView) {

		MainActivity mainActivity = (MainActivity)getActivity();

		getActivity().getActionBar().setTitle(mainActivity.getUserName());
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		ImageView userImgBorda = (ImageView) rootView.findViewById(R.id.userBadgesImgBorda);
		ImageView userImg = (ImageView) rootView.findViewById(R.id.userBadgesImg);
		TextView description = (TextView) rootView.findViewById(R.id.descUserBadges);
		ImageView star1 = (ImageView) rootView.findViewById(R.id.star1Badges);
		ImageView star2 = (ImageView) rootView.findViewById(R.id.star2Badges);
		ImageView star3 = (ImageView) rootView.findViewById(R.id.star3Badges);
		ImageView star4 = (ImageView) rootView.findViewById(R.id.star4Badges);
		ImageView star5 = (ImageView) rootView.findViewById(R.id.star5Badges);
		ListView listView = (ListView) rootView.findViewById(R.id.listViewBadges);

		Bitmap bm = mainActivity.getUserImage();

		CropImage cim = new CropImage(200, bm);
		userImg.setImageBitmap(cim.getCroppedBitmap());

		description.setText("Especialista em " + area);
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


		adapter = new AdapterBadge(getActivity().getApplicationContext(), lista);
		listView.setAdapter(adapter);

		/*
		View view = null;
		for(int i = 0; i < badge_tipos.length; i++) {
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.descript = (TextView) rootView.findViewById(R.id.badge_description);
			viewHolder.title = (TextView) rootView.findViewById(R.id.badge_title);
			viewHolder.token = (ImageView) rootView.findViewById(R.id.Bagde_image);

			if (has_badge[i] == 1) {
				viewHolder.descript.setText("Você possui essa Badge!");
				viewHolder.title.setText(badge_tipos[i].toUpperCase()+"");
				viewHolder.token.setImageResource(getIdBadgeImg(badge_tipos[i]));
			} else {
				viewHolder.descript.setText("Acerte mais questões para conquistar essa badge!");
				viewHolder.title.setText("Badge desconhecida");
				viewHolder.token.setImageResource(getIdBadgeImg(badge_tipos[i]));
				viewHolder.token.setImageAlpha(2);
			}

			view.setTag(viewHolder);
			listView.addFooterView(view);
		}
		*/
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

	*/
	
	public static int getIdBadgeImg(String badge){
		if (badge.equals("caseiro")) return R.drawable.badge_caseiro;
		if (badge.equals("flash")) return R.drawable.badge_flash;
		if (badge.equals("lerdo")) return R.drawable.badge_lerdo;
		if (badge.equals("dev")) return R.drawable.badge_dev;
		return 0;
	}

	/*
	
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