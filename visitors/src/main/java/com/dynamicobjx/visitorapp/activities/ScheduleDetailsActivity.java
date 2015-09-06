package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.models.EventSchedule;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 3/5/15.
 */
public class ScheduleDetailsActivity extends BaseActivity {

    @InjectView(R.id.llLayout) LinearLayout llLayout;
    @InjectView(R.id.tvEvent) TextView tvEvent;
    @InjectView(R.id.tvDate) TextView tvDate;
    @InjectView(R.id.tvVenue) TextView tvVenue;
    @InjectView(R.id.tvDescription) TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        ButterKnife.inject(this);
        initActionBar("Details").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setGroupFont(llLayout, getTfSegoe());
        EventSchedule sched = getIntent().getParcelableExtra("schedule");
        tvEvent.setText(sched.getTitle());
        tvDate.setText(getDateFormat().format(sched.getStartDate())+ " - " +
            getDateFormat().format(sched.getEndDate()));
        tvVenue.setText(sched.getRoomNumber());
        tvDescription.setText(sched.getDescription());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(this);
    }
}
