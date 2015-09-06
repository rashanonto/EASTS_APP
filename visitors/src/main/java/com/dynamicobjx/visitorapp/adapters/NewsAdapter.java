package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.ExhibitorsActivity;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.fragments.NewsFragment;
import com.dynamicobjx.visitorapp.models.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by graciosa on 2/9/15.
 */
public class NewsAdapter extends BaseAdapter {

    private ArrayList<News> news;
    private Context context;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMMM dd, yyyy");

   public NewsAdapter (ArrayList<News> news, Context context) {
       this.news = news;
       this.context = context;
       this.inflater = LayoutInflater.from(context);
       Log.d("news", "news size in constructor --> " + news.size());
   }

    @Override
    public int getCount() {
        Log.d("news", "news size --> " + news.size());
        return news.size();
    }

    @Override
    public News getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.news_header, parent, false);
            holder = new ItemViewHolder();
            holder.tvHeadline = (TextView)convertView.findViewById(R.id.tvHeadline);
            holder.tvPubDate = (TextView)convertView.findViewById(R.id.tvPubDate);
            convertView.setTag(holder);
        }
        else {
            holder = (ItemViewHolder)convertView.getTag();
        }

        holder.tvHeadline.setText(news.get(i).getHeadline());
        holder.tvPubDate.setText(sdf.format(news.get(i).getPublishDate()));
        return convertView;
    }

    private class ItemViewHolder {
        TextView tvHeadline;
        TextView tvPubDate;
    }
}
