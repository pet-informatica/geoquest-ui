package br.ufpe.cin.pet.geoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;

/**
 * Created by Tomer Simis on 19/04/2015.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

        private List<Category> categories;

        public CategoryAdapter(Context context, List<Category> categories){
            super(context, android.R.layout.simple_list_item_2, categories);
            this.categories = categories;
        }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);

        Category category = getItem(position);
        text1.setText(category.getName());
        text2.setText(category.getDescription());

        return convertView;
    }

}
