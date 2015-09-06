package com.dynamicobjx.visitorapp.helpers;

import android.app.Activity;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by rsbulanon on 2/3/15.
 */
public class ZxingHelper {

    private static Activity activity;

    public static void openScanner(Activity activity) {
        ZxingHelper.activity = activity;
        IntentIntegrator.initiateScan(ZxingHelper.activity,"QR_CODE_MODE");
    }

    public static void closeScanner() {
        activity.finish();
    }

    public static BitMatrix generateQRCode(String toEncode, int height, int width) {
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(toEncode, BarcodeFormat.QR_CODE, height, width);
        } catch (WriterException ex) {
            Log.d("qr", "error while writing qr code --> " + ex.toString());
        }
        return bitMatrix;
    }
}
