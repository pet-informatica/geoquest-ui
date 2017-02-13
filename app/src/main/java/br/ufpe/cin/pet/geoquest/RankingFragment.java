package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Raking;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RankingFragment extends Fragment implements
		SearchView.OnQueryTextListener {

	private AdapterRaking adapter;
	private final OkHttpClient client = new OkHttpClient();
	private ProgressDialog progressDialog;

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

		getActivity().getActionBar().setTitle("Ranking");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		try {
			new AsyncTask<Void, Void, Void>() {
				List<Raking> items = new ArrayList<Raking>();

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
					adapter = new AdapterRaking(getActivity().getApplicationContext(),items);

					ListView listView = (ListView) rootView.findViewById(R.id.listViewRaking);
					listView.setAdapter(adapter);

					progressDialog.hide();
				}
			}.execute();

		} catch (Exception e) {
			Log.i("ERROR", "Não foi possível obter o ranking de seus amigos");
			e.printStackTrace();
		}

		EditText editText = (EditText) rootView.findViewById(R.id.searchRaking);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		return rootView;
	}

	private List<Raking> run() throws Exception {

		//String url = "http://www.mocky.io/v2/587665d1100000b70b8b5c85";
		String url = getResources().getString(R.string.base_url)+"users/ranking/";

		Request request = new Request.Builder()
				.url(url)
				.header("Authorization", Config.key)
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

		List<Raking> items = new ArrayList<Raking>();
		try {
			JSONArray obj = new JSONArray(response.body().string());
			Bitmap bm = null;

			InputStream in;
			int tam = obj.length();
			for (int i = 0; i < tam; i++) {
				JSONObject object = obj.getJSONObject(i);

				String nome = object.getString("name");
				String foto = object.getString("profile_picture");
				int pontuacao = object.getInt("score");

				in = new URL(foto).openStream();
				bm = BitmapFactory.decodeStream(in);
				Raking raking = new Raking(Integer.toString(i+1), nome, bm, pontuacao);
				items.add(raking);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("JSONError", "Erro na formatação do response");
		}
		return items;
	}
}
