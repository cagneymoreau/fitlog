package com.cagneymoreau.fitlog.views.history_viewer.gridrecycle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;

public class Grid_ViewHolder extends RecyclerView.ViewHolder {

        TextView record;
        View view;

    public Grid_ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        record = view.findViewById(R.id.gridcard_textView);

    }


   public void setVals(String v)
   {
      record.setText(v);


   }




}
