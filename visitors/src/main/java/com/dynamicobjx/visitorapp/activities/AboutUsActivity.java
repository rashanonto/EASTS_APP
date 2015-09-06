package com.dynamicobjx.visitorapp.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.models.Event;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by graciosa on 2/3/15.
 */
public class AboutUsActivity extends BaseActivity {

    @InjectView(R.id.tvEvent) TextView tvEvent;
    @InjectView(R.id.tvEventDate) TextView tvEventDate;
    @InjectView(R.id.tvAboutBody) TextView tvAboutUsBody;
    @InjectView(R.id.llBackground) LinearLayout llBackground;
    private static Bitmap bm;
//android:background="@drawable/aboutusbg"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ButterKnife.inject(this);
        initActionBar("About Us").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                animateToRight(AboutUsActivity.this);
            }
        });

        bm = trimDownImgReso(getResources(), R.drawable.aboutusbg,2);
        Drawable drawable = new BitmapDrawable(getResources(),bm);
        llBackground.setBackgroundDrawable(drawable);
        setFont(getTfSegoe(), tvAboutUsBody);
        setFont(getTfSegoe(),tvEventDate);
        Event event = getMyEvents().get(0);
        //tvEvent.setText(event.getTitle());
        tvEventDate.setText(event.getEventDate().toString());
        tvAboutUsBody.setText(event.getAboutUs());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bm.recycle();
        bm = null;
        finish();
        animateToRight(AboutUsActivity.this);
    }
}
