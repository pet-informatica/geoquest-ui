package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HelpFragment extends Fragment {
	Button about;
	Button works;
	Button report;
	TextView aboutText;
	TextView worksText;
	TextView reportText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
				false);
		
		getActivity().getActionBar().setTitle("Ajuda");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		about = (Button) rootView.findViewById(R.id.aboutGeoquest);
		works = (Button) rootView.findViewById(R.id.howWorks);
		report = (Button) rootView.findViewById(R.id.ReportProblems);

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

		worksText = (TextView) rootView.findViewById(R.id.howWorkstText);
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

		return rootView;
	}
	
}
