package com.cagneymoreau.fitlog.views.history_viewer.recycleview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class History_Summary_ViewHolder extends RecyclerView.ViewHolder {

        TextView splitName;
        TextView dayName;
        TextView date;
        View view;

    public History_Summary_ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        splitName = view.findViewById(R.id.histSum_splitName_TextView);
        dayName = view.findViewById(R.id.histSum_dayName_TextView);
        date = view.findViewById(R.id.histSum_date_TextView);


    }


   public void setVals(HistoryItem hist)
   {
       String v1 = hist.splitName + "  ";
       splitName.setText(v1);
       String v2 = hist.dayName + "  ";
       dayName.setText(v2);
       String dVal = hist.date.getDayOfWeek().toString() + " the " + hist.date.getDayOfMonth();
       this.date.setText(dVal);

   }

    public void setDebug(HistoryItem historyItem)
    {
        String n = Integer.toString(historyItem.uid);
        splitName.setText(n);
    }

}
