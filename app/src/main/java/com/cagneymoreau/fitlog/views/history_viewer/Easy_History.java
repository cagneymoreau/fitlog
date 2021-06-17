package com.cagneymoreau.fitlog.views.history_viewer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.data.WorkoutRecord;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.active_workout.recycleview.Active_Workout_Adapter;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.History_Adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Display two recycleviews with recent workouts
 *
 * the first list is a curated view of recent matches
 *
 * the second list is all recent workouts
 *
 */

public class Easy_History extends Fragment {

    public static final String TAG = "Easy_History";

    private View fragView;
    Controller controller;

    ArrayList<HistoryItem> curatedRecords;
    RecyclerView curRecyclerView;
    History_Adapter curHistory_adapter ;
    RecyclerView.LayoutManager curLayoutManager;

    ArrayList<HistoryItem> recentRecords;
    RecyclerView recRecyclerView;
    History_Adapter recHistory_adapter;
    RecyclerView.LayoutManager recLayoutManager;

    History_ViewOnly history_viewOnly;

    public Easy_History(History_ViewOnly hv)
    {
        history_viewOnly = hv;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.easy_history, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        buildCuratedRecycleView();
        buildRecentRecycleView();

        return fragView;

    }


    private void buildCuratedRecycleView()
    {

        curatedRecords = controller.getCuratedList();

        curRecyclerView = fragView.findViewById(R.id.easyHist_curated_recycleView);
        curLayoutManager = new LinearLayoutManager(getActivity());
        curRecyclerView.setLayoutManager(curLayoutManager);

        curHistory_adapter = new History_Adapter(curatedRecords);
        curRecyclerView.setAdapter(curHistory_adapter);

        curRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), curRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                history_viewOnly.addNewTab(curatedRecords.get(position));

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {



            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));


    }


    private void buildRecentRecycleView()
    {
        recentRecords = controller.getRecentList();

        recRecyclerView = fragView.findViewById(R.id.easyHist_recent_recyclview);
        recLayoutManager = new LinearLayoutManager(getActivity());
        recRecyclerView.setLayoutManager(recLayoutManager);

        recHistory_adapter = new History_Adapter(recentRecords);
        recRecyclerView.setAdapter(recHistory_adapter);

        recRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                history_viewOnly.addNewTab(curatedRecords.get(position));

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
