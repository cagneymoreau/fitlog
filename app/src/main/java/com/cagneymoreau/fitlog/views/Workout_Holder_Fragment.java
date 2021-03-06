package com.cagneymoreau.fitlog.views;

import android.os.Bundle;
import android.util.Log;
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
import com.cagneymoreau.fitlog.views.checklist.CheckList_Viewer;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.history_viewer.History_Chooser;
import com.cagneymoreau.fitlog.views.history_viewer.History_Viewer;

/**
 * Workout holder is the fragment that's viewable during a new workout or when viewing a workout in history
 * It holds several easily accessible fragments the user would want to see while working out or editing
 *
 * current workout
 * non editable history
 * checklists and notes
 * timerActions
 *
 * All data is loaded from the controllers currentworkout data
 *
 */

public class Workout_Holder_Fragment extends Fragment {

    private static String TAG = "Workout_Holder_Fragment";

    private View fragView;
    Controller controller;

   Button activeButton, checklistButton, historyButton, timeButton;

  // FrameLayout display;

    Active_Workout active_workout_;
    History_Viewer history_viewer;
    TimerActions timerActions;
    CheckList_Viewer checkListViewer;

    Fragment currentFrag;

    int currUID = -1;

    boolean saved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.workout_holder, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        int uid = getArguments().getInt("uid");



        if (uid == -1){
            buildFragments();
        }else{
            requestActive(uid);
        }






        return fragView;


    }


    public void requestActive(int uid)
    {
        controller.activateWorkout(uid, this);
    }


    public void openHistory()
    {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildFragments();

            }
        });

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

        activeButton.setText("workout");
        activeButton.setOnClickListener(v -> {
            if (currentFrag.equals(active_workout_))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, active_workout_).addToBackStack(null).commit();
           // getActivity().getSupportFragmentManager().beginTransaction().show(active_workout_);
            currentFrag = active_workout_;
        });


        checklistButton.setText("checklist");
        checklistButton.setOnClickListener(v -> {
            if (currentFrag.equals(checkListViewer))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, checkListViewer).addToBackStack(null).commit();
            // getActivity().getSupportFragmentManager().beginTransaction().show(active_workout_);
            currentFrag = checkListViewer;
        });


        historyButton.setText("history");
        historyButton.setOnClickListener(v ->{
            if (currentFrag.equals(history_viewer))return;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workout_Holder_Layout, history_viewer).addToBackStack(null).commit();
            //getActivity().getSupportFragmentManager().beginTransaction().show(historyViewOnly);
            currentFrag = history_viewer;
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
        activeButton = fragView.findViewById(R.id.leftfar_holder_button);
        checklistButton = fragView.findViewById(R.id.left_holder_button);
        historyButton = fragView.findViewById(R.id.right_holder_button);
        timeButton = fragView.findViewById(R.id.rightfar_holder_button);
        //display = fragView.findViewById(R.id.workout_Holder_Layout);



        active_workout_ = new Active_Workout();
        history_viewer = new History_Viewer();
        history_viewer.setWkHolder(this);
        timerActions = new TimerActions();
        checkListViewer = new CheckList_Viewer();

        buildInitialUI();

        saved = false;

    }



    private void saveAll()
    {
        if(saved) return;

        active_workout_.saveAll();
        checkListViewer.saveAll();

        controller.saveWorkoutToDB();

        saved = true;

    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();

        saveAll();

    }


    @Override
    public void onPause() {
        super.onPause();
        saveAll();
    }

    public int getCurrUID()
    {
        return currUID;
    }

    public void setCurrUID(int v)
    {
        currUID = v;
    }


}
