package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufpe.cin.pet.geoquest.Utils.BitmapFromURL;
import br.ufpe.cin.pet.geoquest.Utils.Cloud;

public class CreditsFragment extends Fragment {

	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_credits, container, false);
		
		getActivity().getActionBar().setTitle("Créditos");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().hide();

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		populateView(rootView);
		
		return rootView;
	}

	class ViewHolder {
		TextView text;
		TextView part1;
		ImageView img;
	}

	void populateView (final View rootView) {

		ViewHolder viewHolder = new ViewHolder();

		viewHolder.text = (TextView) rootView.findViewById(R.id.thanks);
		viewHolder.part1 = (TextView) rootView.findViewById(R.id.partner);
		viewHolder.img = (ImageView) rootView.findViewById(R.id.exit);

		Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "cooper-black.ttf");
		viewHolder.text.setTypeface(type);

		viewHolder.part1.setText("É uma parceria do");
		viewHolder.part1.setTextColor(Color.rgb(247, 160, 47));

		viewHolder.part1.setTypeface(type);

		viewHolder.img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new MainFragment());
				ft.addToBackStack("main_fragment");
				ft.commit();
			}
		});

		getImage(rootView, "logocon.png", 1);
		getImage(rootView, "logopet.png", 2);

	}

	private void getImage(final View rootView, String str, final int type) {

		final String src = Cloud.cloudinary.url().generate(str);

		new AsyncTask<Void, Void, Void>() {
			Bitmap bm = null;

			@Override
			protected Void doInBackground(Void... params) {
				bm = new BitmapFromURL(src).getBitmapFromURL();
				return null;
			}

			@Override
			protected void onPostExecute(Void v) {
				ImageView img;
				if (isAdded()) {
					if (type == 1) {
						img = (ImageView) rootView.findViewById(R.id.petcon);
						img.setImageBitmap(bm);
					} else {
						img = (ImageView) rootView.findViewById(R.id.petinfo);
						img.setImageBitmap(bm);
					}
				}
				progressDialog.hide();
			}
		}.execute();
	}
}
