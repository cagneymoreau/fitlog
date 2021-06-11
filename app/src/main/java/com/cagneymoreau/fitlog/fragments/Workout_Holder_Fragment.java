package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

/**
 * Workout holder is the fragment that's viewable during a new workout or when viewing a workout in history
 * It holds several easily accessible fragments the user would want to see while working out or editing
 *
 * current workout
 * non editable history
 * checklists and notes
 * timer
 *
 */

public class Workout_Holder_Fragment extends Fragment {


    private View fragView;
    Controller controller;

   Button active, historyButton, timeButton;

   LinearLayout display;

    Active_Workout active_workout_;
    History_ViewOnly historyViewOnly;
    Timer timer;

    Fragment currentFrag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.workout_holder, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        active = fragView.findViewById(R.id.left_holder_button);
        historyButton = fragView.findViewById(R.id.center_holder_button);
        timeButton = fragView.findViewById(R.id.right_holder_button);
        display = fragView.findViewById(R.id.workout_Holder_LinearLayout);



        return fragView;


    }

    @Override
    public void onResume() {
        super.onResume();

        buildFragments();
        buildInitialUI();
    }




    private void buildInitialUI()
    {

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.workout_Holder_LinearLayout, active_workout_).addToBackStack(null).commit();
        currentFrag = active_workout_;

        active.setOnClickListener(v -> {
            if (currentFrag.equals(active_workout_))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_LinearLayout, active_workout_).addToBackStack(null).commit();
            currentFrag = active_workout_;
        });

        historyButton.setOnClickListener(v ->{
            if (currentFrag.equals(historyViewOnly))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_LinearLayout, historyViewOnly).addToBackStack(null).commit();
            currentFrag = historyViewOnly;
        });

        timeButton.setOnClickListener(v -> {
            if (currentFrag.equals(timer))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_LinearLayout, timer).addToBackStack(null).commit();
            currentFrag = timer;
        });




    }


    private void buildFragments()
    {
        active_workout_ = new Active_Workout();
        historyViewOnly = new History_ViewOnly();
        timer = new Timer();

    }





}
