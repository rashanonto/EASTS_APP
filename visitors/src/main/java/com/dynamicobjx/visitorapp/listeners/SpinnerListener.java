package com.dynamicobjx.visitorapp.listeners;

/**
 * Created by jobelle on 2/18/15.
 */
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SpinnerListener implements OnItemSelectedListener {

    private String spinner;

    public SpinnerListener(String spinner) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
/*        Toast.makeText(parent.getContext(),
                "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();*/
  }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}