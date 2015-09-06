package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.MyVisitsListingActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/24/15.
 */
public class MyVisitsListingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> headers;
    private ArrayList<String> subHeaders;
    private ArrayList<String> icons;
    private LayoutInflater inflater;
    private MyVisitsListingActivity myVisitsListingActivity;

    public MyVisitsListingAdapter(Context context, ArrayList<String> icons, ArrayList<String> headers,
                                  ArrayList<String> subHeaders) {
        this.context = context;
        this.icons = icons;
        this.headers = headers;
        this.subHeaders = subHeaders;
        this.inflater = LayoutInflater.from(context);
        this.myVisitsListingActivity = (MyVisitsListingActivity)context;
    }

    @Override
    public int getCount() {
        return headers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_myvisits_listing, null);
        TextView tvIcon = (TextView )view.findViewById(R.id.tvIcon);
        TextView tvMyVisits = (TextView)view.findViewById(R.id.tvMyVisits);
        TextView tvMyVisitsSub = (TextView)view.findViewById(R.id.tvMyVisitsSub);
        tvIcon.setText(icons.get(i));
        tvMyVisits.setText(headers.get(i));
        tvMyVisitsSub.setText(subHeaders.get(i));
        tvIcon.setTypeface(myVisitsListingActivity.getTfBootstrap());
        tvMyVisits.setTypeface(myVisitsListingActivity.getTfSegoe(), Typeface.BOLD);
        tvMyVisitsSub.setTypeface(myVisitsListingActivity.getTfSegoe());
        return view;
    }
}
