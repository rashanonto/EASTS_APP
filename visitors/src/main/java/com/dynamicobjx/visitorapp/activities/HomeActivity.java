package com.dynamicobjx.visitorapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.Prefs;
import com.dynamicobjx.visitorapp.helpers.ZxingHelper;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rsbulanon on 2/3/15.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.ivLogo) ImageView ivLogo;
    @InjectView(R.id.tvEvDate) TextView tvEvDate;

    private static Bitmap bitmap;
    private Exhibitor exhibitor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initActionBar();


        getllQrScanner().setOnClickListener(this);
        getllMyCode().setOnClickListener(this);
        setMenuFont();
        setFont(getTfSegoe(),tvEvDate);

        if (bitmap == null) {
            bitmap = trimDownImgReso(getResources(), R.drawable.smart_logo, 850, 850);
        }
        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
        ivLogo.setImageDrawable(drawable);

        ivLogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("exhibitor", getExhibitorById("xRiMaAl5J7"));
                bundle.putString("lastScreen", "Exhibitors");
                Intent intent = new Intent(HomeActivity.this, ExhibitorDetailsActivity.class);
               // bundle.putString("lastScreen", "xRiMaAl5J7");
                intent.putExtras(bundle);

                startActivity(intent);
                animateToLeft(HomeActivity.this);
            }

        });
        getTvOptionsMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsMenu();
            }
        });
/*        ParsePush.subscribeInBackground(Prefs.getMyStringPrefs(this, "visitor_id") + "user", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("push", "failed to subscribe for push", e);
                }
            }
        });
        ParseInstallation.getCurrentInstallation().saveInBackground(); */
    }

    private void setMenuFont() {
        int[] ids = {R.id.tvMenuMyVisit, R.id.tvMenuAboutUs, R.id.tvMenuProducts, R.id.tvMenuExhibit,
                    R.id.tvMenuSchedule,R.id.tvMenuMaps,R.id.tvMenuReg,R.id.tvMenuNews};
        int[] btnIds = {R.id.tvMyVisit,R.id.tvAboutUs,R.id.tvProducts,R.id.tvExhibitors,R.id.tvSchedule,
                    R.id.tvMaps,R.id.tvReg,R.id.tvNews};
        for (int i = 0  ; i < ids.length ; i++) {
            setFont(getTfSegoe(),(TextView)findViewById(ids[i]));
            setFont(getTfBootstrap(),(TextView)findViewById(btnIds[i]));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("app","must destory app");
        finish();
    }

    @OnClick(R.id.tvMaps)
    public void viewMaps() {
        startActivity(new Intent(HomeActivity.this,MapsActivity.class));
        animateToLeft(HomeActivity.this);
      }

    @OnClick(R.id.tvReg)
    public void viewMedia() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dynamicglobalsoft.com/easts2015/program/"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.tvMyVisit)
    public void myVisits() {
        startActivity(new Intent(HomeActivity.this, MyVisitsListingActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @OnClick(R.id.tvNews)
    public void viewNews() {
        startActivity(new Intent(HomeActivity.this, NewsActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @OnClick(R.id.tvExhibitors)
    public void viewExhibitors() {
        startActivity(new Intent(HomeActivity.this, ExhibitorsActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @OnClick(R.id.tvAboutUs)
    public void viewAboutUs() {
        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @OnClick(R.id.tvProducts)
    public void viewProductsList() {
        startActivity(new Intent(HomeActivity.this, ProductsActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @OnClick(R.id.tvSchedule)
    public void viewSchedules() {
        startActivity(new Intent(HomeActivity.this, ScheduleActivity.class));
        animateToLeft(HomeActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optAbout:
                startActivity(new Intent(HomeActivity.this, AboutDynamicGlobalActivity.class));
                animateToLeft(HomeActivity.this);
                return true;
            case R.id.optReview:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_review,null);
                Button btnSendReview = (Button)view.findViewById(R.id.btnSend);
                final EditText etReview = (EditText)view.findViewById(R.id.etReview);

                btnSendReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNetworkAvailable()) {
                            if (etReview.getText().toString().isEmpty()) {
                                showToast("Please write your review!");
                            } else if (etReview.getText().toString().length() < 10) {
                                showToast("Review must be greater than 10 characters!");
                            } else {
                                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Review of the Worldbex Application (Android)");
                                intent.putExtra(android.content.Intent.EXTRA_TEXT,etReview.getText().toString());
                                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {
                                        "info@dynamicglobalsoft.com","info.worldbex@gmail.com"});
                                startActivity(intent);
                            }
                        } else {
                            showToast("Please enable your WiFi/mobile data connection!");
                        }
                    }
                });
                alert.setView(view);
                alert.show();
                return true;
            case R.id.optLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure you want to exit from the app?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetLists();
                        finish();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        animateToRight(HomeActivity.this);
                        showToast("You have been successfully logged out!");
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llQrScanner:
                ZxingHelper.openScanner(HomeActivity.this);
                break;
            case R.id.llMyCode:
                startActivity(new Intent(HomeActivity.this,MyQRActivity.class));
                animateToLeft(HomeActivity.this);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String email = "";
        if (resultCode == RESULT_OK) {
            String scan = data.getStringExtra("SCAN_RESULT");

            try {
                JSONObject json = new JSONObject(scan);
                email = json.getString("email");
                proceedWithScanning(email);
            } catch (JSONException e) {
                try {
                    JSONObject json = new JSONObject(scan);
                    email = json.getString("e");
                    proceedWithScanning(email);
                } catch (JSONException ex) {
                    showToast("Invalid QR format! Please try again!");
                }
            }
        }
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void proceedWithScanning(final String email) {
        showProgressDialog("Scan","Scanning, Please wait...");
        Log.d("scan","email ---> " + email);
        final Exhibitor ex = getExhibitorByEmail(email);
        final String visitorId = Prefs.getMyStringPrefs(HomeActivity.this,"object_id");

        /* exhibitor found in local data */
        if (ex != null) {
            Log.d("scan","exhibitor is not null visitorId --> " + visitorId);
            ParseObject obj = new ParseObject("VisitorScan");
            obj.put("visitorId", ParseObject.createWithoutData("Visitor",visitorId));
            obj.put("exhibitorId", ParseObject.createWithoutData("Exhibitor", ex.getObjectId()));
            obj.put("email",email);
            obj.saveEventually();
            dismissProgressDialog();
            Log.d("scan","company found --> " + ex.getCompanyName());
            Bundle bundle = new Bundle();
            bundle.putString("lastScreen","Home");
            bundle.putParcelable("exhibitor", ex);
            Intent intent = new Intent(HomeActivity.this, ExhibitorDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            animateToLeft(HomeActivity.this);
        } else {
            if (!isNetworkAvailable()) {
                dismissProgressDialog();
                showDialog("Scan","You dont have an internet connection as of the moment." +
                        "Exhibitor not found in your cached list of Registered Exhibitor!");
/*                ParseObject obj = new ParseObject("VisitorScan");
                obj.put("visitorId", ParseObject.createWithoutData("Visitor",visitorId));
                obj.put("email",email);
                obj.saveEventually();
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                bundle.putString("lastScreen","Home");
                bundle.putParcelable("exhibitor", ex);
                Intent intent = new Intent(HomeActivity.this, ExhibitorDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                animateToLeft(HomeActivity.this);*/
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Exhibitor");
                query.whereEqualTo("email",email);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            ParseObject obj = new ParseObject("VisitorScan");
                            obj.put("visitorId", ParseObject.createWithoutData("Visitor",visitorId));
                            obj.put("exhibitorId",parseObject);
                            obj.put("email",email);
                            obj.saveEventually();
                            dismissProgressDialog();
                            Log.d("scan","successful");
                            for (Exhibitor ex : getExhibitorList()) {
                                if (ex.getCompanyName().equals(parseObject.getString("company_name"))) {
                                    Log.d("scan","company found --> " + ex.getCompanyName());
                                    Bundle bundle =new Bundle();
                                    bundle.putString("lastScreen","Home");
                                    bundle.putParcelable("exhibitor", ex);
                                    Intent intent = new Intent(HomeActivity.this, ExhibitorDetailsActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    animateToLeft(HomeActivity.this);
                                }
                            }
                            showToast("Scan successfully finished!");
                        } else {
/*                            String visitorId = Prefs.getMyStringPrefs(HomeActivity.this,"object_id");
                            ParseObject obj = new ParseObject("VisitorScan");
                            obj.put("visitorId", ParseObject.createWithoutData("Visitor",visitorId));
                            obj.put("email", email);
                            obj.saveEventually();*/
                            dismissProgressDialog();
                            showDialog("Scan", "Scan successfully finished, but exhibitor not found in Registered Exhibitor List");
                        }
                    }
                });
            }
        }
    }


}
