package com.dynamicobjx.visitorapp.listeners;

import com.parse.ParseObject;

import java.util.List;

import bolts.Task;

/**
 * Created by rsbulanon on 2/4/15.
 */
public interface OnParseQueryListener {

    public void onPreQuery(String className);

    public void onQueryFinished(String className, Task<List<ParseObject>> result);

}
