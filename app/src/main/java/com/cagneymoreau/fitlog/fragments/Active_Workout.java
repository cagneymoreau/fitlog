package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.recycleviews.Active_Workout_Adapter;
import com.cagneymoreau.fitlog.recycleviews.CheckList_Adapter;
import com.cagneymoreau.fitlog.recycleviews.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Child window of workout holder.
 * Displays the current days movements with fields the user can enter
 */

public class Active_Workout extends Fragment {

    private View fragView;
    Controller controller;

    ArrayList<ArrayList<String>> currentWorkout;

    RecyclerView recyclerView;
    Active_Workout_Adapter active_workout_adapter;
    RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        controller = mainActivity.getConroller();

        return fragView;

    }


    @Override
    public void onResume() {
        super.onResume();
        buildRecycleView();
    }


    private void buildRecycleView()
    {
        currentWorkout = controller.getCurrentWorkout();

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        active_workout_adapter = new Active_Workout_Adapter(currentWorkout, this);
        recyclerView.setAdapter(active_workout_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {



            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {



            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));


    }


}
