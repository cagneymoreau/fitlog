package com.cagneymoreau.fitlog.views.checklist.recycleview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class CheckList_Edit_ViewHolder extends RecyclerView.ViewHolder {

        TextView checkListTitle;
        View view;

    public CheckList_Edit_ViewHolder(@NonNull View itemView) {
        super(itemView);

        checkListTitle = itemView.findViewById(R.id.checklist_item_title_textView);
        view = itemView;

    }


    public TextView getCheckListTitle()
    {
        return checkListTitle;
    }


}
