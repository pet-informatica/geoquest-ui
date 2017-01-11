package br.ufpe.cin.pet.geoquest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;

/**
 * Created by Tomer Simis on 19/04/2015.
 * Modified by Ruy Brito on 14/09/2016
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

        private List<Category> categories;
        private LayoutInflater mInflater;
        private Context parent;

        public CategoryAdapter(Context context, List<Category> categories, Context parent) {
            super(context, R.layout.list_item_category, categories);
            this.categories = categories;
            mInflater = LayoutInflater.from(getContext());
            this.parent = parent;
        }

        static class ViewHolder {
            protected TextView descricao;
            protected ProgressBar progresso;
            protected TextView nome;
            protected Button jogar;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
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
            final Context ctx = this.parent;
            final CharSequence[] levels = {"Nível 1", "Nível 2", "Nível 3"};

            double pr = (double)100*category.getDone()/(double)category.getTotal();

            final CharSequence message = pr+"%";
            final CharSequence unavailability = "Nível Indisponível";

            vh.descricao.setText(category.getDescription());
            vh.progresso.setProgress(category.getDone());

            vh.progresso.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            vh.nome.setText(category.getName());

            vh.jogar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(ctx).setItems(levels, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int pos) {
                            if (category.is_available(pos + 1)) {
                                MainActivity ma = (MainActivity) ctx;
                                ma.getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new TransitionFragment(category, pos + 1, 1)).addToBackStack("transition_fragment").commit();
                            } else {
                                Toast toast = Toast.makeText(ctx, unavailability, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }).create();

                    alert.setTitle(category.getName());
                    alert.show();
                }
            });

            return view;
        }

}
