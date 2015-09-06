package com.dynamicobjx.visitorapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.adapters.TwitterAdapter;
import com.dynamicobjx.visitorapp.helpers.TwitterHelper;
import com.dynamicobjx.visitorapp.models.TwitterFeeds;
import com.quentindommerc.superlistview.SuperListview;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class NewsTwitterFragment extends android.support.v4.app.Fragment {

    private TwitterHelper twitterHelper;
    private SuperListview slvTwitter;
    private LinearLayout llNoDataFound;
    private TextView tvNoDataMsg;
    private Button btnRefresh;
    private int metro_red;
    private int metro_blue;
    private int metro_orange;
    private int metro_purple;
    private ArrayList<TwitterFeeds> feeds;
    private int currPage = 1;
    private boolean isRefreshing = false;
    private NewsActivity newsActivity;
    private String hashTag = "";

    public static NewsTwitterFragment newInstance() {
        return new NewsTwitterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_twitter, null);
        slvTwitter = (SuperListview)view.findViewById(R.id.slvTwitter);
        llNoDataFound = (LinearLayout)view.findViewById(R.id.llNoDataFound);
        tvNoDataMsg = (TextView)view.findViewById(R.id.tvNoDataMsg);
        btnRefresh = (Button)view.findViewById(R.id.btnRefresh);
        metro_red = getActivity().getResources().getColor(R.color.metro_red);
        metro_blue = getActivity().getResources().getColor(R.color.metro_blue);
        metro_orange = getActivity().getResources().getColor(R.color.metro_orange);
        metro_purple = getActivity().getResources().getColor(R.color.metro_purple);
        twitterHelper = new TwitterHelper();
        newsActivity = (NewsActivity)getActivity();
        twitterHelper.initTwitter();
        feeds = new ArrayList<>();
        hashTag = newsActivity.getMyEvents().get(0).getOfficialHashTag();
        llNoDataFound.setVisibility(View.GONE);

        if (newsActivity.isNetworkAvailable()) {
            new HashTag().execute();
        } else {
            llNoDataFound.setVisibility(View.VISIBLE);
            slvTwitter.setVisibility(View.GONE);
        }

        slvTwitter.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newsActivity.isNetworkAvailable()) {
                    new RefreshTwitterFeeds().execute();
                } else {
                    newsActivity.showToast("Please turn on your WiFi/Data connection!");
                    slvTwitter.getSwipeToRefresh().setRefreshing(false);
                }
            }
        });

        slvTwitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("twitter","isRefreshing --> " + isRefreshing);
                if (!isRefreshing) {
                    String url = feeds.get(i).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    newsActivity.animateToLeft(getActivity());
                } else {
                    newsActivity.showToast("Refreshing items, Please wait...");
                }
            }
        });
        return view;
    }

    private class HashTag extends AsyncTask<Void,Void,List<twitter4j.Status>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newsActivity.showProgressDialog("Twitter", "Getting tweets for " +
                    hashTag + " , Please wait...");
        }

        @Override
        protected List<twitter4j.Status> doInBackground(Void... voids) {
            List<twitter4j.Status> tweets = null;
            try {
                String[] tags = hashTag.split(" ");
                StringBuilder builder = new StringBuilder();
                for (int i = 0 ; i < tags.length ; i++) {
                    if (i == (tags.length - 1)) {
                        builder.append(tags[i]);
                    } else {
                        builder.append(tags[i]+" OR ");
                    }
                }
                Log.d("twitter","query --> " + builder.toString());
                Query query = new Query(builder.toString());
                QueryResult result = twitterHelper.getTwitter().search(query);
                tweets = result.getTweets();
            } catch (TwitterException te) {
                Log.d("twitter","error twitter --> " + te.toString());
            }
            return tweets;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            feeds.clear();
            newsActivity.dismissProgressDialog();
            if (statuses.size() > 0) {
                Log.d("twitter","size -->  " + statuses.size());
                for (twitter4j.Status s : statuses) {
                    feeds.add(new TwitterFeeds(s.getUser().getName(), s.getText(), s.getCreatedAt(),
                            "http://twitter.com/"+s.getUser().getId()+"/status/"+s.getId()));
                }
                llNoDataFound.setVisibility(View.GONE);
                slvTwitter.setVisibility(View.VISIBLE);
                slvTwitter.setAdapter(new TwitterAdapter(getActivity(), feeds));
            } else {
                slvTwitter.setVisibility(View.GONE);
                llNoDataFound.setVisibility(View.VISIBLE);
                tvNoDataMsg.setText(Html.fromHtml("No tweets result found for hash tag " +
                        "<font color='#3399ff'>" + hashTag + "</font>"));
                btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new HashTag().execute();
                    }
                });
            }
        }
    }

    private class RefreshTwitterFeeds extends AsyncTask<Void, Void, List<Status>> {

        @Override
        protected void onPreExecute() {
            isRefreshing = true;
            super.onPreExecute();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(Void... arg) {
            List<twitter4j.Status> tweets = null;
            try {
                String[] tags = hashTag.split(" ");
                StringBuilder builder = new StringBuilder();
                for (int i = 0 ; i < tags.length ; i++) {
                    if (i == (tags.length - 1)) {
                        builder.append(tags[i]);
                    } else {
                        builder.append(tags[i]+" OR ");
                    }
                }
                Log.d("twitter","query --> " + builder.toString());
                Query query = new Query(builder.toString());
                QueryResult result = twitterHelper.getTwitter().search(query);
                tweets = result.getTweets();
            } catch (TwitterException te) {
                Log.d("twitter","error twitter --> " + te.toString());
            }
            return tweets;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            feeds.clear();
            Log.d("twitter", "size --> " + statuses.size());
            for (twitter4j.Status s : statuses) {
                Log.d("twitter","http://twitter.com/"+s.getUser().getId()+"/status/"+s.getId());
                feeds.add(new TwitterFeeds(s.getUser().getName(), s.getText(), s.getCreatedAt(),
                        "http://twitter.com/"+s.getUser().getId()+"/status/"+s.getId()));
            }
            isRefreshing = false;
            ((BaseAdapter)slvTwitter.getAdapter()).notifyDataSetChanged();
            slvTwitter.getSwipeToRefresh().setRefreshing(false);
            newsActivity.showToast("Refresh completed!");
        }
    }

    private class LoadMoreTwitterFeeds extends AsyncTask<Void, Void, List<Status>> {

        private int page;

        public LoadMoreTwitterFeeds(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            slvTwitter.showMoreProgress();
            super.onPreExecute();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(Void... arg) {
            Paging paging = new Paging();
            paging.setPage(page);
            ResponseList<twitter4j.Status> status = null;
            try {
                status = twitterHelper.getTwitter().getHomeTimeline(paging);
            } catch (TwitterException ex) {
                Log.d("twitter", "twitter exception -->  " + ex.toString());
            }
            return status;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            Log.d("twitter", "size --> " + statuses.size());
            for (twitter4j.Status s : statuses) {
                Log.d("twitter","http://twitter.com/"+s.getUser().getId()+"/status/"+s.getId());
                feeds.add(new TwitterFeeds(s.getUser().getName(), s.getText(), s.getCreatedAt(),
                        "http://twitter.com/"+s.getUser().getId()+"/status/"+s.getId()));
            }
            ((BaseAdapter)slvTwitter.getAdapter()).notifyDataSetChanged();
            newsActivity.showToast("Load more completed!");
            slvTwitter.hideMoreProgress();
        }
    }
}
