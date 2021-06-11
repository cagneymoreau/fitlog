package com.cagneymoreau.fitlog.recycleviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds each workout movement and data in a vertical list
 * each list item holds a title with another recycleview with a horizontallist with each movement
 */

public class Active_Workout_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ArrayList<ArrayList<String>> data;
    Fragment fragment;

    RecyclerView recyclerView;
    Movement_Adapter movement_adapter;
    RecyclerView.LayoutManager layoutManager;

    public Active_Workout_Adapter(ArrayList<ArrayList<String>> dataField, Fragment fragment)
    {
        data = dataField;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.movement_card, parent, false);

        recyclerView = v.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(parent.getContext());
        recyclerView.setLayoutManager(layoutManager);

        viewHolder = new CheckList_ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CheckList_ViewHolder checkList_viewHolder = (CheckList_ViewHolder) holder;

        movement_adapter = new Movement_Adapter(data.get(position), fragment);
        recyclerView.setAdapter(movement_adapter);


    }


    @Override
    public int getItemCount() {
        return data.size();
    }




}
