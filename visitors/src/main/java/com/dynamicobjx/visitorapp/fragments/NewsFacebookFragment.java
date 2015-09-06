package com.dynamicobjx.visitorapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.NewsActivity;
import com.dynamicobjx.visitorapp.adapters.FacebookFeedsAdapter;
import com.dynamicobjx.visitorapp.listeners.FacebookQueryListener;
import com.dynamicobjx.visitorapp.models.FacebookFeeds;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.quentindommerc.superlistview.SuperListview;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * Created by rsbulanon on 2/4/15.
 */
public class NewsFacebookFragment extends android.support.v4.app.Fragment {

    private SuperListview slvFBFeeds;
    private FacebookQueryListener facebookQueryListener;
    private ArrayList<FacebookFeeds> feeds;
    private NewsActivity newsActivity;
    private UiLifecycleHelper uiLifecycleHelper;
    private String nextPageUrl = "";
    private String officialPage;

    public static NewsFacebookFragment newInstance() {
        return new NewsFacebookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_facebook, null);
        slvFBFeeds = (SuperListview)view.findViewById(R.id.slvFBFeeds);
        newsActivity = ((NewsActivity) getActivity());
        feeds = new ArrayList<>();


        uiLifecycleHelper = new UiLifecycleHelper(getActivity(),callback);
        uiLifecycleHelper.onCreate(savedInstanceState);
        officialPage = newsActivity.getMyEvents().get(0).getOfficialFBPage();
        final Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile"))
                    .setCallback(callback));
        } else {
            Session.openActiveSession(getActivity(), true, callback);
        }

        facebookQueryListener = new FacebookQueryListener() {

            @Override
            public void onPreQuery(String action, Session session, String nextPageUrl) {
                Log.d("fb","this is called  -->  " + nextPageUrl);
                slvFBFeeds.showMoreProgress();
                readFeedFromPage(nextPageUrl, 10, action, session);
            }

            @Override
            public void onPreQuery(String action, Session session) {
                if (action.equals("init")) {
                    Log.d("fb","page --> " + officialPage);
                    newsActivity.showProgressDialog("Facebook feeds","Getting feeds from time line, Please wait...");
                    readFeedFromPage("/"+ officialPage +"/feed", 10, action, session);
                } else if (action.equals("refresh")) {
                    readFeedFromPage("/"+ officialPage  +"/feed", 10,action,session);
                }
            }

            @Override
            public void onQueryFinished(Response response, String action) {
                if (response != null) {
                    JSONObject json = response.getGraphObject().getInnerJSONObject();
                    Log.d("fbArr","json size --> " + json.length());
                    try {
                        JSONArray arr = json.getJSONArray("data");
                        JSONObject paging = json.getJSONObject("paging");
                        Log.d("paging","paging --> " + paging.toString());
                        DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();

                        if (action.equals("init") || action.equals("refresh")) {
                            feeds.clear();
                        }

                        for (int i = 0 ; i < arr.length() ; i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Log.d("fbArr","size obj --> " + obj.length());
                            String message = "";
                            String from = "";
                            String datePosted = "";
                            DateTime date;
                            String url = "";

                            for (int j = 0 ; j < obj.length() ; j++) {
                                if (obj.has("message")) {
                                    message = obj.getString("message");
                                    Log.d("fbArr",i + " --> not null ang message --> " + message);
                                    if (obj.has("from")) {
                                        JSONObject fromObj = obj.getJSONObject("from");
                                        from = fromObj.getString("name");
                                    }
                                    date = parser.parseDateTime(obj.getString("created_time"));
                                    datePosted = DateUtils.getRelativeTimeSpanString(date.toDate().getTime(),
                                            (new Date()).getTime(), DateUtils.HOUR_IN_MILLIS).toString();
                                    if (datePosted.equals("0 hours ago")) {
                                        datePosted = DateUtils.getRelativeTimeSpanString(date.toDate().getTime(),
                                                (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS).toString();
                                    }
                                    url = obj.getString("link");
                                    if (j == (obj.length() -1)) {
                                        feeds.add(new FacebookFeeds(message,from,datePosted,url));
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }

                        nextPageUrl = paging.getString("next");

                        if (action.equals("init")) {
                            newsActivity.dismissProgressDialog();
                            slvFBFeeds.setAdapter(new FacebookFeedsAdapter(getActivity(),feeds));
                            slvFBFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String url = feeds.get(i).getUrl();
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);
                                    newsActivity.animateToLeft(getActivity());
                                }
                            });
                        } else if (action.equals("load more")) {
                            newsActivity.showToast("Load more completed!");
                            slvFBFeeds.hideMoreProgress();
                            ((BaseAdapter)slvFBFeeds.getAdapter()).notifyDataSetChanged();
                        } else {
                            slvFBFeeds.getSwipeToRefresh().setRefreshing(false);
                            ((BaseAdapter)slvFBFeeds.getAdapter()).notifyDataSetChanged();
                            newsActivity.showToast("Refresh completed!");
                        }
                    } catch (JSONException ex){
                        newsActivity.dismissProgressDialog();
                        Log.d("user","json ex -->  " + ex.toString());
                    }
                }
            }
        };

        slvFBFeeds.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newsActivity.isNetworkAvailable()) {
                    facebookQueryListener.onPreQuery("refresh", session);
                } else {
                    newsActivity.showToast("Please turn on your WiFi/Data connection!");
                    slvFBFeeds.getSwipeToRefresh().setRefreshing(false);
                }
            }
        });

/*        slvFBFeeds.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int i, int i2, int i3) {
                Log.d("fb","must load more");
                facebookQueryListener.onPreQuery("load more", session, nextPageUrl);
            }
        },1);*/

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode,resultCode,data);
    }

    public void readFeedFromPage(String url, int limit, final String action, Session session) {
        Bundle bundle = new Bundle();
        bundle.putInt("limit", limit);
        new Request(session,url,
            bundle,HttpMethod.GET, new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                facebookQueryListener.onQueryFinished(response,action);
            }
        }).executeAsync();
    }

    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.d("fb", "Logged in...");
            facebookQueryListener.onPreQuery("init", session);
        } else if (state.isClosed()) {
            Log.d("fb", "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
}