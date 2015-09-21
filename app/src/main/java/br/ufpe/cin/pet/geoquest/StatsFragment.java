package br.ufpe.cin.pet.geoquest;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.Vector;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.ufpe.cin.pet.geoquest.classes.Stats;

public class StatsFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        getActivity().getActionBar().setTitle("Geoquest");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        Stats.PairAreaPercentage pairGeo = new Stats.PairAreaPercentage("Geologia", 67);
        Stats.PairAreaPercentage pairHidro = new Stats.PairAreaPercentage("Hidrografia", 35);
        Stats.PairAreaPercentage pairPol = new Stats.PairAreaPercentage("Política", 88);

        Vector<Stats.PairAreaPercentage> pairs = new Vector<Stats.PairAreaPercentage>();
        pairs.add(pairGeo);
        pairs.add(pairHidro);
        pairs.add(pairPol);

        Stats stats = new Stats(2, "Geologia", 56, pairs);

        TextView rankingPosition = (TextView) rootView.findViewById(R.id.rankingPosition);
        TextView favoriteArea = (TextView) rootView.findViewById(R.id.favoriteArea);
        TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
        ProgressBar progressBarPercentageCompleted = (ProgressBar) rootView.findViewById(R.id.progressBarPercentageCompleted);

        rankingPosition.setText(stats.rankingPos + "° posição");
        favoriteArea.setText(stats.favoriteArea);
        progressBarText.setText(stats.percentageCompleted + "% ");
        progressBarPercentageCompleted.setProgress(stats.percentageCompleted);

        AdapterStats adapter = new AdapterStats(getActivity().getApplicationContext(), stats.areas);

        ListView listView = (ListView) rootView.findViewById(R.id.listViewStats);
        listView.setAdapter(adapter);

        return rootView;
    }

}
