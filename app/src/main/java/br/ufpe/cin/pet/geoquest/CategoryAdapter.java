package br.ufpe.cin.pet.geoquest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;
import me.itangqi.waveloadingview.WaveLoadingView;

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
            protected WaveLoadingView progresso;
            protected TextView nome;
            protected RelativeLayout jogar;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            View view = convertView;

            ViewHolder vh = null;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_category, null);
                vh = new ViewHolder();
                vh.descricao = (TextView) view
                        .findViewById(R.id.cat_description);
                vh.progresso = (WaveLoadingView) view.findViewById(R.id.waveLoadingView);
                vh.nome = (TextView) view.findViewById(R.id.cat_name);
                vh.jogar = (RelativeLayout) view.findViewById(R.id.item);

                view.setTag(vh);
            } else {
                view = convertView;
                vh = ((ViewHolder) view.getTag());
            }

            final Category category = getItem(position);
            final Context ctx = this.parent;
            final CharSequence[] levels = {"Nível 1", "Nível 2", "Nível 3"};

            double pr = (double)100*(category.getDone()/(double)category.getTotal());

            final CharSequence message = pr+"%";
            final CharSequence unavailability = "Nível Indisponível";
            final CharSequence completed = "Nível concluído";

            vh.nome.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "cooper-black.ttf"));
            vh.descricao.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Calibri.ttf"));
            vh.descricao.setText(category.getDescription());
            vh.progresso.setProgressValue((int)pr);
            vh.progresso.setCenterTitle((int)pr + "%");

            /*vh.progresso.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });*/

            vh.nome.setText(category.getName());

            vh.jogar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(ctx).setItems(levels, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int pos) {
                            if (category.is_completed(pos + 1)) {
                                Toast toast = Toast.makeText(ctx, completed, Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (category.is_unavailable(pos + 1)) {
                                Toast toast = Toast.makeText(ctx, unavailability, Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                MainActivity ma = (MainActivity) ctx;
                                ma.getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new TransitionFragment(category, pos + 1, 0, 0, 1)).addToBackStack("transition_fragment").commit();


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
