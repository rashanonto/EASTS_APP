package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.fragments.MediaPhotosFragment;
import com.dynamicobjx.visitorapp.fragments.VideosFragment;

import java.util.ArrayList;

/**
 * Created by graciosa on 2/11/15.
 */
public class MediaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initActionBar("Media").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });

        //PhotosFragment photosFragment = PhotosFragment.newInstance();
        MediaPhotosFragment photosFragment = MediaPhotosFragment.newInstance();
        VideosFragment videosFragment = VideosFragment.newInstance();

        ArrayList<Fragment> fragments = new ArrayList();
        ArrayList<String> tabName = new ArrayList();
        fragments.add(videosFragment);
        fragments.add(photosFragment);
        tabName.add("Videos");
        tabName.add("Photos");
        initActionBarTab(fragments, tabName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(MediaActivity.this);
    }
}
