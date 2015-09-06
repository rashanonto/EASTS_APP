package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.ParseHelper;
import com.dynamicobjx.visitorapp.helpers.Prefs;
import com.dynamicobjx.visitorapp.listeners.OnLoadDataListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/11/15.
 */
public class SplashActivity extends BaseActivity {

    private ParseHelper parseHelper;
    private ArrayList<Integer> finishedQueriesCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        final Handler handler = new Handler();
        parseHelper = new ParseHelper();



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String visitorId = Prefs.getMyStringPrefs(SplashActivity.this,"visitor_id");
                if (ParseUser.getCurrentUser() == null || visitorId.length() < 1) {
                    resetLists();
                    Log.d("parse","splash null current user");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    animateToLeft(SplashActivity.this);
                    finish();
                } else {
                    Log.d("parse", "splash with user");
                    parseHelper.setOnLoadDataListener(new OnLoadDataListener() {
                        @Override
                        public void onPreLoad() {

                        }

                        @Override
                        public void onLoadFinished(final int counter) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finishedQueriesCount.add(counter);
                                    Log.d("login", "finished queries --> " + finishedQueriesCount.size());
                                    if (finishedQueriesCount.size() == 6) {
                                        finishedQueriesCount.clear();
                                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                        animateToLeft(SplashActivity.this);
                                        finish();
                                    }
                                }
                            });
                        }
                    });

                    boolean getFromCache = true;

                    if (isNetworkAvailable()) {
                        getFromCache = false;
                    }

                    Log.d("cache","get from cache -->  " + getFromCache);
                    parseHelper.getExhibitors(getExhibitorList(), getFromCache);
                    parseHelper.getNews(getNewsList(),getFromCache);
                    parseHelper.getMedia(getMyPhotoVidoes(),getFromCache);
                    parseHelper.getCategories(getCategoriesList() ,getFromCache);
                    parseHelper.getEvent(getMyEventSchedule(),getMyEvents(),getMyEventMaps(),getFromCache);
                    parseHelper.getVisitor(visitorId,getFromCache,getVisitor());
                }
            }
        }, 3000);
    }
}
