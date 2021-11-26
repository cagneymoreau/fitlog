package com.cagneymoreau.fitlog.views.main_menu.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;

import java.util.ArrayList;
import java.util.List;

public class Main_Menu_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ArrayList<String>> data;
    Fragment fragment;

    public Main_Menu_Adapter(List<ArrayList<String>> dataField, Fragment fragment)
    {
        data = dataField;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       View v = inflater.inflate(R.layout.split_card, parent, false);
       viewHolder = new MainMenu_ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MainMenu_ViewHolder mainMenu_viewHolder = (MainMenu_ViewHolder) holder;
        ArrayList<String> inner = data.get(position);

        mainMenu_viewHolder.getSplitTitle().setText(inner.get(0));
        mainMenu_viewHolder.getSplitTitle().setTextSize(30);

        if (inner.size() > 1){
            mainMenu_viewHolder.getSplitDesc().setText(inner.get(1));
            mainMenu_viewHolder.getSplitDesc().setTextSize(20);
        }

        switch (position) {

            case 0:
                mainMenu_viewHolder.getImageView().setImageResource(R.drawable.check);
                mainMenu_viewHolder.getImageView().setMaxHeight(100);
                break;

            case 1:
                mainMenu_viewHolder.getImageView().setImageResource(R.drawable.gear);
                mainMenu_viewHolder.getImageView().setMaxHeight(100);

                break;


            case 2:
                mainMenu_viewHolder.getImageView().setImageResource(R.drawable.dumbellv2);
                mainMenu_viewHolder.getImageView().setMaxHeight(100);
                break;

            case 3:
                mainMenu_viewHolder.getImageView().setImageResource(R.drawable.hourglass);
                mainMenu_viewHolder.getImageView().setMaxHeight(100);
                break;


        }




    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
