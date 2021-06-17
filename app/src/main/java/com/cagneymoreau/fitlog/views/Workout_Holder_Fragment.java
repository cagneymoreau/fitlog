package com.cagneymoreau.fitlog.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.active_workout.Active_Workout;
import com.cagneymoreau.fitlog.views.history_viewer.History_ViewOnly;
import com.cagneymoreau.fitlog.logic.Controller;

/**
 * Workout holder is the fragment that's viewable during a new workout or when viewing a workout in history
 * It holds several easily accessible fragments the user would want to see while working out or editing
 *
 * current workout
 * non editable history
 * checklists and notes
 * timerActions
 *
 */

public class Workout_Holder_Fragment extends Fragment {


    private View fragView;
    Controller controller;

   Button active, historyButton, timeButton;

  // FrameLayout display;

    Active_Workout active_workout_;
    History_ViewOnly historyViewOnly;
    TimerActions timerActions;

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
        //display = fragView.findViewById(R.id.workout_Holder_Layout);

        buildFragments();

        return fragView;


    }

    @Override
    public void onResume() {
        super.onResume();

        buildInitialUI();
    }




    private void buildInitialUI()
    {

        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.workout_Holder_Layout, active_workout_).addToBackStack(null).commit();
        //getActivity().getSupportFragmentManager().beginTransaction().hide(currentFrag);
        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.workout_Holder_Layout, historyViewOnly).addToBackStack(null).commit();
       // getActivity().getSupportFragmentManager().beginTransaction().hide(historyViewOnly);
        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.workout_Holder_Layout, timerActions).addToBackStack(null).commit();
       // getActivity().getSupportFragmentManager().beginTransaction().hide(timerActions);

        //getActivity().getSupportFragmentManager().beginTransaction().show(active_workout_);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.workout_Holder_Layout, active_workout_).addToBackStack(null).commit();
        currentFrag = active_workout_;

        active.setText("active workout");
        active.setOnClickListener(v -> {
            if (currentFrag.equals(active_workout_))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, active_workout_).addToBackStack(null).commit();
           // getActivity().getSupportFragmentManager().beginTransaction().show(active_workout_);
            currentFrag = active_workout_;
        });

        historyButton.setText("history");
        historyButton.setOnClickListener(v ->{
            if (currentFrag.equals(historyViewOnly))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, historyViewOnly).addToBackStack(null).commit();
            //getActivity().getSupportFragmentManager().beginTransaction().show(historyViewOnly);
            currentFrag = historyViewOnly;
        });

        timeButton.setText("time");
        timeButton.setOnClickListener(v -> {
            if (currentFrag.equals(timerActions))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, timerActions).addToBackStack(null).commit();
            // getActivity().getSupportFragmentManager().beginTransaction().show(timerActions);
            currentFrag = timerActions;
        });



    }


    private void buildFragments()
    {
        active_workout_ = new Active_Workout();
        historyViewOnly = new History_ViewOnly();
        timerActions = new TimerActions();

    }





}
