package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.util.Pair;
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
import com.cagneymoreau.fitlog.recycleviews.CheckList_Adapter;
import com.cagneymoreau.fitlog.recycleviews.RecyclerTouchListener;
import com.cagneymoreau.fitlog.recycleviews.Trophies_Adapter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import kotlin.Triple;

/**
 * Opens from settings
 *
 * explain data policy
 * recycleview with
 *  create new backup <-- this will create backup up all that arent backed up, unless some specific month is chosen
 *  show recent backups... <-- each of these can be mannualy selected to be backed up and show a backed vs non backeded up warning
 * // TODO: 6/8/2021 if old backup is pressed try resend
 *
 */

public class DataBackup extends Fragment {

    public static final String TAG = "Data_Backup";

    private View fragView;
    Controller controller;

    RecyclerView recyclerView;
    CheckList_Adapter trophies_adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Triple<Long, Long, Boolean>> backups;

    ArrayList<String> humanReadable;

    ArrayList<Integer> toSend = new ArrayList<>();

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

        backups = controller.getBackups();
        buildUIList();

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        trophies_adapter = new CheckList_Adapter(humanReadable, this);
        recyclerView.setAdapter(trophies_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {


                if (position == 0){

                    sendToBackUp();

                }else{

                    // TODO: 6/9/2021 allow user to send certain files for backup
                }

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


    /**
     * Turn an arraylist of pair long long which represent two points in time
     * into a readable description
     */
    private void  buildUIList()
    {
        humanReadable.clear();

        humanReadable.add("Create backups Now!");

        for (int i = 0; i < backups.size(); i++) {

            LocalDate d = LocalDate.ofEpochDay(backups.get(i).getFirst());

            String yet = "  !";

            if (backups.get(i).getThird()) yet = "";

           String description =  d.getMonth() + " " + d.getYear() + yet;

           humanReadable.add(description);

        }


    }



    private void sendToBackUp()
    {

    }


}
