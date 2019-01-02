package com.wsu.dailyfitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainMenuAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    String name;

    public MainMenuAdapter(Context context, String[] values) {
        super(context, R.layout.logoadapter, values);
        this.context = context;
        this.values = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.logoadapter, parent, false);
        TextView textView =  rowView.findViewById(R.id.label);
        textView.setText(values[position]);

        return rowView;
    }
}

