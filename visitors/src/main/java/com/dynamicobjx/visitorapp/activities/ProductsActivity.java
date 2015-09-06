package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.ProductsAdapter;
import com.dynamicobjx.visitorapp.models.Categories;
import com.dynamicobjx.visitorapp.models.Exhibitor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class ProductsActivity extends BaseActivity {

    private ArrayList<Categories> backUp = new ArrayList<>();
    @InjectView(R.id.llNoDataFound) LinearLayout llNoDataFound;
    @InjectView(R.id.productsList) StickyListHeadersListView stickyListHeadersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.inject(this);

        initActionBarWithSearch("Categories").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getMyProductBookMarks();
        sortCategories(getCategoriesList());

        for (Categories c : getCategoriesList()) {
            for (String s : getMyProductFavorites()) {
                if (s.equals(c.getObjectId())) {
                    c.setFavorite(true);
                }
            }
            ArrayList<Exhibitor> exhibitors = new ArrayList<>();
            for (Exhibitor e : getExhibitorList()) {
                if (e.getCategories().size() > 0) {
                    for (String exCat : e.getCategories()) {
                        if (exCat.equals(c.getName())) {
                            exhibitors.add(e);
                            break;
                        }
                    }
                }
            }
            sortArrayList(exhibitors);
            getCategoriesExhibitor().put(c.getName(),exhibitors);
        }

        backUp.clear();
        backUp.addAll(getCategoriesList());
        final ProductsAdapter productsAdapter = new ProductsAdapter(this, getCategoriesList());
        stickyListHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Exhibitor> exhibitorBySelectedCategory = new ArrayList<>();
                String selectedCategory = productsAdapter.getItem(position).getName();
                for (Exhibitor exhibitor : getExhibitorList()) {
                    ArrayList<String> exhibitorCategories = exhibitor.getCategories();
                    if (exhibitorCategories.contains(selectedCategory)) {
                        exhibitorBySelectedCategory.add(exhibitor);
                    }
                }

                sortArrayList(exhibitorBySelectedCategory);
                Bundle bundle = new Bundle();
                //bundle.putString("selectedCategory", selectedCategory);
                bundle.putString("lastScreen", selectedCategory);
                bundle.putParcelableArrayList("exhibitorByCategory", exhibitorBySelectedCategory);
                Intent intent = new Intent(ProductsActivity.this, ExhibitorsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(ProductsActivity.this);
            }
        });

        getEtSearch().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("filter",s.toString());
                ArrayList<Categories> cat = new ArrayList<>();
                if (s.toString().length() > 0) {
                    for (Categories c : backUp) {
                        if (c.getName().toLowerCase().startsWith(s.toString().toLowerCase())) {
                            cat.add(c);
                        }
                    }
                    getCategoriesList().clear();
                    getCategoriesList().addAll(cat);
                    if (getCategoriesList().size() == 0) {
                        llNoDataFound.setVisibility(View.VISIBLE);
                        stickyListHeadersListView.setVisibility(View.GONE);
                    } else {
                        llNoDataFound.setVisibility(View.GONE);
                        stickyListHeadersListView.setVisibility(View.VISIBLE);
                    }
                    productsAdapter.notifyDataSetChanged();
                } else {
                    llNoDataFound.setVisibility(View.GONE);
                    stickyListHeadersListView.setVisibility(View.VISIBLE);
                    getCategoriesList().clear();
                    getCategoriesList().addAll(backUp);
                    productsAdapter.notifyDataSetChanged();
                }
            }
        });

        getTvSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if (getEtSearch().isShown()) {
                    getEtSearch().setText("");
                    llNoDataFound.setVisibility(View.GONE);
                    stickyListHeadersListView.setVisibility(View.VISIBLE);
                    getCategoriesList().clear();
                    getCategoriesList().addAll(backUp);
                    productsAdapter.notifyDataSetChanged();
                    getEtSearch().setVisibility(View.INVISIBLE);
                    toggleSoftKeyboard(ProductsActivity.this,false);
                } else {
                    toggleSoftKeyboard(ProductsActivity.this,true);
                    getEtSearch().setVisibility(View.VISIBLE);
                    getEtSearch().setFocusable(true);
                    getEtSearch().requestFocus();
                }*/
            }
        });
        stickyListHeadersListView.setAdapter(productsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toggleSoftKeyboard(ProductsActivity.this,false);
        finish();
        getCategoriesList().clear();
        getCategoriesList().addAll(backUp);
        animateToRight(ProductsActivity.this);
    }
}
