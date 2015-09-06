package com.dynamicobjx.visitorapp.listeners;

import android.graphics.drawable.Drawable;

/**
 * Created by rsbulanon on 3/5/15.
 */
public interface OnMapLoadListener {

    public void onMapPreLoad(int index);

    public void onLoadFinished(Drawable drawable);
}
