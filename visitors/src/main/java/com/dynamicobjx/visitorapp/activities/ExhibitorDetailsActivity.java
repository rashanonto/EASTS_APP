package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ExhibitorDetailsActivity extends BaseActivity {

    @InjectView(R.id.tvFav) TextView tvFav;
    @InjectView(R.id.tvExhibitorName) TextView tvExhibitorName;
    @InjectView(R.id.Vlogo) ImageView ivLogo;
    @InjectView(R.id.tvExhibitorDesc) TextView tvExhibitorDesc;
    @InjectView(R.id.llContacts) LinearLayout llContacts;
    @InjectView(R.id.llEmail) LinearLayout llEmail;
    @InjectView(R.id.llFax) LinearLayout llFax;
    @InjectView(R.id.llWeb) LinearLayout llWeb;
    @InjectView(R.id.tvBoothNo) TextView tvBoothNo;
    @InjectView(R.id.tvLocateOnMap) TextView tvLocateOnMap;
    @InjectView(R.id.llCategories) LinearLayout llCategories;
    @InjectView(R.id.llCompanyDesc) LinearLayout llCompanyDesc;
    @InjectView(R.id.tvSchedEvent) TextView tvSchedEvent;
    @InjectView(R.id.tvContactPerson) TextView tvContactPerson;
    @InjectView(R.id.tvAddress) TextView tvAddress;
    private LayoutInflater inflater;
    private Exhibitor exhibitor;
    private String lastScreen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_details);
        ButterKnife.inject(this);
        setFont(getTfSegoe(),tvExhibitorDesc);
        setFont(getTfSegoe(),tvBoothNo);
        setFont(getTfSegoe(),tvLocateOnMap);
        setFont(getTfSegoe(),tvSchedEvent);
        setFont(getTfSegoe(),tvContactPerson);
        setFont(getTfSegoe(),tvAddress);
        inflater = LayoutInflater.from(this);
        Bundle bundle = getIntent().getExtras();
        lastScreen = getIntent().getStringExtra("lastScreen");
        initActionBar("Exhibitor Details").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                animateToRight(ExhibitorDetailsActivity.this);
            }
        });
        exhibitor = bundle.getParcelable("exhibitor");
        if (exhibitor.getPackageType().equals("C")) {
            llCompanyDesc.setVisibility(View.GONE);
        } else {
            llCompanyDesc.setVisibility(View.VISIBLE);
        }
        tvExhibitorName.setText(exhibitor.getCompanyName());
        tvExhibitorDesc.setText(exhibitor.getDecsription());
        tvBoothNo.setText(exhibitor.getBoothNo());
        tvSchedEvent.setText(exhibitor.getScheduleEvent());
        tvAddress.setText(exhibitor.getAddress());
        tvContactPerson.setText(exhibitor.getContactPerson());
        setContactsLayout(llContacts);
        setEmailLayout(llEmail);
        setFaxLayout(llFax);
        setWebLayout(llWeb);
        Glide.with(this).load(exhibitor.getLogoUrl()).into(ivLogo);

        if (lastScreen.equals("Home")) {
            getMyBookMarks();
            if (!getMyFavorites().contains(exhibitor.getObjectId())) {
                ParseObject visitor = getVisitor().get(0);
                getMyFavorites().add(exhibitor.getObjectId());
                visitor.remove("myBookMarked");
                visitor.addAll("myBookMarked",getMyFavorites());
                visitor.saveEventually();
                exhibitor.setFavorite(true);
                tvFav.setText(getResources().getString(R.string.fa_star_empty));
                showToast(exhibitor.getCompanyName() + " successfully into your visits");
            } else {
                showToast(exhibitor.getCompanyName() + " already in your visits");
                exhibitor.setFavorite(false);
                tvFav.setText(getResources().getString(R.string.fa_star_full));
            }
            tvFav.setVisibility(View.GONE);
        } else {
            tvFav.setVisibility(View.VISIBLE);
            if (exhibitor.isFavorite()) {
                tvFav.setText(getResources().getString(R.string.fa_star_full));
            } else {
                tvFav.setText(getResources().getString(R.string.fa_star_empty));
            }
        }

        setFont(getTfBootstrap(),tvFav);
        tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FlipHorizontalAnimation(view)
                        .setDuration(500)
                        .setInterpolator(new DecelerateInterpolator())
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                boolean isFavorite = exhibitor.isFavorite();
                                isFavorite = !isFavorite;
                                ArrayList<String> toUpdate = new ArrayList<>();
                                ParseObject visitor = getVisitor().get(0);

                                if (isFavorite) {
                                    exhibitor.setFavorite(true);
                                    Log.d("myFave","update to true");
                                    tvFav.setText(getResources().getString(R.string.fa_star_full));
                                    visitor.addUnique("myBookMarked", exhibitor.getObjectId());
                                    getMyFavorites().add(exhibitor.getObjectId());
                                    visitor.saveEventually(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.d("myFave","successfully saved!");
                                            } else {
                                                Log.d("myFave","error while saving --> " + e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    exhibitor.setFavorite(false);
                                    Log.d("myFave","update to false");
                                    for (String s : getMyFavorites()) {
                                        if (!s.equals(exhibitor.getObjectId())) {
                                            toUpdate.add(s);
                                        }
                                    }
                                    tvFav.setText(getResources().getString(R.string.fa_star_empty));
                                    Log.d("myFave","to update -->  " + toUpdate.toString());
                                    visitor.remove("myBookMarked");
                                    visitor.addAll("myBookMarked", toUpdate);

                                    visitor.saveEventually(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.d("myFave","successfully saved!");
                                            } else {
                                                Log.d("myFave","error while saving --> " + e.toString());
                                            }
                                        }
                                    });
                                }
                            }
                        }).animate();
            }
        });
        setMenuFont();

        if (exhibitor.getCategories().size() > 0) {
            for (int i = 0 ; i < exhibitor.getCategories().size() ; i++) {
                llCategories.addView(setCategoriesLayout(
                        exhibitor.getCategories().get(i),exhibitor.getCategoriesId().get(i)));
            }
        } else {
            setCategoriesLayout("No categories found","");
        }

        tvLocateOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExhibitorDetailsActivity.this,MapsActivity.class));
                animateToRight(ExhibitorDetailsActivity.this);
                finish();
            }
        });
    }

    private void setMenuFont() {
        int[] iconIds = {R.id.tvIconContact,R.id.tvIconEmail,R.id.tvIconFax,R.id.tvIconWebsite,
                R.id.tvIconLocate,R.id.tvIconBoothNo,R.id.tvIconAddress};
        for (int i = 0  ; i < iconIds.length ; i++) {
            setFont(getTfBootstrap(),(TextView)findViewById(iconIds[i]));
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(ExhibitorDetailsActivity.this);
    }

    private void setContactsLayout(LinearLayout parent) {
        ArrayList<String> contacts = new ArrayList<>();
        if (exhibitor.getContactNo().contains(";")) {
            contacts.addAll(Arrays.asList(exhibitor.getContactNo().split(";")));
        } else {
            contacts.add(exhibitor.getContactNo());
        }
        for (String c : contacts) {
            TextView textView = (TextView)inflater.inflate(R.layout.row_exhibitor_details,null);
            setFont(getTfSegoe(),textView);
            textView.setText(c.trim());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = ((TextView)view).getText().toString();
                    if (number.equals(("Not available"))) {
                        showToast("number not available!");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                }
            });
            parent.addView(textView);
        }
    }

    private void setEmailLayout(LinearLayout parent) {
        ArrayList<String> email = new ArrayList<>();
        if (exhibitor.getEmail().contains(";")) {
            email.addAll(Arrays.asList(exhibitor.getEmail().split(";")));
        } else {
            email.add(exhibitor.getEmail());
        }
        for (String c : email) {
            TextView textView = (TextView)inflater.inflate(R.layout.row_exhibitor_details,null);
            setFont(getTfSegoe(),textView);
            textView.setText(c.trim());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = ((TextView)view).getText().toString();
                    if (email.equals(("Not available"))) {
                        showToast("Email not available!");
                    } else {
                        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(intent);
                    }
                }
            });
            parent.addView(textView);
        }
    }

    private void setFaxLayout(LinearLayout parent) {
        ArrayList<String> fax = new ArrayList<>();
        if (exhibitor.getFax().contains(";")) {
            fax.addAll(Arrays.asList(exhibitor.getFax().split(";")));
        } else {
            fax.add(exhibitor.getFax());
        }
        for (String c : fax) {
            TextView textView = (TextView)inflater.inflate(R.layout.row_exhibitor_details,null);
            setFont(getTfSegoe(),textView);
            textView.setText(c.trim());
            parent.addView(textView);
        }
    }

    private void setWebLayout(LinearLayout parent) {
        ArrayList<String> web = new ArrayList<>();
        if (exhibitor.getWebsite().contains(";")) {
            web.addAll(Arrays.asList(exhibitor.getWebsite().split(";")));
        } else {
            web.add(exhibitor.getWebsite());
        }
        for (String c : web) {
            TextView textView = (TextView)inflater.inflate(R.layout.row_exhibitor_details,null);
            setFont(getTfSegoe(),textView);
            textView.setText(c.trim());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = ((TextView)view).getText().toString();
                    if (url.equals(("Not available"))) {
                        showToast("Website not available!");
                    } else {
                        if (!url.contains("http://")) {
                            url = "http://" + url;
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                }
            });
            parent.addView(textView);
        }
    }

    private View setCategoriesLayout(final String category, final String categoryId) {
        View view = inflater.inflate(R.layout.dynamic_categories_layout, null);
        TextView tvTag = (TextView)view.findViewById(R.id.tvTagIcon);
        TextView tvNewCategory = (TextView)view.findViewById(R.id.tvNewCategory);
        setFont(getTfSegoe(),tvNewCategory);
        setFont(getTfBootstrap(), tvTag);
        tvNewCategory.setText(category);

        if (categoryId.equals("")) {
            tvTag.setVisibility(View.GONE);
        } else {
            tvNewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tvCat = (TextView)view;
                    ArrayList<Exhibitor> exhibitorBySelectedCategory = new ArrayList<>();
                    String selectedCategory = tvCat.getText().toString();

                    for (Exhibitor exhibitor : getExhibitorList()) {
                        ArrayList<String> exhibitorCategories = exhibitor.getCategories();
                        if (exhibitorCategories.contains(selectedCategory)) {
                            exhibitorBySelectedCategory.add(exhibitor);
                        }
                    }

                    sortArrayList(exhibitorBySelectedCategory);
                    Bundle bundle = new Bundle();
                    bundle.putString("lastScreen", selectedCategory);
                    bundle.putParcelableArrayList("exhibitorByCategory", exhibitorBySelectedCategory);
                    Intent intent = new Intent(ExhibitorDetailsActivity.this, ExhibitorsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    animateToLeft(ExhibitorDetailsActivity.this);
                    finish();
                }
            });
        }
        return view;
    }
}
