package com.cagneymoreau.fitlog.views.active_workout.recycleview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.active_workout.Active_Workout;
import com.cagneymoreau.fitlog.views.active_workout.recycleview.Movement_Adapter;

import java.util.ArrayList;

public class ActiveWorkout_ViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    View view;
    ArrayList<String> mList;

    RecyclerView recyclerView;
    Movement_Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Active_Workout active_workout;

    public ActiveWorkout_ViewHolder(@NonNull View itemView, Active_Workout active_workout) {
        super(itemView);

        this.active_workout = active_workout;

        title = itemView.findViewById(R.id.activeWorkoutTitle_TextView);
        view = itemView;

    }


    public void setList(ArrayList<String> list, int position)
    {
       mList = new ArrayList<>(list);

       title.setText(mList.get(0));
       mList.remove(0);

        recyclerView = itemView.findViewById(R.id.movement_recycleView);
        layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Movement_Adapter(mList, active_workout, position);
        recyclerView.setAdapter(adapter);

    }




}