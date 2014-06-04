package br.unirondon.cop;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaCostumizada extends ArrayAdapter<String> {
	
	private final Activity context;
	private final String[] nomes;
	private final Integer[] imgs;
	
	public ListaCostumizada(Activity context, String[] nomes, Integer[] imgs) {
		super(context, R.layout.list_single, nomes);
		this.context = context;
		this.nomes = nomes;
		this.imgs = imgs;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.img);
		
		txtTitle.setText(nomes[position]);
		imgView.setImageResource(imgs[position]);
		
		return rowView;
		
	}
	
}
