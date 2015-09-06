package com.dynamicobjx.visitorapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dynamicobjx.visitorapp.R;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class ScheduleIncomingFragment extends android.support.v4.app.Fragment {

    public static ScheduleIncomingFragment newInstance() {
        return new ScheduleIncomingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_incoming,null);
        return view;
    }
}
