package com.cagneymoreau.fitlog.views.setting_utility.recycleview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.checklist.recycleview.CheckList_ViewHolder;

import java.util.ArrayList;

public class Backup_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Pair<String, Boolean>> data;
    Fragment fragment;

    public Backup_Adapter(ArrayList<Pair<String, Boolean>> dataField, Fragment fragment)
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
        String v = data.get(position).first;

        if ( data.get(position).second){
            v += "  go";
        }
        checkList_viewHolder.getCheckListTitle().setText(v);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
