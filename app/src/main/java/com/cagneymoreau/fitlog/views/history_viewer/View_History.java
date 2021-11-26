package com.cagneymoreau.fitlog.views.history_viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.history_viewer.gridrecycle.Grid_Adapter;

import java.util.ArrayList;

/**
 * Replaces read_history with recycleview gridview so you can scroll end define grid easier
 */
public class View_History extends Fragment {

    public static final String TAG = "View_History";

    private View fragView;
    Controller controller;

    TextView read_status_Tv;

    ArrayList<ArrayList<String>> record;

    ArrayList<String> preparedrecords;

    private int columnCount;

    private int uid;

    RecyclerView recyclerView;
    Grid_Adapter grid_adapter;
    GridLayoutManager gridLayoutManager;


    public View_History(int uid)
    {
        this.uid = uid;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.view_history, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        read_status_Tv = fragView.findViewById(R.id.read_status_TextView);
        recyclerView = fragView.findViewById(R.id.viewHist_recycleView);


        requestData();



        return fragView;

    }






    private void requestData()
    {
        new Thread(()-> {

            record = new ArrayList<>(controller.openExistingWorkoutRecord(uid).workout);

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buildView();

                }
            });


        }).start();

    }



    private void buildView()
    {

        buildArrayList();


        gridLayoutManager = new GridLayoutManager(getContext(), columnCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        grid_adapter = new Grid_Adapter(preparedrecords);
        recyclerView.setAdapter(grid_adapter);

    }

    //add empty columns as needed
    private void buildArrayList()
    {
        columnCount = 0;preparedrecords = new ArrayList<>();

        for (int i = 0; i < record.size(); i++) {

            if (record.get(i).size() > columnCount){
                columnCount = record.get(i).size();
            }
        }

        for (int i = 0; i < record.size(); i++) {

            preparedrecords.addAll(record.get(i));

            for (int j = 0; j < (columnCount- record.get(i).size()); j++) {
                preparedrecords.add(" - ");
            }

        }


    }



}
