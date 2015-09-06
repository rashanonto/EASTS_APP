package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.fragments.NewsFacebookFragment;
import com.dynamicobjx.visitorapp.fragments.NewsFragment;
import com.dynamicobjx.visitorapp.fragments.NewsTwitterFragment;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initActionBar("News").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(NewsActivity.this);
            }
        });

        NewsFragment newsFragment = NewsFragment.newInstance();
        NewsTwitterFragment newsTwitterFragment = NewsTwitterFragment.newInstance();
        NewsFacebookFragment facebookFragment = NewsFacebookFragment.newInstance();

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabName = new ArrayList<>();
        fragments.add(newsFragment);
        fragments.add(newsTwitterFragment);
        fragments.add(facebookFragment);
        tabName.add("News");
       // tabName.add("Twitter");
      //  tabName.add("Facebook");
        initActionBarTab(fragments, tabName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(NewsActivity.this);
    }


}
