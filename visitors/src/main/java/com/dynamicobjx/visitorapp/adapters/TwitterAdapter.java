package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.models.TwitterFeeds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import twitter4j.Twitter;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class TwitterAdapter extends BaseAdapter {

    private ArrayList<TwitterFeeds> tweets;
    private NewsActivity newsActivity;
    private Context context;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMMM dd, yyyy HH:mm a");

    public TwitterAdapter(Context context, ArrayList<TwitterFeeds> tweets) {
        this.tweets = tweets;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.newsActivity = (NewsActivity)context;
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public TwitterFeeds getItem(int i) {
        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_twitter,viewGroup, false);
            holder.tvTwitterName = (TextView)view.findViewById(R.id.tvTwitterName);
            holder.tvTwitterBody = (TextView)view.findViewById(R.id.tvTwitterBody);
            holder.tvTwitterTime = (TextView)view.findViewById(R.id.tvTwitterTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.tvTwitterName.setText(getItem(i).getTwitterName());
        holder.tvTwitterBody.setText(getItem(i).getTwitterBody());
        holder.tvTwitterTime.setText(sdf.format(getItem(i).getTwitterTime()));
        return view;
    }

    private class ViewHolder {
        TextView tvTwitterName;
        TextView tvTwitterBody;
        TextView tvTwitterTime;
    }
}
