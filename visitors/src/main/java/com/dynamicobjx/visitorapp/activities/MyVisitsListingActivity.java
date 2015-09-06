package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.MyVisitsListingAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 2/20/15.
 */
public class MyVisitsListingActivity extends BaseActivity {

    @InjectView(R.id.lvMyVisits) ListView lvMyVisits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvisits_listing);
        ButterKnife.inject(this);

        initActionBar("My Visits").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(MyVisitsListingActivity.this);
            }
        });
        ArrayList<String> icons = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> subHeaders = new ArrayList<>();

        icons.add(getResources().getString(R.string.fa_group));
        icons.add(getResources().getString(R.string.fa_cart));
        headers.add("Exhibitors");
        headers.add("Categories");
        subHeaders.add("Manage your exhibitors favorites");
        subHeaders.add("Manage your categories favorites");
        lvMyVisits.setAdapter(new MyVisitsListingAdapter(this, icons, headers, subHeaders));
        lvMyVisits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( i == 0) {
                    startActivity(new Intent(MyVisitsListingActivity.this,MyVisitsActivity.class));
                    animateToLeft(MyVisitsListingActivity.this);
                } else {
                    startActivity(new Intent(MyVisitsListingActivity.this,MyVisitsCategoriesActivity.class));
                    animateToLeft(MyVisitsListingActivity.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(MyVisitsListingActivity.this);
    }
}
