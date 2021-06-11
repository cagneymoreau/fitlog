package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.recycleviews.RecyclerTouchListener;
import com.cagneymoreau.fitlog.recycleviews.Trophies_Adapter;

import java.util.ArrayList;

/**
 * opened from settings menu
 *
 * A collection of entertaining images users recieve as rewards
 * for using certain app features
 *
 */


public class Trophies extends Fragment {

    public static final String TAG = "Trophies";

    private View fragView;
    Controller controller;

    RecyclerView recyclerView;
    Trophies_Adapter trophies_adapter;
    RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.easy_history, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        buildRecycleView();

        return fragView;

    }



    private void buildRecycleView()
    {

        ArrayList<Integer> trophies = controller.getTrophies();

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        trophies_adapter = new Trophies_Adapter(trophies, this);
        recyclerView.setAdapter(trophies_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                new Trophy_Dialog(trophies.get(position));

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                // TODO: 5/27/2021 copy split

            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));




    }




}