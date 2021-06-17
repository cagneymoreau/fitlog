package com.cagneymoreau.fitlog.views.checklist_design.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.List;

public class CheckList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> data;
    int mChosen = -1;

    public CheckList_Adapter(List<String> dataField)
    {
        data = dataField;


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

        if (position == mChosen){
            String v = data.get(position) + "!!";
            checkList_viewHolder.getCheckListTitle().setText(v);
        }else{
            checkList_viewHolder.getCheckListTitle().setText(data.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setChosen(int c)
    {
        if (mChosen == c){
            mChosen = -1;
        }else{
            mChosen = c;
        }
    }

    public int getChosen()
    {
        return mChosen;

    }

}
