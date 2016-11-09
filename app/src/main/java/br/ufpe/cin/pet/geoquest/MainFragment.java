package br.ufpe.cin.pet.geoquest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment{

	RelativeLayout app_ranking;
	RelativeLayout app_statistics;
	RelativeLayout app_myBadges;

    TextView userName;

	Button btn_play;

    private View rootView;

    private MainActivity activity;

    private ProfileTracker profileTracker;

    private ImageView userImage;

	private ProgressDialog progressDialog;

	private static int fullStars = 0;

	private final int[] STARS_IDS = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		activity = (MainActivity) getActivity();

		getActivity().getActionBar().setTitle("Geoquest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

        userName = (TextView) rootView.findViewById(R.id.nameUserPerfil);

        userImage = (ImageView) rootView.findViewById(R.id.imgUserPerfil);

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

        updateProfile(rootView);

        if(activity.getUserImage() != null){
            userImage.setImageBitmap(activity.getUserImage());
        }else{
            updateImage();
        }


		app_ranking = (RelativeLayout) rootView.findViewById (R.id.layoutRanking);
		app_ranking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new RankingFragment()).commit();
			}
		});

		app_statistics = (RelativeLayout) rootView.findViewById (R.id.layoutEstatisticas);
		app_statistics.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new StatsFragment()).commit();
			}
		});

		app_myBadges = (RelativeLayout) rootView.findViewById (R.id.layoutMeusBadges);
		app_myBadges.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new MyBadgesFragment()).commit();
			}
		});

		btn_play = (Button) rootView.findViewById (R.id.btnPlay);
		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
						.replace(R.id.container, new CategoryFragmentUp()).commit();
			}
		});




		return rootView;
	}

    public void updateProfile(View rootView){
        Log.i("MainFragment", "Updating user information " + Profile.getCurrentProfile().getName());
        Profile profile = Profile.getCurrentProfile();
		if(fullStars == 0)
			getStars(rootView);
		else
		{
			updateStars(rootView);
			progressDialog.hide();
		}


        userName.setText(profile.getName());
		activity.setUserName(profile.getName());
    }

    public void updateImage(){
        new FacebookImageTask(userImage, activity).execute(Profile.getCurrentProfile().getProfilePictureUri(150,150).toString());
    }



	private void getStars(final View rootView){

		String url = "http://www.mocky.io/v2/57678dc30f00000a08291dc8";

		Log.i("Statistics", "Enviando requisição de estrelas");

		StringRequest sr;
		sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("Response", response);
				try	 {
					JSONObject jsonResponse = new JSONObject(response);
					fullStars = jsonResponse.getInt("fullstars");
					updateStars(rootView);
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


	private void updateStars(final View rootView){
		for(int i = 0; i < fullStars; i++){
			ImageView star = (ImageView) rootView.findViewById(STARS_IDS[i]);
			star.setImageResource(R.drawable.star_full);
		}
	}



}
