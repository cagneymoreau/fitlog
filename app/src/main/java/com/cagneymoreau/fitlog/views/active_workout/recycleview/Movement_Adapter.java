package com.cagneymoreau.fitlog.views.active_workout.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.active_workout.Active_Workout;

import java.util.ArrayList;

public class Movement_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> data;
    Active_Workout active_workout;
    int position;

    public Movement_Adapter(ArrayList<String> dataField, Active_Workout active_workout, int position)
    {
        data = dataField;
        this.active_workout = active_workout;
        this.position = position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.movement_card, parent, false);
        viewHolder = new Movement_ViewHolder(v, active_workout, position);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Movement_ViewHolder movement_viewHolder = (Movement_ViewHolder) holder;

        if (data.get(position).equals("")){
            movement_viewHolder.getEditText().setHint("set here");

        }else{
            movement_viewHolder.getEditText().setText(data.get(position));
        }

        if (position == data.size()-1){
            movement_viewHolder.showButton();
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
