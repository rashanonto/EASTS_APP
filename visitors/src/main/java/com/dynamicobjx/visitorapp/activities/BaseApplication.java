package com.dynamicobjx.visitorapp.activities;

import android.app.Application;
import android.util.Log;

import com.dynamicobjx.visitorapp.R;
import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by rashanonto on 9/4/2015.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getResources().getString(R.string.parid),
                getResources().getString(R.string.parkey));
        Log.d("parse", "parse successfully initialized!");
    }
}
