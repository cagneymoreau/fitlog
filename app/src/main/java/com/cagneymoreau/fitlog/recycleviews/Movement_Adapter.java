package com.cagneymoreau.fitlog.recycleviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.ArrayList;
import java.util.List;

public class Movement_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> data;
    Fragment fragment;

    public Movement_Adapter(ArrayList<String> dataField, Fragment fragment)
    {
        data = dataField;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.checklist_card, parent, false);
        viewHolder = new CheckList_ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CheckList_ViewHolder checkList_viewHolder = (CheckList_ViewHolder) holder;
        checkList_viewHolder.getCheckListTitle().setText(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
