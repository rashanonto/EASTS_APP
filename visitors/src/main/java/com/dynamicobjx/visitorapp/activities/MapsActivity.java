package com.dynamicobjx.visitorapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.Prefs;
import com.dynamicobjx.visitorapp.listeners.OnMapLoadListener;
import com.dynamicobjx.visitorapp.models.EventMap;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by rsbulanon on 2/10/15.
 */
public class MapsActivity extends BaseActivity implements OnMapLoadListener {

    @InjectView(R.id.ivMap) ImageView ivMap;
    @InjectView(R.id.llPleaseWait) LinearLayout llPleaseWait;
    private PhotoViewAttacher mAttacher;
    private static Bitmap[] bitmap = new Bitmap[getMyEventMaps().size()];
    private static Drawable[] map = new Drawable[getMyEventMaps().size()];
    private ArrayList<EventMap> eventMaps = new ArrayList<>();
    private int trimDown;
    private static int DEFAULT_SIZE = 650;
    private ArrayList<String> fileName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        ivMap.setImageDrawable(null);
        ArrayList<String> maps = new ArrayList<>();
        llPleaseWait.setVisibility(View.GONE);
        ivMap.setVisibility(View.VISIBLE);
        mAttacher = new PhotoViewAttacher(ivMap);
        for (EventMap map : getMyEventMaps()) {
            fileName.add(map.getFileName());
            maps.add(map.getFileName());
        }

        initActionBarWithSpinner("Map", maps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getMapSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //onMapPreLoad(i);
                llPleaseWait.setVisibility(View.VISIBLE);
                ivMap.setVisibility(View.GONE);
                Log.d("map", "on start");
                Glide.with(MapsActivity.this)
                        .load(getMyEventMaps().get(i).getMap().getUrl())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(ivMap) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                Log.d("map", "on map ready");
                                llPleaseWait.setVisibility(View.GONE);
                                ivMap.setVisibility(View.VISIBLE);
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onMapPreLoad(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAttacher = null;
                ivMap.setVisibility(View.GONE);
                llPleaseWait.setVisibility(View.VISIBLE);

                if (map[index] == null) {
                    trimDown = Prefs.getMyIntPref(MapsActivity.this,"trimDown");
                    eventMaps.get(index).getMap().getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                Log.d("map","bytes size -->  " + bytes.length);
                                if (bitmap[index] != null) {
                                    bitmap[index].recycle();
                                    bitmap[index] = null;
                                }
                                try {
                                    if (trimDown > 0) {
                                        DEFAULT_SIZE -= trimDown;
                                    }
                                    Log.d("trim","trim down -->> " + DEFAULT_SIZE);
                                    bitmap[index] = resizeImage(bytes,DEFAULT_SIZE,DEFAULT_SIZE);
                                    map[index] = new BitmapDrawable(getResources(),bitmap[index]);
                                    onLoadFinished(map[index]);
                                    Log.d("map","from cached");
                                } catch (OutOfMemoryError ex) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                    builder.setTitle("Map Preview");
                                    builder.setMessage("Having trouble while processing the map image. " +
                                            " We'll try to lower down the image resolution");
                                    builder.setPositiveButton("Lower down resolution",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (trimDown == 0) {
                                                trimDown += 30;
                                                Prefs.setMyIntPref(MapsActivity.this,"trimDown",trimDown);
                                            }
                                            dialogInterface.dismiss();
                                            onMapPreLoad(index);
                                        }
                                    });
                                    builder.show();
                                }
                            } else {
                                Log.d("map","error in getting map from cache -->  " + e.toString());
                            }
                        }
                    });
                } else {
                    onLoadFinished(map[index]);
                }
            }
        });
    }

    @Override
    public void onLoadFinished(final Drawable drawable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Prefs.setMyIntPref(MapsActivity.this,"trimDown",0);
                ivMap.setVisibility(View.VISIBLE);
                llPleaseWait.setVisibility(View.GONE);
                ivMap.setImageDrawable(drawable);
                mAttacher = new PhotoViewAttacher(ivMap);
                Log.d("map", "map successfully loaded");
            }
        });
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_maps,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optMapA:
                //new LoadMap().execute(0);
                onMapPreLoad(0);
                return true;
            case R.id.optMapB:
                //new LoadMap().execute(1);
                onMapPreLoad(1);
                return true;
            case R.id.optMapC:
                //new LoadMap().execute(2);
                onMapPreLoad(2);
                return true;
*//*            case R.id.optMapFull:
                //new LoadMap().execute(3);
                onMapPreLoad(3);
                return true;*//*
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        finish();
        animateToRight(MapsActivity.this);
    }

    private EventMap getMapByName(String name) {
        for (EventMap map : getMyEventMaps()) {
            if (map.getFileName().contains(name)) {
                return map;
            }
        }
        return null;
    }
}
