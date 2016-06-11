package br.ufpe.cin.pet.geoquest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufpe.cin.pet.geoquest.classes.Raking;

public class AdapterRaking extends ArrayAdapter<Raking> implements Filterable {
	private List<Raking> originalData = null;
	private List<Raking> filteredData = null;
	private LayoutInflater mInflater;
	private ItemFilter filter = null;

	public AdapterRaking(Context context, List<Raking> list) {
		super(context, R.layout.list_item_raking, list);
		this.originalData = new ArrayList<Raking>();
		originalData.addAll(list);
		this.filteredData = new ArrayList<Raking>();
		filteredData.addAll(originalData);
		mInflater = LayoutInflater.from(getContext());
		getFilter();
	}

	@Override
	public int getCount() {
		return filteredData.size();
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new ItemFilter();
		}
		return filter;
	}

	static class ViewHolder {
		protected TextView number;
		protected ImageView photo;
		protected TextView name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		ViewHolder viewHolder = null;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.list_item_raking, null);
			viewHolder = new ViewHolder();
			viewHolder.number = (TextView) view
					.findViewById(R.id.list_item_raking_number_textView);
			viewHolder.photo = (ImageView) view
					.findViewById(R.id.list_item_raking_imageView);
			viewHolder.name = (TextView) view
					.findViewById(R.id.list_item_raking_name_textView);

			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = ((ViewHolder) view.getTag());
		}

		if (position < filteredData.size()) {
			Raking raking = filteredData.get(position);

			viewHolder.number.setText(raking.getNumber());
			raking.setColor();
			viewHolder.number.setTextColor(view.getResources().getColor(raking.getColor()));
			viewHolder.photo.setImageBitmap(getCroppedBitmap(raking.getPicture(), 50));
			viewHolder.name.setText(raking.getName());
			viewHolder.number.setTextSize(24);
		}
		return view;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults result = new FilterResults();

			if(constraint == null || constraint.length() == 0) {
				result.count = originalData.size();
				result.values = originalData;
			} else {
				List<Raking> filteredItems = new ArrayList<Raking>();

				for (Raking r : originalData) {
					if (r.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
						filteredItems.add(r);
					}
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
									  FilterResults results) {

			if (results.count == 0) {
				notifyDataSetInvalidated();
			} else {
				filteredData = (List<Raking>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int raio) {
		Bitmap sbmp;
		if(bmp.getWidth() != raio || bmp.getHeight() != raio) {
			sbmp = Bitmap.createScaledBitmap(bmp, raio, raio, false);
		} else {
			sbmp = bmp;
		}

		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
				sbmp.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
				sbmp.getWidth() / 2+0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}
}
