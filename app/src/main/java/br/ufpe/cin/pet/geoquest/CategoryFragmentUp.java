package br.ufpe.cin.pet.geoquest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Badge;
import br.ufpe.cin.pet.geoquest.classes.Category;

/**
 * Created by rbb3 on 29/08/16.
 */
public class CategoryFragmentUp extends Fragment {

    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        CategoryRequest cr = new CategoryRequest("http://www.mocky.io/v2/5810e66e3a0000780760985d");
        cr.execute();

        List<Category> items = new ArrayList<Category>();


        try {
            items = cr.get();
        } catch (Exception e) {
            Log.i("ERROR", "Não foi possível obter as suas categorias");
            e.printStackTrace();
        }


        adapter = new CategoryAdapter(getActivity().getApplicationContext(), items, this.getActivity());

        ListView listView = (ListView) rootView.findViewById(R.id.listViewCategories);
        listView.setAdapter(adapter);

        return rootView;
    }

}
