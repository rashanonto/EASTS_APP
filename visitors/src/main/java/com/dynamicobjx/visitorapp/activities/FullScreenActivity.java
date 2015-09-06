package com.dynamicobjx.visitorapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by graciosa on 2/17/15.
 */
public class FullScreenActivity extends BaseActivity {

    public static PhotoVideo photoVideo;
    private PhotoViewAttacher mAttacher;
    @InjectView(R.id.fullImage) ImageView fullImage;
    @InjectView(R.id.llPleaseWait) LinearLayout llPleaseWait;
    private static Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.inject(this);
        initActionBar("Photos").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fullImage.setImageDrawable(null);

/*        parseImageView.setParseFile(photoArraylist.get(position).getAsset().getData());
        parseImageView.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
            }
        });*/
        new LoadMap().execute();
    }

    private class LoadMap extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fullImage.setVisibility(View.GONE);
            llPleaseWait.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            byte[] bm = null;
            try {
                bm = photoVideo.getAsset().getData();
                Log.d("full","ok");
            } catch (ParseException ex) {
                Log.d("full","error --> " + ex.toString());
            }
            return resizeImage(bm,300,300);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            fullImage.setVisibility(View.VISIBLE);
            llPleaseWait.setVisibility(View.GONE);
            Drawable drawable = new BitmapDrawable(getResources(),bitmap);
            fullImage.setImageDrawable(drawable);
            mAttacher = new PhotoViewAttacher(fullImage);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        photoVideo = null;
        finish();
        animateToRight(FullScreenActivity.this);
    }

    public Bitmap resizeImage(byte[] data,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length,options);
    }
}
