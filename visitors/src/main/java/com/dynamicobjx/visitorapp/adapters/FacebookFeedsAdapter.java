package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.models.FacebookFeeds;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/5/15.
 */
public class FacebookFeedsAdapter extends BaseAdapter {

    private ArrayList<FacebookFeeds> feeds;
    private Context context;
    private LayoutInflater inflater;
    private NewsActivity newsActivity;

    public FacebookFeedsAdapter(Context context, ArrayList<FacebookFeeds> feeds) {
        this.context = context;
        this.feeds = feeds;
        this.newsActivity = (NewsActivity)context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public FacebookFeeds getItem(int i) {
        return feeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.row_facebook_feeds, viewGroup, false);
            holder = new ViewHolder();
            holder.tvFeedFrom = (TextView)view.findViewById(R.id.tvFeedFrom);
            holder.tvFeedMessage = (TextView)view.findViewById(R.id.tvFeedMessage);
            holder.tvDatePosted = (TextView)view.findViewById(R.id.tvDatePosted);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        holder.tvFeedMessage.setText(getItem(i).getMessage());
        holder.tvFeedFrom.setText(getItem(i).getFrom());
        holder.tvDatePosted.setText(getItem(i).getDatePosted());
        return view;
    }

    private class ViewHolder {
        TextView tvFeedMessage;
        TextView tvFeedFrom;
        TextView tvDatePosted;
    }
}
