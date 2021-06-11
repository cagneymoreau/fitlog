package com.cagneymoreau.fitlog.recycleviews;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Split_Edit_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ListItem> data;
    Fragment fragment;

    public Split_Edit_Adapter(ArrayList<ListItem> dataField, Fragment fragment)
    {
        data = dataField;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.split_card, parent, false);
       viewHolder = new Split_ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Split_ViewHolder split_viewHolder = (Split_ViewHolder) holder;
        split_viewHolder.getSplitTitle().setText(data.get(position).title);
        switch (data.get(position).myType){

            case TITLE:
                split_viewHolder.getSplitTitle().setTextColor(Color.RED);
                break;

            case HEADER:
                split_viewHolder.getSplitTitle().setTextColor(Color.GREEN);
                break;

            case FIELD:
                split_viewHolder.getSplitTitle().setTextColor(Color.BLACK);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
