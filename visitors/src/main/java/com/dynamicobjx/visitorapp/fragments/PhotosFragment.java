package com.dynamicobjx.visitorapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.FullScreenActivity;
import com.dynamicobjx.visitorapp.activities.MediaActivity;
import com.dynamicobjx.visitorapp.adapters.PhotosAdapter;
import com.dynamicobjx.visitorapp.listeners.OnParseQueryListener;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

/**
 * Created by graciosa on 2/11/15.
 */
public class PhotosFragment extends android.support.v4.app.Fragment implements OnParseQueryListener {

    private MediaActivity mediaActivity;
    private GridView gridView;
    private String objectId;
    private String fileName;
    private String mediaType;
    private ParseFile asset;

    private ArrayList<PhotoVideo> filteredPhotoArraylist = new ArrayList();

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        mediaActivity = (MediaActivity) getActivity();
        gridView = (GridView) view.findViewById(R.id.gridview);

        filteredPhotoArraylist.clear();
        for (PhotoVideo pv : mediaActivity.getMyPhotoVidoes()) {
            if (pv.getMediaType().equals("photo")) {
                filteredPhotoArraylist.add(pv);
            }
        }

        gridView.setAdapter(new PhotosAdapter(filteredPhotoArraylist, getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getActivity(), FullScreenActivity.class);
                intent.putExtra("positionkey", position);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onPreQuery(String className) {
        filteredPhotoArraylist.clear();
        mediaActivity.showProgressDialog("Media", "Getting media, Please wait...");
    }

    @Override
    public void onQueryFinished(String className, Task<List<ParseObject>> result) {

        for (ParseObject obj : result.getResult()) {
            objectId = obj.getObjectId();
            fileName = obj.getString("filename");
            mediaType = obj.getString("media_type");
            asset = obj.getParseFile("asset");
            PhotoVideo pv = new PhotoVideo(objectId, fileName, mediaType, asset);

            if (mediaType.equals("photo")) {
                filteredPhotoArraylist.add(pv);
            } else {
            }
        }
        mediaActivity.dismissProgressDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridView.setAdapter(new PhotosAdapter(filteredPhotoArraylist, getActivity()));
            }
        });
    }
}
