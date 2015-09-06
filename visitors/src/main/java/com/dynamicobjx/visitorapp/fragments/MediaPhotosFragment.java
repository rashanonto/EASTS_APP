package com.dynamicobjx.visitorapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.FullScreenActivity;
import com.dynamicobjx.visitorapp.activities.MediaActivity;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 3/5/15.
 */
public class MediaPhotosFragment extends android.support.v4.app.Fragment {

    private MediaActivity mediaActivity;
    private DisplayImageOptions options;
    private ArrayList<PhotoVideo> filteredPhotoArraylist = new ArrayList();

    public static MediaPhotosFragment  newInstance() {
        return new MediaPhotosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaActivity = (MediaActivity)getActivity();
        filteredPhotoArraylist.clear();

        for (PhotoVideo pv : mediaActivity.getMyPhotoVidoes()) {
            if (pv.getMediaType().equals("photo")) {
                filteredPhotoArraylist.add(pv);
            }
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_photo_grid,null);
        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mediaActivity.isNetworkAvailable()) {
                    FullScreenActivity.photoVideo = filteredPhotoArraylist.get(position);
                    startActivity(new Intent(getActivity(),FullScreenActivity.class));
                    mediaActivity.animateToLeft(getActivity());
                } else {
                    mediaActivity.showToast("Requires internet to view full screen!");
                }
            }

        });

        return view;
    }

    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public ImageAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return filteredPhotoArraylist.size();
        }

        @Override
        public PhotoVideo getItem(int position) {
            return filteredPhotoArraylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(filteredPhotoArraylist.get(i).getAsset().getUrl(), holder.imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }
}
