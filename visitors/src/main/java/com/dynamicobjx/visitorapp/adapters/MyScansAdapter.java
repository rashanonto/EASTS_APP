package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class MyScansAdapter extends BaseAdapter {

    private ArrayList<ParseObject> myScans;
    private Context context;
    private LayoutInflater inflater;

    public MyScansAdapter(Context context, ArrayList<ParseObject> myScans) {
        this.myScans = myScans;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myScans.size();
    }

    @Override
    public ParseObject getItem(int i) {
        return myScans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_myscans,viewGroup,false);
        TextView tvMyScans = (TextView)view.findViewById(R.id.tvMyScans);
        tvMyScans.setText(getItem(i).getParseObject("exhibitorId").getString("fname"));
        return view;
    }
}
