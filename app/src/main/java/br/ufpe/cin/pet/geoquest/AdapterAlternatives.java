package br.ufpe.cin.pet.geoquest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Alternative;


/**
 * Created by rbb3 on 12/01/17.
 */
public class AdapterAlternatives extends ArrayAdapter<Alternative> {

    private List<Alternative> list = null;
    private LayoutInflater mInflater;

    public AdapterAlternatives(Context context, List<Alternative> list) {
        super(context, R.layout.list_item_question, list);
        this.list = list;
        mInflater = LayoutInflater.from(getContext());
    }

    static class ViewHolder {
        protected TextView description;
        protected ImageView image;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_question, null);

            viewHolder = new ViewHolder();

            viewHolder.description = (TextView) view.findViewById(R.id.alternative_description);
            viewHolder.image = (ImageView) view.findViewById(R.id.alternative_image);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        viewHolder.description.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Calibri.ttf"));
        if (pos < list.size()) {
            Alternative alternative = list.get(pos);

            viewHolder.description.setText(alternative.getText());

            String seq = alternative.getSequence();
            if (seq.equals("A")) viewHolder.image.setImageResource(R.drawable.a);
            else if (seq.equals("B")) viewHolder.image.setImageResource(R.drawable.b);
            else if (seq.equals("C")) viewHolder.image.setImageResource(R.drawable.c);
            else if (seq.equals("D")) viewHolder.image.setImageResource(R.drawable.d);
            else if (seq.equals("E")) viewHolder.image.setImageResource(R.drawable.e);
        }

        return view;
    }
}
