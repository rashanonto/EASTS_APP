package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.CategoriesAdapter;
import com.dynamicobjx.visitorapp.models.Exhibitor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by rsbulanon on 2/10/15.
 */
public class CategoriesActivity extends BaseActivity {

    @InjectView(R.id.slhCategories) StickyListHeadersListView slhCategories;
    private ArrayList<Exhibitor> exhibitors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.inject(this);
        String category = getIntent().getStringExtra("category");
        initActionBar(category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                animateToRight(CategoriesActivity.this);
            }
        });

        for (Exhibitor ex : getExhibitorList()) {
            if (ex.getCategories().size() > 0) {
                for (String cat : ex.getCategories()) {
                    if (cat.equals(category)) {
                        exhibitors.add(ex);
                    }
                }
            }
        }

        sortArrayList(exhibitors);
        slhCategories.setAdapter(new CategoriesAdapter(CategoriesActivity.this, exhibitors));
        slhCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Bundle bundle =new Bundle();
                bundle.putParcelable("exhibitor", exhibitors.get(i));
                bundle.putString("lastScreen", "Categories");
                Intent intent = new Intent(CategoriesActivity.this, ExhibitorDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(CategoriesActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
