package com.dynamicobjx.visitorapp.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.helpers.ZxingHelper;
import com.google.zxing.common.BitMatrix;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rsbulanon on 2/3/15.
 */
public class MyQRActivity extends BaseActivity {

    @InjectView(R.id.ivMyQr) ImageView ivMyQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myqr);
        ButterKnife.inject(this);
        initActionBar("My QR Code").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(MyQRActivity.this);
            }
        });
        if (getVisitorJSON().length() < 1) {
            Log.d("qr","create QR");
            contructVisitorJSON();
        } else {
            Log.d("qr","QR already created " + getVisitorJSON().toString());
        }
        new DisplayQR().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(MyQRActivity.this);
    }

    private class DisplayQR extends AsyncTask<Void, Void, BitMatrix> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("My QR","Generating your QR code, Please wait...");
        }

        @Override
        protected BitMatrix doInBackground(Void... arg) {
            return ZxingHelper.generateQRCode(getVisitorJSON().toString(), 500, 500);
        }

        @Override
        protected void onPostExecute(BitMatrix bitMatrix) {
            super.onPostExecute(bitMatrix);
            dismissProgressDialog();
            showMYQR(bitMatrix);
        }
    }

    private void showMYQR(BitMatrix bitMatrix) {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        ivMyQr.setImageBitmap(bmp);
    }
}
