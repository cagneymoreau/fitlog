package com.cagneymoreau.fitlog.views.generic_recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.List;


public class Generic_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<String> data;
    int mChosen = -1;


    public Generic_Adapter(List<String> dataField)
    {
        data = dataField;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.checklist_card, parent, false);
       viewHolder = new Generic_ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Generic_ViewHolder gen_viewHolder = (Generic_ViewHolder) holder;

            String v = data.get(position);

            if (position == mChosen){
                gen_viewHolder.getCheckListImage().setVisibility(View.VISIBLE);
                gen_viewHolder.getCheckListImage().setImageResource(R.drawable.green_select);
                gen_viewHolder.getCheckListImage().setMaxHeight(100);
            }else{
                gen_viewHolder.getCheckListImage().setVisibility(View.INVISIBLE);
            }

            gen_viewHolder.getCheckListTitle().setText(v);

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
