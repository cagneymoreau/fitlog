package com.cagneymoreau.fitlog.views.history_viewer.gridrecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.History_Summary_ViewHolder;

import java.util.ArrayList;

public class Grid_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> data;


    public Grid_Adapter( ArrayList<String> dataField)
    {
        data = dataField;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.grid_card, parent, false);
       viewHolder = new Grid_ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Grid_ViewHolder viewHolder = (Grid_ViewHolder) holder;
        viewHolder.setVals(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public ArrayList<String> getData() {
        return data;
    }

}
