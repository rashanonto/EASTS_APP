package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;

import java.util.ArrayList;

/**
 * Created by graciosa on 2/11/15.
 */
public class PhotosAdapter extends BaseAdapter{
    private ArrayList<PhotoVideo> photosArraylist;
    private Context context;

    public PhotosAdapter(ArrayList<PhotoVideo> photosArraylist, Context context) {
        this.context = context;
        this.photosArraylist = photosArraylist;
    }

    public int getCount() {
        return photosArraylist.size();
    }

    public Object getItem(int i) {
        return photosArraylist.get(i);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View convertView, ViewGroup parent) {

        ParseImageView imageView;

        if (convertView == null) {
            imageView = new ParseImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ParseImageView) convertView;
        }

        imageView.setParseFile(photosArraylist.get(i).getAsset());
        imageView.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

            }
        });
        //FullScreenActivity.photoArraylist = photosArraylist;
        return imageView;
    }
}
