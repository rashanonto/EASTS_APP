package com.dynamicobjx.visitorapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.activities.NewsDetailsActivity;
import com.dynamicobjx.visitorapp.adapters.NewsAdapter;
import com.dynamicobjx.visitorapp.helpers.ParseHelper;
import com.dynamicobjx.visitorapp.listeners.OnLoadDataListener;
import com.quentindommerc.superlistview.SuperListview;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class NewsFragment extends android.support.v4.app.Fragment implements OnLoadDataListener  {

    private ParseHelper parseHelper;
    private SuperListview slvNews;
    private NewsActivity newsActivity;
    private LinearLayout llPleaseWait;
    private boolean isRefreshing;
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsActivity = (NewsActivity)getActivity();
        slvNews = (SuperListview)view.findViewById(R.id.slvNews);
        llPleaseWait = (LinearLayout)view.findViewById(R.id.llPleaseWait);
        parseHelper = new ParseHelper();
        parseHelper.setOnLoadDataListener(this);
        slvNews.setAdapter(new NewsAdapter(newsActivity.getNewsList(),getActivity()));
        slvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("news", newsActivity.getNewsList().get(i));
            Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ((NewsActivity) getActivity()).animateToLeft(getActivity());
            }
        });

        slvNews.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newsActivity.isNetworkAvailable()) {
                    isRefreshing = true;
                    parseHelper.getNews(newsActivity.getNewsList(),false);
                } else {
                    newsActivity.showToast("Please turn on your WiFi/Data connection!");
                    slvNews.getSwipeToRefresh().setRefreshing(false);
                    ((BaseAdapter)slvNews.getAdapter()).notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onPreLoad() {
        Log.d("login","news ON PRE LOAD");
        newsActivity.getNewsList().clear();
    }

    @Override
    public void onLoadFinished(int counter) {
        Log.d("login","ON load finished outside");
        newsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("login","ON LOAD FINISHED");
                if (isRefreshing) {
                    isRefreshing = false;
                    newsActivity.showToast("News successfully refreshed!");
                    ((BaseAdapter)slvNews.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }
}
