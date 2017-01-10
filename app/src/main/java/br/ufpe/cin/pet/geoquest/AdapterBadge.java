package br.ufpe.cin.pet.geoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Badge;

/**
 * Created by rbb3 on 24/06/16.
 */
public class AdapterBadge extends ArrayAdapter<Badge> {

    private List<Badge> list = null;
    private LayoutInflater mInflater;

    public AdapterBadge(Context context, List<Badge> list) {
        super(context, R.layout.list_item_badge, list);
        this.list = list;
        mInflater = LayoutInflater.from(getContext());
    }

    static class ViewHolder {
        protected TextView title;
        protected TextView descript;
        protected ImageView token;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_badge, null);

            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) view.findViewById(R.id.badge_title);
            viewHolder.descript = (TextView) view.findViewById(R.id.badge_description);
            viewHolder.token = (ImageView) view.findViewById(R.id.Bagde_image);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        if (pos < list.size()) {
            Badge badge = list.get(pos);

            if (badge.getPossui() == true) {
                viewHolder.title.setText(badge.getNome().toUpperCase()+"");
                viewHolder.descript.setText(badge.getDescricao()+"");
                //viewHolder.token.setImageResource(badge.getId());
            } else {
                viewHolder.title.setText("DESCONHECIDO");
                viewHolder.descript.setText("Conquinta ainda não alcançada");
                //viewHolder.token.setImageResource(badge.getId());
            }
        }

        return view;
    }
}
