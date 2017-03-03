package br.ufpe.cin.pet.geoquest;

import android.accessibilityservice.GestureDescription;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreditsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_credits, container,
				false);
		
		getActivity().getActionBar().setTitle("Créditos");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		populateView(rootView);
		
		return rootView;
	}

	class ViewHolder {
		TextView text;
		TextView part1;
		TextView part2;
		ImageButton img;
	}

	void populateView (final View rootView) {

		ViewHolder viewHolder = new ViewHolder();

		viewHolder.text = (TextView) rootView.findViewById(R.id.thanks);
		viewHolder.part1 = (TextView) rootView.findViewById(R.id.partner);
		viewHolder.part2 = (TextView) rootView.findViewById(R.id.partner2);
		viewHolder.img = (ImageButton) rootView.findViewById(R.id.exit);

		Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "cooper-black.ttf");
		viewHolder.text.setTypeface(type);

		SpannableString str = new SpannableString("O GeoQuest é uma parceria do");
		str.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 28, 0);
		str.setSpan(new ForegroundColorSpan(Color.rgb(81, 184, 93)), 2, 10, 0);

		viewHolder.part1.setText(str);
		viewHolder.part1.setTypeface(type);
		viewHolder.part2.setTypeface(type);
		viewHolder.img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new MainFragment());
				ft.addToBackStack("main_fragment");
				ft.commit();
			}
		});

	}
}
