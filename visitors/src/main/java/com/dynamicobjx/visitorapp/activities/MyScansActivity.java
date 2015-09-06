package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.MyScansAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class MyScansActivity extends BaseActivity {

    @InjectView(R.id.lvMyScans) ListView lvMyScans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscans);
        ButterKnife.inject(this);
        initActionBar("My Scans").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            animateToRight(MyScansActivity.this);
            }
        });

        showProgressDialog("My Scans","Getting your scans, Please wait...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("VisitorScan");
        query.whereEqualTo("visitorId",ParseObject.createWithoutData("Visitor", ParseUser.getCurrentUser().getObjectId()));

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                dismissProgressDialog();
                if (e == null) {
                    Log.d("load", "ok size -->  " + objects.size());
                    lvMyScans.setAdapter(new MyScansAdapter(MyScansActivity.this,(ArrayList)objects));
                } else {
                    Log.d("load", "error --> " + e.toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(MyScansActivity.this);
    }
}
