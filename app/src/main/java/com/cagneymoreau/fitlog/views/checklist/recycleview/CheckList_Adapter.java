package com.cagneymoreau.fitlog.views.checklist.recycleview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cagneymoreau.fitlog.R;
import java.util.List;



public class CheckList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Pair<List<String>, Boolean> data;
    int mChosen = -1;


    public CheckList_Adapter(Pair<List<String>, Boolean> dataField)
    {
        data = dataField;

        if (data.second){
            mChosen = 0;
        }
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
            String v = data.first.get(position);
            checkList_viewHolder.getCheckListTitle().setText(v);
            checkList_viewHolder.getCheckListImage().setImageResource(R.drawable.green_select);
            checkList_viewHolder.getCheckListImage().setMaxHeight(100);
        }
        else{
            checkList_viewHolder.getCheckListTitle().setText(data.first.get(position));
            checkList_viewHolder.getCheckListImage().setImageDrawable(null);
            String v = data.first.get(position);
            checkList_viewHolder.getCheckListTitle().setText(v);

            // TODO: 10/21/2021 text should be resource as its comparing to hardcode elsewhere
            if (v.equals(" Create New CheckList")){
                checkList_viewHolder.getCheckListImage().setImageResource(R.drawable.gold_arrow);
                checkList_viewHolder.getCheckListImage().setMaxHeight(100);
            }

        }

    }


    public void insertList(Pair<List<String>, Boolean> newData)
    {
        data = newData;
        if (data.second){
            mChosen = 0;
        }else{
            mChosen = -1;
        }

    }


    @Override
    public int getItemCount() {
        return data.first.size();
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


    public String removeItem(int pos)
    {
        // TODO: 11/1/2021  
        //if (pos == 0){
        // data.second =false;
        //}        
        
        return  data.first.get(pos);
    }
    
    public void restoreItem(String item, int pos)
    {
        data.first.add(pos, item);
    }

}
