package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

import java.util.List;

/**
 * Main menu is menu buttons only
 * checklist builder
 * split builder
 * start workout
 * body measure
 * statistics
 *
 */

public class Main_Menu_Fragment extends Fragment {

    private View fragView;
    Controller controller;

    Button checkListBuildEdit, splitBuildEdit, startWorkout, bodyMeasure, statistics, historyButton;

    TextView checkListTextView, splitTextView;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.main_menu, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();



        return fragView;

    }

    @Override
    public void onResume() {
        super.onResume();
        addData();
    }

    private void addData()
    {

        buildButtons();

        buildTextViews();

    }

    private void buildButtons()
    {

        splitBuildEdit = fragView.findViewById(R.id.split_Build_Edit_Button);
        splitBuildEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_routine_Builder_Fragment);

            }
        });



            checkListBuildEdit = fragView.findViewById(R.id.checkList_build_edit_button);
            checkListBuildEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_checkList_Chooser_Fragment);
                }
            });
        if (!controller.getcheckListBool()) {
            checkListBuildEdit.setVisibility(View.INVISIBLE);
        }

        startWorkout = fragView.findViewById(R.id.start_workout_button);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_split_Chooser_Fragment);
            }
        });

        bodyMeasure = fragView.findViewById(R.id.body_measurement_button);
        bodyMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_body_Measure);
            }
        });

        if (!controller.bodyMeasureBool()){
            bodyMeasure.setVisibility(View.INVISIBLE);
        }

        statistics = fragView.findViewById(R.id.statistics_button);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_statistics);
            }
        });

        if (!controller.getStatisticsBool()){
            statistics.setVisibility(View.INVISIBLE);
        }


        historyButton = fragView.findViewById(R.id.history_Button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_history);

            }
        });

    }

    private void buildTextViews()
    {

        checkListTextView = fragView.findViewById(R.id.current_checkList_textView);

        Pair<List<String>, Boolean> inp = controller.getCheckListsChoice();
        List<String> checkLists = inp.first;

        if (checkLists != null && checkLists.size() > 1) checkListTextView.setText("Current: " + checkLists.get(0));

        if (!controller.getcheckListBool()) checkListTextView.setVisibility(View.INVISIBLE);


        splitTextView = fragView.findViewById(R.id.current_split_textView);

        List<String> splits =  controller.getSplitsList();

        if (splits != null && splits.size() > 0){ splitTextView.setText("Current: " + splits.get(0));}
        else {
            splitTextView.setText("Current: none");
        }

    }






}
