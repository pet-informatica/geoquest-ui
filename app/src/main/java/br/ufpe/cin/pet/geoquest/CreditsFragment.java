package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreditsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_credits, container,
				false);
		
		getActivity().getActionBar().setTitle("Créditos");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		return rootView;
	}
}
