package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpFragment extends Fragment {
	Button about;
	Button works;
	Button report;
	TextView aboutText;
	TextView worksText;
	TextView reportText;
	ImageButton exit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
				false);
		
		getActivity().getActionBar().setTitle("Ajuda");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().hide();

		exit = (ImageButton) rootView.findViewById(R.id.exit2);

		Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "cooper-black.ttf");

		about = (Button) rootView.findViewById(R.id.aboutGeoquest);
		about.setTypeface(type);
		works = (Button) rootView.findViewById(R.id.howWorks);
		works.setTypeface(type);
		report = (Button) rootView.findViewById(R.id.ReportProblems);
		report.setTypeface(type);

		aboutText = (TextView) rootView.findViewById(R.id.aboutGeoquestText);
		aboutText.setVisibility(View.GONE);

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (aboutText.getVisibility() == View.VISIBLE) {
					aboutText.setVisibility(View.GONE);
					about.setBackgroundResource(R.drawable.div_down);
				} else {
					aboutText.setVisibility(View.VISIBLE);
					about.setBackgroundResource(R.drawable.div_up);
				}
			}
		});

		worksText = (TextView) rootView.findViewById(R.id.howWorksText);
		worksText.setVisibility(View.GONE);

		works.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (worksText.getVisibility() == View.VISIBLE) {
					worksText.setVisibility(View.GONE);
					works.setBackgroundResource(R.drawable.div_down);
				} else {
					worksText.setVisibility(View.VISIBLE);
					works.setBackgroundResource(R.drawable.div_up);
				}
			}
		});

		reportText = (TextView) rootView.findViewById(R.id.reportProblemsText);
		reportText.setVisibility(View.GONE);

		report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (reportText.getVisibility() == View.VISIBLE) {
					reportText.setVisibility(View.GONE);
					report.setBackgroundResource(R.drawable.div_down);
				} else {
					reportText.setVisibility(View.VISIBLE);
					report.setBackgroundResource(R.drawable.div_up);
				}
			}
		});

		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.container, new MainFragment());
				ft.addToBackStack("main_fragment");
				ft.commit();
			}
		});

		return rootView;
	}
	
}
