package com.dynamicobjx.visitorapp.listeners;

import com.facebook.Response;
import com.facebook.Session;
import com.parse.ParseUser;

/**
 * Created by rsbulanon on 2/9/15.
 */
public interface FacebookQueryListener {

    public void onPreQuery(String action, Session session);

    public void onPreQuery(String action, Session session, String nextPageUrl);

    public void onQueryFinished(Response response, String action);
}
