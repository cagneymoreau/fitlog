package com.cagneymoreau.fitlog.views.split_design.recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class Split_ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView splitTitle;
        View view;

    public Split_ViewHolder(@NonNull View itemView) {
        super(itemView);

        splitTitle = itemView.findViewById(R.id.split_item_title_textView);
        imageView = itemView.findViewById(R.id.split_item__imageView);
        view = itemView;

    }


    public TextView getSplitTitle()
    {
        return splitTitle;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
