package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.Utils.CropImage;
import br.ufpe.cin.pet.geoquest.classes.Badge;

public class MyBadgesFragment extends Fragment {

	private ProgressDialog progressDialog;
	private String area;
	private double percentage;
	private AdapterBadge adapter;
	private final OkHttpClient client = new OkHttpClient();

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

		try {

			new AsyncTask<Void, Void, Void>() {
				List<Badge> items = new ArrayList<Badge>();

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
					populateView(rootView, items);
					progressDialog.hide();
				}
			}.execute();

		} catch (Exception e) {
			Log.i("ERROR", "Não foi possível obter as suas badges");
			e.printStackTrace();
		}
	}

	private List<Badge> run() throws Exception {

		String url = "http://www.mocky.io/v2/5874839f0f0000d12652e2b3";
		//String backUrl = getResources().getString(R.string.base_url)+"users/badge/";

		Request request = new Request.Builder()
				.url(url)
				.header("TOKEN", Config.key)
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

		List<Badge> auxiliar = new ArrayList<Badge>();
		try {
			JSONObject jsonResponse = new JSONObject(response.body().string());
			area = jsonResponse.getString("area");
			percentage = jsonResponse.getDouble("percentage");
			JSONArray badgeresponse = jsonResponse.getJSONArray("badges");

			int size = badgeresponse.length();
			Bitmap bm = null;
			InputStream in;
			Badge badge;
			for(int i = 0; i < size; i++) {
				JSONObject obj = badgeresponse.getJSONObject(i);

				int id = obj.getInt("id");
				String name = obj.getString("name");
				String desc = obj.getString("description");
				int has = obj.getInt("has");
				boolean b;
				if (has == 1) b = true;
				else b = false;
				String img = obj.getString("image");

				in = new URL(img).openStream();
				bm = BitmapFactory.decodeStream(in);
				badge = new Badge(id, name, desc, bm, b);
				auxiliar.add(badge);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("JSONError", "Erro na formatação do response");
		}
		return auxiliar;
	}

	private void populateView(View rootView, List<Badge> items) {

		MainActivity mainActivity = (MainActivity)getActivity();

		getActivity().getActionBar().setTitle("Conquistas");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

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


		adapter = new AdapterBadge(getActivity().getApplicationContext(), items);
		listView.setAdapter(adapter);
	}
	
}