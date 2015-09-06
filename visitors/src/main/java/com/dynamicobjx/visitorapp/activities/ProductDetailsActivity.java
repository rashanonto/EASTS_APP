package com.dynamicobjx.visitorapp.activities;

import android.os.Bundle;
import android.view.View;

import com.dynamicobjx.visitorapp.R;

public class ProductDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initActionBar("Product Details").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                animateToRight(ProductDetailsActivity.this);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(ProductDetailsActivity.this);
    }

}
