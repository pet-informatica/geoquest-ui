package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Badge;

/**
 * Created by rbb3 on 29/08/16.
 */
public class CategoryFragmentUp extends Fragment {

    private List<Badge> lista;
    private AdapterBadge adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        return rootView;
    }

}
