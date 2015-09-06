package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.SchedulesAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class ScheduleActivity extends BaseActivity {

    @InjectView(R.id.slhSchedules) StickyListHeadersListView slhSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);
        initActionBar("Schedule").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(ScheduleActivity.this);
            }
        });
        slhSchedules.setAdapter(new SchedulesAdapter(ScheduleActivity.this, getMyEventSchedule()));
        slhSchedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("schedule",getMyEventSchedule().get(i));
                Intent intent = new Intent(ScheduleActivity.this,ScheduleDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(ScheduleActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(ScheduleActivity.this);
    }
}
