package com.cagneymoreau.fitlog.views.active_workout.recycleview;

import android.view.View;
import android.widget.Button;
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
    Button button;

    RecyclerView recyclerView;
    Movement_Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Active_Workout active_workout;

    public ActiveWorkout_ViewHolder(@NonNull View itemView, Active_Workout active_workout) {
        super(itemView);

        this.active_workout = active_workout;

        title = itemView.findViewById(R.id.activeWorkoutTitle_TextView);
        button = itemView.findViewById(R.id.freestyle_button);
        view = itemView;

    }


    public void setList(ArrayList<String> list, int position)
    {
       mList = new ArrayList<>(list);

       button.setText("+ add");
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               active_workout.insertFreeStyle(position);
           }
       });

       title.setText(mList.get(0));
       mList.remove(0);

       title.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               active_workout.changeMoveName(position);
               return false;
           }
       });

        recyclerView = itemView.findViewById(R.id.movement_recycleView);
        layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Movement_Adapter(mList, active_workout, position, this);
        recyclerView.setAdapter(adapter);
        scrollRight();

    }


    public void scrollRight()
    {
        recyclerView.smoothScrollToPosition(mList.size());
    }

}