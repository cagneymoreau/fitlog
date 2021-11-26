package com.cagneymoreau.fitlog.views.generic_recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class Generic_ViewHolder extends RecyclerView.ViewHolder {

        TextView checkListTitle;
        ImageView checkListImage;
        View view;

    public Generic_ViewHolder(@NonNull View itemView) {
        super(itemView);

        checkListTitle = itemView.findViewById(R.id.checklist_item_title_textView);
        checkListImage = itemView.findViewById(R.id.checklist_item__imageView);
        view = itemView;

    }


    public TextView getCheckListTitle()
    {
        return checkListTitle;
    }

    public ImageView getCheckListImage() {
        return checkListImage;
    }
}
