package com.cagneymoreau.fitlog.views.checklist.recycleview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.List;

public class CheckList_Review_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String, Boolean>> data;
    Fragment fragment;

    public CheckList_Review_Adapter(List<Pair<String, Boolean>> dataField, Fragment fragment)
    {
        data = dataField;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.checkable_card, parent, false);
       viewHolder = new CheckList_Review_ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            CheckList_Review_ViewHolder checkList_viewHolder = (CheckList_Review_ViewHolder) holder;
            checkList_viewHolder.getCheckBox().setText(data.get(position).first);
            checkList_viewHolder.getCheckBox().setChecked(data.get(position).second);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
