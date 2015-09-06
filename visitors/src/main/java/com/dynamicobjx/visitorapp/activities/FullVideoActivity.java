package com.dynamicobjx.visitorapp.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dynamicobjx.visitorapp.R;
import com.parse.ParseFile;

/**
 * Created by graciosa on 2/18/15.
 */
public class FullVideoActivity extends BaseActivity {

    private VideoView videoView;
    public static ParseFile pfVideo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);
        videoView = (VideoView) findViewById(R.id.vvFullVideo);
        initActionBar("Videos").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        new ViewVideo().execute();
    }

    private class ViewVideo extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Video","Loading video, Please wait...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            return pfVideo.getUrl();
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            Uri uri = Uri.parse(url);
            videoView.setVideoURI(uri);
            videoView.setMediaController(new MediaController(FullVideoActivity.this));
            videoView.requestFocus();
            videoView.start();
            dismissProgressDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pfVideo = null;
        finish();
        animateToRight(FullVideoActivity.this);
    }
}
