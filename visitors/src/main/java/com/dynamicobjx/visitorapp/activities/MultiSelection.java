package com.dynamicobjx.visitorapp.activities;

/**
 * Created by jobelle on 2/18/15.
 */

import android.os.Bundle;
        import android.app.Activity;
        import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.Toast;

     import com.dynamicobjx.visitorapp.R;

public class MultiSelection extends Activity implements AdapterView.OnItemClickListener {

    MultiSelect spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onClick(View v){
        String s = spinner.getSelectedItemsAsString();
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        CheckedTextView ctv = (CheckedTextView)arg1;
        if(ctv.isChecked()){
            Toast.makeText(getApplicationContext(), "now it is unchecked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "now it is checked", Toast.LENGTH_SHORT).show();
        }
    }
}
