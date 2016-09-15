package br.ufpe.cin.pet.geoquest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;

/**
 * Created by Tomer Simis on 19/04/2015.
 * Modified by Ruy Brito on 14/09/2016
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

        private List<Category> categories;
        private LayoutInflater mInflater;

        public CategoryAdapter(Context context, List<Category> categories){
            super(context, R.layout.list_item_category, categories);
            this.categories = categories;
            mInflater = LayoutInflater.from(getContext());
        }

        static class ViewHolder {
            protected TextView descricao;
            protected ProgressBar progresso;
            protected TextView nome;
            protected Button jogar;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            ViewHolder vh = null;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_category, null);
                vh = new ViewHolder();
                vh.descricao = (TextView) view
                        .findViewById(R.id.category_description);
                vh.progresso = (ProgressBar) view.findViewById(R.id.progressCategoria);
                vh.nome = (TextView) view.findViewById(R.id.category_name);
                vh.jogar = (Button) view.findViewById(R.id.buttonPlay);

                view.setTag(vh);
            } else {
                view = convertView;
                vh = ((ViewHolder) view.getTag());
            }

            final Category category = getItem(position);

            vh.descricao.setText(category.getDescription());
            vh.progresso.setProgress(category.getDone());
            vh.nome.setText(category.getName());

            vh.jogar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(CategoryAdapter.this.getContext()).create();
                    alert.setTitle(category.getName());

                    alert.setButton("Nível 1", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int pos) {
                            Log.e("ashuahsuahsu", "ahsshsaudha");
                        }
                    });

                    alert.show();
                }
            });

            return view;
        }

}
