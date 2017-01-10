package br.ufpe.cin.pet.geoquest;

/**
 * Created by Tomer Simis on 26/07/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Stats;

public class AdapterStats extends ArrayAdapter<Stats.PairAreaPercentage>{
    private List<Stats.PairAreaPercentage> areas = null;
    private LayoutInflater mInflater;

    public AdapterStats(Context context, List<Stats.PairAreaPercentage> list) {
        super(context, R.layout.list_item_stats, list);
        areas = list;
        mInflater = LayoutInflater.from(getContext());
        getFilter();
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    static class ViewHolder {
        protected TextView statsArea;
        protected ProgressBar areaPercentageCompleted;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_stats, null);
            viewHolder = new ViewHolder();
            viewHolder.statsArea = (TextView) view
                    .findViewById(R.id.statsArea);
            viewHolder.areaPercentageCompleted = (ProgressBar) view
                    .findViewById(R.id.areaPercentageCompleted);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        Stats.PairAreaPercentage pair = areas.get(position);
        viewHolder.statsArea.setText(pair.area);
        viewHolder.areaPercentageCompleted.setProgress(pair.percentage);
        viewHolder.areaPercentageCompleted.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C00") , Mode.SRC_IN);

        return view;
    }
}
