package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.dynamicobjx.visitorapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class TweetPreviewActivity extends BaseActivity {

    @InjectView(R.id.wvTweetPreview) WebView wvTweetPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_preview);
        ButterKnife.inject(this);
        String header = getIntent().getStringExtra("header");
        String url = getIntent().getStringExtra("url");
        initActionBar(header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(TweetPreviewActivity.this);
            }
        });
        Log.d("twitter", url + "  header -->  " + header);
        wvTweetPreview.getSettings().setJavaScriptEnabled(true);
        wvTweetPreview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(TweetPreviewActivity.this);
    }
}
