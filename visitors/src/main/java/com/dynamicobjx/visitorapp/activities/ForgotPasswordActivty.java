package com.dynamicobjx.visitorapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.ParseHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgotPasswordActivty extends BaseActivity {

    private ParseHelper parseHelper;
    @InjectView(R.id.email) EditText email;
    @InjectView(R.id.ivLogo) ImageView ivLogo;
    private ArrayList<Integer> finishedQueriesCount = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        ButterKnife.inject(this);
        initFonts();
        parseHelper = new ParseHelper();
       // parseHelper.setOnLoginListener(this);


        Bitmap bitmap = trimDownImgReso(getResources(), R.drawable.logo, 1000, 1000);
        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
        ivLogo.setImageDrawable(drawable);
    }

    @OnClick(R.id.btnSubmit)
    public void authenticateUser() {
        if (email.getText().toString().isEmpty()) {
            showToast("Please enter your email!");

        } else {
            /*startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            animateToLeft(LoginActivity.this);*/
            if (isNetworkAvailable()) {
                showProgressDialog("", "Retrieving your Password, Please wait...");
                ParseUser.requestPasswordResetInBackground(email.getText().toString(), new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            dismissProgressDialog();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!isFinishing()) {
                                        new AlertDialog.Builder(ForgotPasswordActivty.this)
                                                .setTitle("Successful!")
                                                .setMessage("View your Inbox to reset your password")
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // whatever...
                                                    }
                                                }).create().show();
                                    }
                                }
                            });
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            dismissProgressDialog();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("Open your Network Connection!");
                                }
                            });
                        }
                    }
                });


            }}}}