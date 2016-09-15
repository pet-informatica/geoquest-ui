package br.ufpe.cin.pet.geoquest;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
            protected ImageView imagem;
            protected TextView descricao;
            protected ProgressBar progresso;
            protected TextView nome;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            ViewHolder vh = null;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_category, null);
                vh = new ViewHolder();
                //vh.imagem = (ImageView) view.findViewById(R.id.Category_image);
                vh.descricao = (TextView) view
                        .findViewById(R.id.category_description);
                vh.progresso = (ProgressBar) view.findViewById(R.id.progressoCategoria);
                vh.nome = (TextView) view.findViewById(R.id.category_name);

                view.setTag(vh);
            } else {
                view = convertView;
                vh = ((ViewHolder) view.getTag());
            }

            Category category = getItem(position);

            vh.descricao.setText(category.getDescription());
            //vh.imagem.setImageResource(category.getId());
            //vh.imagem.setImageResource(R.id.);
            vh.progresso.setProgress(category.getDone());
            vh.nome.setText(category.getName());

            return view;
        }

}
