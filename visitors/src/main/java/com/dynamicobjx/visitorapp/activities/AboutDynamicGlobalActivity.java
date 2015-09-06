package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 2/23/15.
 */
public class AboutDynamicGlobalActivity extends BaseActivity {

    @InjectView(R.id.tvAboutBody) TextView tvAboutUsBody;
    @InjectView(R.id.tvAboutInfo) TextView tvAboutInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_global);
        ButterKnife.inject(this);
        setFont(getTfSegoe(),tvAboutUsBody);
        setFont(getTfSegoe(),tvAboutInfo);

        initActionBar("Dynamic Global Soft Inc.").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(AboutDynamicGlobalActivity.this);
    }
}
