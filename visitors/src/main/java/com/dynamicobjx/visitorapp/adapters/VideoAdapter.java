package com.dynamicobjx.visitorapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.FullVideoActivity;
import com.dynamicobjx.visitorapp.activities.MediaActivity;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.ArrayList;

/**
 * Created by graciosa on 2/16/15.
 */
public class VideoAdapter extends BaseAdapter {

    private ArrayList<PhotoVideo> videoUrl;
    private Context context;
    private MediaActivity mediaActivity;
    private LayoutInflater inflater;

    public VideoAdapter(ArrayList<PhotoVideo> videoUrl, Context context) {
        this.context = context;
        this.videoUrl = videoUrl;
        this.inflater = LayoutInflater.from(context);
        this.mediaActivity = (MediaActivity)context;
    }

    public int getCount() {
        return videoUrl.size();
    }

    public PhotoVideo getItem(int i) {
        return videoUrl.get(i);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_items_videos,null);
            holder.ivMedia = (ImageView)convertView.findViewById(R.id.ivMedia);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.ivMedia.setImageResource(R.drawable.play);
        return convertView;
    }

    private class ViewHolder {
        ImageView ivMedia;
    }
}