package com.cagneymoreau.fitlog.views.active_workout.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.active_workout.Active_Workout;

import java.util.ArrayList;

/**
 * Holds each workout movement and data in a vertical list
 * each list item holds a title with another recycleview with a horizontallist with each movement
 */

public class Active_Workout_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ArrayList<ArrayList<String>> data;
    Active_Workout fragment;


    public Active_Workout_Adapter(ArrayList<ArrayList<String>> dataField, Active_Workout fragment)
    {
        data = dataField;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.active_workout_card, parent, false);

        viewHolder = new ActiveWorkout_ViewHolder(v, fragment);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ActiveWorkout_ViewHolder activeWorkout_viewHolder = (ActiveWorkout_ViewHolder) holder;
        activeWorkout_viewHolder.setList(data.get(position), position);
        //activeWorkout_viewHolder.setTitle("position " + position);


    }


    @Override
    public int getItemCount() {
        return data.size();
    }




}
