package com.cagneymoreau.fitlog.views.history_viewer.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.checklist_design.recycleview.CheckList_ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class History_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HistoryItem> data;


    public History_Adapter(ArrayList<HistoryItem> dataField)
    {
        data = dataField;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.history_summary_card, parent, false);
       viewHolder = new History_Summary_ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        History_Summary_ViewHolder history_summary_viewHolder = (History_Summary_ViewHolder) holder;
        history_summary_viewHolder.setVals(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
