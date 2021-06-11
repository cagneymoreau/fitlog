package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

/**
 * Child window of workout holder
 * Handy timer for measuring rest or sets
 *
 * format as time x repeats
 *
 */

public class Timer extends Fragment {

    public static final String TAG = "Timer";

    private View fragView;
    Controller controller;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.timer_stopwatch_layout, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();



        return fragView;

    }


}
