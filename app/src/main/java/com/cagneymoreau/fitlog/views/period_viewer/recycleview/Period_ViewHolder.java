package com.cagneymoreau.fitlog.views.period_viewer.recycleview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import kotlin.Triple;

public class Period_ViewHolder extends RecyclerView.ViewHolder {

        TextView splitName;
        TextView dayName;
        TextView date;
        View view;

    public Period_ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        splitName = view.findViewById(R.id.histSum_splitName_TextView);
        dayName = view.findViewById(R.id.histSum_dayName_TextView);
        date = view.findViewById(R.id.histSum_date_TextView);



    }


   public void setVal(Triple<Long, Long, Boolean> p)
   {
       if (p == null){

           splitName.setText("Add backdated workout");
           dayName.setVisibility(View.INVISIBLE);
           date.setVisibility(View.INVISIBLE);
            return;
       }

       LocalDate date = Instant.ofEpochMilli(p.getFirst() + 1000).atZone(ZoneId.systemDefault()).toLocalDate();

       String val = date.getMonth().toString() + " " + date.getYear();

       splitName.setText(val);
       dayName.setVisibility(View.INVISIBLE);
       this.date.setVisibility(View.INVISIBLE);
   }


}
