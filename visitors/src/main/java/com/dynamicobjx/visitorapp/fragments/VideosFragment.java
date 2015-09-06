package com.dynamicobjx.visitorapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.FullVideoActivity;
import com.dynamicobjx.visitorapp.activities.MediaActivity;
import com.dynamicobjx.visitorapp.adapters.VideoAdapter;
import com.dynamicobjx.visitorapp.models.PhotoVideo;

import java.util.ArrayList;

/**
 * Created by graciosa on 2/11/15.
 */
public class VideosFragment extends android.support.v4.app.Fragment {

    private MediaActivity mediaActivity;
    private GridView vidGridView;
    private ArrayList<PhotoVideo> videoUrlList = new ArrayList();
    public static VideosFragment  newInstance() {
        return new VideosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        mediaActivity = (MediaActivity)getActivity();
        vidGridView = (GridView)view.findViewById(R.id.videoGrid);

        Log.d("media","on new instance");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("media","activity created");
        new VideoURL().execute();
    }

    private class VideoURL extends AsyncTask<Void,Void,ArrayList<PhotoVideo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mediaActivity.showProgressDialog("Video","Loading video files, Please wait...");
        }

        @Override
        protected ArrayList<PhotoVideo> doInBackground(Void... voids) {
            videoUrlList.clear();
            for (PhotoVideo pv : mediaActivity.getMyPhotoVidoes()) {
                if (pv.getMediaType().equals("video")) {
                    videoUrlList.add(pv);
                }
            }
            return videoUrlList;
        }

        @Override
        protected void onPostExecute(final ArrayList<PhotoVideo> result) {
            super.onPostExecute(result);
            mediaActivity.dismissProgressDialog();
            vidGridView.setAdapter(new VideoAdapter(result,getActivity()));
            vidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(getActivity(), FullVideoActivity.class);
                    FullVideoActivity.pfVideo = videoUrlList.get(position).getAsset();
                    startActivity(intent);
                }
            });

        }
    }
}
