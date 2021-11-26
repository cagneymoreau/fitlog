package com.cagneymoreau.fitlog.views.period_viewer.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.History_Summary_ViewHolder;
import com.cagneymoreau.fitlog.views.period_viewer.History_Periods;

import java.util.ArrayList;

import kotlin.Triple;

public class Period_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Triple<Long, Long, Boolean>> data;


    public Period_Adapter(ArrayList<Triple<Long, Long, Boolean>> dataField)
    {
        data = dataField;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.history_summary_card, parent, false);
       viewHolder = new Period_ViewHolder(v);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Period_ViewHolder viewHolder = (Period_ViewHolder) holder;
        viewHolder.setVal(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    public ArrayList<Triple<Long, Long, Boolean>> getData() {
        return data;
    }

}
