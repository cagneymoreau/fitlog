package com.cagneymoreau.fitlog.views.split_design.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.List;

public class Split_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> data;
    Fragment fragment;

    public Split_Adapter(List<String> dataField, Fragment fragment)
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

        String v = data.get(position);
        split_viewHolder.getSplitTitle().setText(v);


        if (data.size() > 1 && position == 0){
            split_viewHolder.getImageView().setImageResource(R.drawable.green_select);
            split_viewHolder.getImageView().setMaxHeight(100);
        }else if (position == data.size()-1){
            split_viewHolder.getImageView().setImageResource(R.drawable.gold_arrow);
            split_viewHolder.getImageView().setMaxHeight(100);
        }else{
            split_viewHolder.getImageView().setImageDrawable(null);
        }


    }

    public void insertList(List<String> newish)
    {
        data = newish;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
