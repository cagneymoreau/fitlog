package com.cagneymoreau.fitlog.views.checklist_design.recycleview;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class CheckList_Review_ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        View view;

    public CheckList_Review_ViewHolder(@NonNull View itemView) {
        super(itemView);

        checkBox = itemView.findViewById(R.id.checklist_checkBox);
        view = itemView;

    }


    public CheckBox getCheckBox()
    {
        return checkBox;
    }


}
