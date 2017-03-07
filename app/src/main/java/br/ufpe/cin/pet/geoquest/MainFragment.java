package br.ufpe.cin.pet.geoquest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;

import org.json.JSONObject;

import java.io.IOException;

import br.ufpe.cin.pet.geoquest.Utils.CropImage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFragment extends Fragment{

	RelativeLayout app_ranking;
	RelativeLayout app_statistics;
	RelativeLayout app_myBadges;

    TextView userName;

	Button btn_play;

    private View rootView;

    private MainActivity activity;

    private ImageView userImage;

	private ImageView backImage;

	private ProgressDialog progressDialog;

	private static int fullStars = 0;

	private final int[] STARS_IDS = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

	private final OkHttpClient client = new OkHttpClient();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		activity = (MainActivity) getActivity();

		getActivity().getActionBar().setTitle("Geoquest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().show();

        userName = (TextView) rootView.findViewById(R.id.nameUserPerfil);

        userImage = (ImageView) rootView.findViewById(R.id.imgUserPerfil);

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

        updateProfile(rootView);

        if (activity.getUserImage() != null) {
            userImage.setImageBitmap(activity.getUserImage());
        } else {
            updateImage();
        }

		app_ranking = (RelativeLayout) rootView.findViewById (R.id.layoutRanking);
		app_ranking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new RankingFragment());
				ft.addToBackStack("ranking_fragment");
				ft.commit();
			}
		});

		app_statistics = (RelativeLayout) rootView.findViewById (R.id.layoutEstatisticas);
		app_statistics.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new StatsFragment());
				ft.addToBackStack("stats_fragment");
				ft.commit();
			}
		});

		app_myBadges = (RelativeLayout) rootView.findViewById (R.id.layoutMeusBadges);
		app_myBadges.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new MyBadgesFragment());
				ft.addToBackStack("badges_fragment");
				ft.commit();
			}
		});

		btn_play = (Button) rootView.findViewById (R.id.btnPlay);
		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new CategoryFragment());
				ft.addToBackStack("category_fragment");
				ft.commit();
			}
		});

		return rootView;
	}

    public void updateProfile(View rootView){
        Log.i("MainFragment", "Updating user information " + Profile.getCurrentProfile().getName());
        Profile profile = Profile.getCurrentProfile();

		if(fullStars == 0) {
			getStars(rootView);
		} else {
			updateStars(rootView);
			progressDialog.hide();
		}

        userName.setText(profile.getName());
		activity.setUserName(profile.getName());

    }

    public void updateImage() {
        new FacebookImageTask(userImage, activity).execute(Profile.getCurrentProfile().getProfilePictureUri(150,150).toString());
    }

	private void getStars(final View rootView){

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
					updateStars(rootView);
					progressDialog.hide();
				}
			}.execute();

		} catch (Exception e) {
			Log.i("ERROR", "Não foi possível obter as suas estrelas");
			e.printStackTrace();
		}
	}

	private void run() throws Exception {

		String url = getResources().getString(R.string.base_url)+"users/stars/";
		//String url = "http://www.mocky.io/v2/57678dc30f00000a08291dc8";

		Request request = new Request.Builder()
				.url(url)
				.header("Authorization", Config.getKey())
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

		try {
			JSONObject jsonResponse = new JSONObject(response.body().string());
			fullStars = jsonResponse.getInt("stars");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("JSONError", "Erro na formatação do response");
		}
	}


	private void updateStars(final View rootView){
		for(int i = 0; i < fullStars; i++){
			ImageView star = (ImageView) rootView.findViewById(STARS_IDS[i]);
			star.setImageResource(R.drawable.star_full);
		}
	}
}
