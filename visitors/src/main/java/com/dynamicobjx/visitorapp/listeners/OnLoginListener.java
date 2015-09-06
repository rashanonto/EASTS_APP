package com.dynamicobjx.visitorapp.listeners;

import com.parse.ParseUser;

import bolts.Task;

/**
 * Created by rsbulanon on 2/3/15.
 */
public interface OnLoginListener {
    public void onPreLoginQuery();

    public void OnLoginFinished(Task<ParseUser> user);
}
