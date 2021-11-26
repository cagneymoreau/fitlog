package com.cagneymoreau.fitlog.views.main_menu.recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

public class MainMenu_ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView splitTitle, splitDesc;
        private View view;

    public MainMenu_ViewHolder(@NonNull View itemView) {
        super(itemView);

        splitTitle = itemView.findViewById(R.id.split_item_title_textView);
        splitDesc = itemView.findViewById(R.id.split_item_descrip_textView);
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

    public TextView getSplitDesc()
    {
        return splitDesc;
    }
}
