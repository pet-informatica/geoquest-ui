package br.ufpe.cin.pet.geoquest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.app.Fragment;

public class RankingFragment extends Fragment implements
SearchView.OnQueryTextListener {
	private AdapterRaking adapter;

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ranking, container,
				false);
		
		getActivity().getActionBar().setTitle("Ranking");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Raking[] person = new Raking[5];
		person[0] = new Raking("1", "Angelina Brittain");
		person[1] = new Raking("2", "Carlina Brittain");
		person[2] = new Raking("3", "Cristina Brittain");
		person[3] = new Raking("4", "Zelina Brittain");
		person[4] = new Raking("5", "Cantolina Brittain");

		List<Raking> items = new ArrayList<Raking>(Arrays.asList(person));

		adapter = new AdapterRaking(getActivity().getApplicationContext(),items);

		ListView listView = (ListView) rootView.findViewById(R.id.listViewRaking);
		listView.setAdapter(adapter);
		
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
}
