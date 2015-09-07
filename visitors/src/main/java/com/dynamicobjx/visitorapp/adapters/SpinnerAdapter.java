package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;

import java.util.List;

/**
 * Created by rsbulanon on 2/19/15.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Typeface tfSegoe;
    private Context context;
    private int textColor;

    public SpinnerAdapter(Context context, int resource, Typeface tfSegoe) {
        super(context, resource);
        this.tfSegoe = tfSegoe;
        this.context = context;
    }

    public SpinnerAdapter(Context context, int resource, List<String> objects,
                          Typeface tfSegoe, int textColor) {
        super(context, resource, objects);
        this.tfSegoe = tfSegoe;
        this.context = context;
        this.textColor = textColor;
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(tfSegoe);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        view.setTextColor(textColor);
        view.setBackgroundColor(Color.BLACK);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(tfSegoe);
        view.setTextColor(textColor);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        return view;
    }
}
