package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.MyVisitsAdapter;
import com.dynamicobjx.visitorapp.models.Exhibitor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class MyVisitsActivity extends BaseActivity {

    @InjectView(R.id.shlvMyVisits) StickyListHeadersListView shlvMyVisits;
    @InjectView(R.id.llNoDataFound) LinearLayout llNoDataFound;
    @InjectView(R.id.tvNoData) TextView tvNoData;
    @InjectView(R.id.tvNoDataMsg) TextView tvNoDataMsg;
    private ArrayList<Exhibitor> myVisits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvisits);
        ButterKnife.inject(this);

        initActionBar("Exhibitors").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(MyVisitsActivity.this);
            }
        });

        getMyBookMarks();
        myVisits.clear();
        Log.d("vi","oncreate");

        for (String s : getMyFavorites()) {
            myVisits.add(getExhibitorById(s));
        }

        Log.d("visits","sizee --->  " + myVisits.size());
        if (myVisits.size() == 0) {
            llNoDataFound.setVisibility(View.VISIBLE);
            shlvMyVisits.setVisibility(View.GONE);
        } else {
            sortArrayList(myVisits);
            llNoDataFound.setVisibility(View.GONE);
            shlvMyVisits.setVisibility(View.VISIBLE);
            shlvMyVisits.setAdapter(new MyVisitsAdapter(this,myVisits));
        }

        shlvMyVisits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MyVisitsActivity.this, ExhibitorDetailsActivity.class);
                bundle.putString("lastScreen", "My Visits");
                myVisits.get(position).setFavorite(true);
                bundle.putParcelable("exhibitor", myVisits.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(MyVisitsActivity.this);
            }
        });

    }

    public LinearLayout getNoDataFound() {
        return llNoDataFound;
    }

    public StickyListHeadersListView getShlvMyVisits() {
        return shlvMyVisits;
    }

    @Override
    public void onBackPressed() {
        finish();
        animateToRight(MyVisitsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myVisits.clear();
        getMyBookMarks();
        Log.d("vi","onresume");
        for (String s : getMyFavorites()) {
            myVisits.add(getExhibitorById(s));
            Log.d("visits","my favorite --> " + s);
        }

        Log.d("visits","sizee --->  " + myVisits.size());
        if (myVisits.size() == 0) {
            llNoDataFound.setVisibility(View.VISIBLE);
            shlvMyVisits.setVisibility(View.GONE);
        } else {
            for (Exhibitor e : myVisits) {
                if (e.getCompanyName() != null) {
                    Log.d("visits", e.getCompanyName());
                } else {
                    Log.d("visits","null null null");
                }
            }
            sortArrayList(myVisits);
            llNoDataFound.setVisibility(View.GONE);
            shlvMyVisits.setVisibility(View.VISIBLE);
            //shlvMyVisits.setAdapter(new MyVisitsAdapter(this,myVisits));
            ((BaseAdapter)shlvMyVisits.getAdapter()).notifyDataSetChanged();
        }
    }
}