package com.cagneymoreau.fitlog.recycleviews;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class Split_ViewHolder extends RecyclerView.ViewHolder {

        TextView splitTitle;
        View view;

    public Split_ViewHolder(@NonNull View itemView) {
        super(itemView);

        splitTitle = itemView.findViewById(R.id.split_item_title_textView);
        view = itemView;

    }


    public TextView getSplitTitle()
    {
        return splitTitle;
    }


}
