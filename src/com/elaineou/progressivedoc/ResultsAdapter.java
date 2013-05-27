package com.elaineou.progressivedoc;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultsAdapter extends ArrayAdapter<ICDResults> {
	private final Context context;
	private final ArrayList<ICDResults> values;

    public ResultsAdapter(Context context, ArrayList<ICDResults> values) {
      super(context, R.layout.list_results, values);
      this.context = context;
      this.values = values;
    }
  
  
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_results, parent, false);
		TextView txtT = (TextView) rowView.findViewById(R.id.treat);
		txtT.setText(values.get(position).getTreatment()); 
		TextView txtMax = (TextView) rowView.findViewById(R.id.maxcost);
		txtMax.setText(values.get(position).getMax()); 
		TextView txtMin = (TextView) rowView.findViewById(R.id.mincost);
		txtMin.setText(values.get(position).getMin()); 
		return rowView;
	}
}	