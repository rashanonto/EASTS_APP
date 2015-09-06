package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.ExhibitorsAdapter;

import com.dynamicobjx.visitorapp.adapters.SortedByPackageAdapter;
import com.dynamicobjx.visitorapp.models.Exhibitor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ExhibitorsActivity extends BaseActivity {

    private String titleBar = "Exhibitors";
    private ArrayList<Exhibitor> exhibitors;
    private ArrayList<Exhibitor> backUp = new ArrayList<>();
    private ExhibitorsAdapter exhibitorsAdapter;
    @InjectView(R.id.tvNoData) TextView tvNoData;
    @InjectView(R.id.lvSorted) ListView lvSorted;
    @InjectView(R.id.llNoDataFound) LinearLayout llNoDataFound;
    @InjectView(R.id.exhibitorsList) StickyListHeadersListView exhibitorsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitors_list);
        ButterKnife.inject(this);
        Bundle bundle;
        exhibitors = new ArrayList<>();

        if (getIntent() != null && getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            titleBar = bundle.getString("lastScreen");
            exhibitors = bundle.getParcelableArrayList("exhibitorByCategory");
        } else {
            titleBar = "Exhibitors";
            exhibitors.clear();
            exhibitors.addAll(getExhibitorList());
            sortArrayList(exhibitors);
        }

        initActionBarWithSearch("Exhibitors").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvNoData.setText("No matches found!");
        getEtSearch().setHint("Search for exhibitor");

        getMyBookMarks();
        for (Exhibitor e : exhibitors) {
            e.setFavorite(false);
        }

        for (int i = 0; i < exhibitors.size(); i++) {
            Exhibitor exhibitor = exhibitors.get(i);
            if (getMyFavorites().contains(exhibitor.getObjectId())) {
                exhibitors.get(i).setFavorite(true);
            }
        }

        backUp.clear();
        backUp.addAll(exhibitors);
        if (titleBar.equals("Exhibitors")) {
            lvSorted.setVisibility(View.GONE);
            exhibitorsList.setVisibility(View.VISIBLE);
            exhibitorsAdapter = new ExhibitorsAdapter(ExhibitorsActivity.this, exhibitors);
            exhibitorsList.setAdapter(exhibitorsAdapter);
            exhibitorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("lastScreen", "Exhibitors");
                    bundle.putParcelable("exhibitor", exhibitors.get(i));
                    Intent intent = new Intent(ExhibitorsActivity.this, ExhibitorDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    animateToLeft(ExhibitorsActivity.this);
                }
            });
        } else {
            sortByPackageType(exhibitors);
            Log.d("sorted","sort --> " + exhibitors.size());
            lvSorted.setVisibility(View.VISIBLE);
            exhibitorsList.setVisibility(View.GONE);
            lvSorted.setAdapter(new SortedByPackageAdapter(ExhibitorsActivity.this,exhibitors));
            lvSorted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("lastScreen", "Exhibitors");
                    bundle.putParcelable("exhibitor", exhibitors.get(i));
                    Intent intent = new Intent(ExhibitorsActivity.this, ExhibitorDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    animateToLeft(ExhibitorsActivity.this);
                }
            });
        }

        if (exhibitors.size() == 0) {
            llNoDataFound.setVisibility(View.VISIBLE);
            exhibitorsList.setVisibility(View.GONE);
            lvSorted.setVisibility(View.GONE);
        } else {
            if (titleBar.equals("Exhibitors")) {
                exhibitorsList.setVisibility(View.VISIBLE);
                lvSorted.setVisibility(View.GONE);
            } else {
                lvSorted.setVisibility(View.VISIBLE);
                exhibitorsList.setVisibility(View.GONE);
            }
            llNoDataFound.setVisibility(View.GONE);
        }

        getEtSearch().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Exhibitor> exhi = new ArrayList<>();

                if (s.toString().length() > 0) {
                    for (Exhibitor e : backUp) {
                        if (e.getCompanyName().toLowerCase().startsWith(s.toString().toLowerCase())) {
                            exhi.add(e);
                        }
                    }
                    Log.d("filter","exhi size --> " + exhi.size());

                    exhibitors.clear();
                    exhibitors.addAll(exhi);

                    if (exhibitors.size() == 0) {
                        llNoDataFound.setVisibility(View.VISIBLE);
                        exhibitorsList.setVisibility(View.GONE);
                        lvSorted.setVisibility(View.GONE);
                    } else {
                        llNoDataFound.setVisibility(View.GONE);
                        if (titleBar.equals("Exhibitors")) {
                            exhibitorsList.setVisibility(View.VISIBLE);
                            lvSorted.setVisibility(View.GONE);
                        } else {
                            exhibitorsList.setVisibility(View.GONE);
                            lvSorted.setVisibility(View.VISIBLE);
                        }
                    }
                    if (titleBar.equals("Exhibitors")) {
                        ((BaseAdapter)exhibitorsList.getAdapter()).notifyDataSetChanged();
                    } else {
                        ((BaseAdapter)lvSorted.getAdapter()).notifyDataSetChanged();
                    }
                } else {
                    llNoDataFound.setVisibility(View.GONE);
                    exhibitors.clear();
                    exhibitors.addAll(backUp);

                    if (titleBar.equals("Exhibitors")) {
                        exhibitorsList.setVisibility(View.VISIBLE);
                        lvSorted.setVisibility(View.GONE);
                        ((BaseAdapter)exhibitorsList.getAdapter()).notifyDataSetChanged();
                    } else {
                        exhibitorsList.setVisibility(View.GONE);
                        lvSorted.setVisibility(View.VISIBLE);
                        ((BaseAdapter)lvSorted.getAdapter()).notifyDataSetChanged();
                    }
                }
            }
        });

        getTvSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if (getEtSearch().isShown()) {
                    getEtSearch().setText("");
                    llNoDataFound.setVisibility(View.GONE);
                    exhibitors.clear();
                    exhibitors.addAll(backUp);

                    if (titleBar.equals("Exhibitors")) {
                        exhibitorsList.setVisibility(View.VISIBLE);
                        lvSorted.setVisibility(View.GONE);
                        ((BaseAdapter)exhibitorsList.getAdapter()).notifyDataSetChanged();
                    } else {
                        exhibitorsList.setVisibility(View.GONE);
                        lvSorted.setVisibility(View.VISIBLE);
                        ((BaseAdapter)lvSorted.getAdapter()).notifyDataSetChanged();
                    }
                    getEtSearch().setVisibility(View.INVISIBLE);
                    toggleSoftKeyboard(ExhibitorsActivity.this,false);
                } else {
                    toggleSoftKeyboard(ExhibitorsActivity.this,true);
                    getEtSearch().setVisibility(View.VISIBLE);
                    getEtSearch().setFocusable(true);
                    getEtSearch().requestFocus();
                }*/
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exhibitors.clear();
        exhibitors.addAll(backUp);
        finish();
        //finish();
        //startActivity(new Intent(ExhibitorsActivity.this, HomeActivity.class));
        animateToRight(ExhibitorsActivity.this);
    }

    @Override
    protected void onResume() {
        for (int i = 0; i < exhibitors.size(); i++) {
            Exhibitor exhibitor = exhibitors.get(i);
            if (getMyFavorites().contains(exhibitor.getObjectId())) {
                System.out.println("contains!");
                exhibitors.get(i).setFavorite(true);
            } else {
                exhibitors.get(i).setFavorite(false);
            }
        }
        if (titleBar.equals("Exhibitors")) {
            if (exhibitorsList.getAdapter() != null) {
                ((BaseAdapter)exhibitorsList.getAdapter()).notifyDataSetChanged();
            }
        } else {
            if (lvSorted.getAdapter() != null) {
                ((BaseAdapter)lvSorted.getAdapter()).notifyDataSetChanged();
            }
        }
        System.out.println("onresume!");
        super.onResume();
    }
}
