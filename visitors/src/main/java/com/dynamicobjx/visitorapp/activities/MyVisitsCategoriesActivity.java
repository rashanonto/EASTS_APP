package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.MyVisitCategoriesAdapter;
import com.dynamicobjx.visitorapp.adapters.ProductsFavoritesAdapter;
import com.dynamicobjx.visitorapp.models.Categories;
import com.dynamicobjx.visitorapp.models.Exhibitor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by rsbulanon on 2/20/15.
 */
public class MyVisitsCategoriesActivity extends BaseActivity {

    @InjectView(R.id.shlvMyVisits) StickyListHeadersListView shlvMyVisits;
    @InjectView(R.id.llNoDataFound) LinearLayout llNoDataFound;
    @InjectView(R.id.tvNoData) TextView tvNoData;
    @InjectView(R.id.tvNoDataMsg) TextView tvNoDataMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvisits);

        ButterKnife.inject(this);
        initActionBar("Categories").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(MyVisitsCategoriesActivity.this);
            }
        });

        getMyProductBookMarks();
        final ArrayList<Categories> myVisits = new ArrayList<>();
        for (String s : getMyProductFavorites()) {
            myVisits.add(getCategoryById(s));
        }

        Log.d("mb", "sssiiiiizzee --->   " + myVisits.size());
        if (myVisits.size() == 0) {
            llNoDataFound.setVisibility(View.VISIBLE);
            shlvMyVisits.setVisibility(View.GONE);
        } else {
            sortCategories(myVisits);
            llNoDataFound.setVisibility(View.GONE);
            shlvMyVisits.setVisibility(View.VISIBLE);
            shlvMyVisits.setAdapter(new ProductsFavoritesAdapter(this,myVisits));
        }

        final MyVisitCategoriesAdapter productsAdapter = new MyVisitCategoriesAdapter(this, myVisits);

        shlvMyVisits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Exhibitor> exhibitorBySelectedCategory = new ArrayList<>();
                String selectedCategory = productsAdapter.getItem(position).getName();

                for (Exhibitor exhibitor : getExhibitorList()) {
                    ArrayList<String> exhibitorCategories = exhibitor.getCategories();
                    if (exhibitorCategories.contains(selectedCategory)) {
                        exhibitorBySelectedCategory.add(exhibitor);
                        System.out.println(exhibitor.getCompanyName() + " has a " + selectedCategory + ". iadd mo yan sa Arraylist ng exhibitor!");
                    }
                }

                sortArrayList(exhibitorBySelectedCategory);
                Bundle bundle = new Bundle();
                //bundle.putString("selectedCategory", selectedCategory);
                bundle.putString("lastScreen", selectedCategory);
                bundle.putParcelableArrayList("exhibitorByCategory", exhibitorBySelectedCategory);
                Intent intent = new Intent(MyVisitsCategoriesActivity.this, ExhibitorsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(MyVisitsCategoriesActivity.this);
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
        animateToRight(MyVisitsCategoriesActivity.this);
    }
}
