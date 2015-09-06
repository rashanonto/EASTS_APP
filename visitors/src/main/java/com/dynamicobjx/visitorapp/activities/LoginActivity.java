package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.ParseHelper;
import com.dynamicobjx.visitorapp.helpers.Prefs;
import com.dynamicobjx.visitorapp.listeners.OnLoadDataListener;
import com.dynamicobjx.visitorapp.listeners.OnLoginListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;

import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements OnLoginListener {

    private ParseHelper parseHelper;
    @InjectView(R.id.etUsername) EditText etUsername;
    @InjectView(R.id.etPassword) EditText etPassword;
    @InjectView(R.id.llLogin) LinearLayout llLogin;

    @InjectView(R.id.ivLogo) ImageView ivLogo;
    private ArrayList<Integer> finishedQueriesCount = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initFonts();
        parseHelper = new ParseHelper();
        parseHelper.setOnLoginListener(this);



        Bitmap bitmap = trimDownImgReso(getResources(), R.drawable.logo, 1000, 1000);
        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
        ivLogo.setImageDrawable(drawable);
    }
    @Override
    public void onPreLoginQuery() {
        Log.d("login", "on pre login query");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressDialog("Login", "Authenticating user, Please wait...");
                parseHelper.authenticateUser(etUsername.getText().toString(),etPassword.getText().toString());
            }
        });
    }
    @Override
    public void OnLoginFinished(Task<ParseUser> user) {
        if (user.isFaulted()) {
            dismissProgressDialog();
            Log.d("login", "faulted");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast("Either username or password is incorrect!");
                }
            });
        } else if (user.isCancelled()) {
            Log.d("login","cancelled");
        } else {
            if (user != null) {
                if (Prefs.getMyStringPrefs(this,"fname").length() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            constructJSON(ParseUser.getCurrentUser());
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            animateToLeft(LoginActivity.this);
                            finish();
                            Log.d("login","login ok else ");
                        }
                    });
                }
            } else {
                Log.d("login","parse user is null");
            }
        }
    }
    @OnClick(R.id.forget)
    public void asd() {
        startActivity(new Intent(LoginActivity.this,ForgotPasswordActivty.class));
        animateToLeft(LoginActivity.this);
    }
    @OnClick(R.id.btnLogin)
    public void authenticateUser() {
        if (etUsername.getText().toString().isEmpty()) {
            showToast("Please enter your username!");
        } else if (etPassword.getText().toString().isEmpty()) {
            showToast("Please enter your password!");
        } else {
            /*startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            animateToLeft(LoginActivity.this);*/
            if (isNetworkAvailable()) {
                showProgressDialog("Login","Authenticating user, Please wait...");
                ParseUser.logInInBackground(etUsername.getText().toString(),etPassword.getText().toString()
                ,new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e == null) {
                            if (parseUser.getBoolean("emailVerified")) {
                                if (Prefs.getMyStringPrefs(LoginActivity.this,"fname").length() == 0) {
                                    constructJSON(ParseUser.getCurrentUser());
                                } else {
                                    dismissProgressDialog();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    animateToLeft(LoginActivity.this);
                                    finish();
                                    Log.d("login","login ok else ");
                                }
                            } else {
                                dismissProgressDialog();
                                showToast("Please check your email for verification");
                            }
                        } else {
                            dismissProgressDialog();
                            showToast("Either username/password was incorrect!");
                        }
                    }
                });
                //parseHelper.authenticateUser(etUsername.getText().toString(),etPassword.getText().toString());
            } else {
                showToast("Please turn on your WiFi/Data connection!");
            }
        }
    }
    public void constructJSON(ParseUser user) {
        Log.d("login","constructing json");
        JSONObject visitorJSON = getVisitorJSON();
        try {
            String visitor_id = user.getParseObject("visitor").fetch().getObjectId();
            final String objectId = user.getParseObject("visitor").fetch().getObjectId();
            String fname = user.getParseObject("visitor").fetch().getString("fname");
            String lname = user.getParseObject("visitor").fetch().getString("lname");
            String contact_no = user.getParseObject("visitor").fetch().getString("contactNo");
            String email = user.getParseObject("visitor").fetch().getString("email");
            String company_name = user.getParseObject("visitor").fetch().getString("company");

            Prefs.setMyStringPref(this,"visitor_id",visitor_id);
            Prefs.setMyStringPref(this,"object_id",objectId);
            Prefs.setMyStringPref(this,"fname",fname);
            Prefs.setMyStringPref(this,"lname",lname);
            Prefs.setMyStringPref(this,"contact_no",contact_no);
            Prefs.setMyStringPref(this,"email",email);
            Prefs.setMyStringPref(this,"company_name",company_name);

            //visitorJSON.put("visitor_id", visitor_id);
            visitorJSON.put("f",fname);
            visitorJSON.put("l",lname);
            visitorJSON.put("m",contact_no);
            visitorJSON.put("e",email);
            visitorJSON.put("c", company_name);
            Log.d("login", "my json --> " + visitorJSON.toString());

            parseHelper.setOnLoadDataListener(new OnLoadDataListener() {
                @Override
                public void onPreLoad() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        updateProgressDialog("Gathering user information, Please wait...");
                        }
                    });
                }
                @Override
                public void onLoadFinished(final int counter) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finishedQueriesCount.add(counter);
                            Log.d("login", "finished queries size -->  " + finishedQueriesCount.size());
                            if (finishedQueriesCount.size() == 6) {
                                finishedQueriesCount.clear();
                                finish();
                                dismissProgressDialog();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                animateToLeft(LoginActivity.this);
                            }
                        }
                    });
                }
            });
            parseHelper.getExhibitors(getExhibitorList(),false);
            parseHelper.getNews(getNewsList(),false);
            parseHelper.getMedia(getMyPhotoVidoes(),false);
            parseHelper.getCategories(getCategoriesList(),false);
            parseHelper.getEvent(getMyEventSchedule(),getMyEvents(),getMyEventMaps(),false);
            parseHelper.getVisitor(Prefs.getMyStringPrefs(this,"visitor_id"),false,getVisitor());
        } catch (Exception e) {
            Log.d("login", "errorrrr ----->  " + e.toString());
        }
    }

    @OnClick(R.id.btnRegister)
    public void registerUser() {
        if (isNetworkAvailable()) {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            animateToLeft(this);
        } else {
            showToast("Please turn on your WiFi/Data connection!");
        }
    }
}
