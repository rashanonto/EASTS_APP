package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.models.News;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by graciosa on 2/9/15.
 */
public class NewsDetailsActivity extends BaseActivity {

    @InjectView(R.id.txtHeadline) TextView txtHeadline;
    @InjectView(R.id.txtBody) TextView txtBody;
    @InjectView(R.id.txtDate) TextView txtDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMMM dd, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.inject(this);

        initActionBar("News Details").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                animateToRight(NewsDetailsActivity.this);
            }
        });
        setFont(getTfSegoe(),txtDate);
        setFont(getTfSegoe(),txtBody);
        Bundle bundle = getIntent().getExtras();
        News news = bundle.getParcelable("news");
        txtHeadline.setText(news.getHeadline());
        txtBody.setText(news.getBody());
        txtDate.setText(sdf.format(news.getPublishDate()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(NewsDetailsActivity.this);
    }
}
