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

        //CategoryRequest cr = new CategoryRequest("http://www.mocky.io/v2/57da0a671100002017d6e9d5");
        //cr.execute();

        List<Category> items = new ArrayList<Category>();

        /*
        try {
            items = cr.get();
        } catch (Exception e) {
            Log.i("ERROR", "Não foi possível obter as suas categorias");
            e.printStackTrace();
        }
        */

        Category cat = new Category("Hidrografia", "Área da geografia física que classifica e estuda as águas do planeta Terra", 73, 100);
        Category cat2 = new Category("Geologia", "Área da geografia física que classifica e estuda as rochas e minerais", 22, 100);
        items.add(cat);
        items.add(cat2);

        adapter = new CategoryAdapter(getActivity().getApplicationContext(), items, this.getActivity());

        ListView listView = (ListView) rootView.findViewById(R.id.listViewCategories);
        listView.setAdapter(adapter);

        return rootView;
    }

}
