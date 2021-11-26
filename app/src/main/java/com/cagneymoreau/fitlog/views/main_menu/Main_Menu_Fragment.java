package com.cagneymoreau.fitlog.views.main_menu;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.Instructions_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.main_menu.recycleview.Main_Menu_Adapter;
import com.cagneymoreau.fitlog.views.split_design.recycleview.Split_Adapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu is menu buttons only
 * checklist builder
 * split builder
 * start workout
 * body measure
 * statistics
 *
 */

public class Main_Menu_Fragment extends MyFragment {

    private View fragView;
    Controller controller;

    //Button checkListBuildEdit, splitBuildEdit, startWorkout, bodyMeasure, statistics, historyButton;

    //TextView checkListTextView, splitTextView;

    RecyclerView recyclerView;
    Main_Menu_Adapter mm_Adapter;
    RecyclerView.LayoutManager layoutManager;

    ImageView ratingsSolicitImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.main_menu, container, false);
        ratingsSolicitImageView = fragView.findViewById(R.id.rating_Imageview);


                MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        addData();
        checkRating();

        return fragView;

    }


    private void addData()
    {

        buildRecycleView();

        //buildButtons();

        //buildTextViews();

    }


    private void buildRecycleView() {
        List<ArrayList<String>> mainList = new ArrayList<>();

        //checklist

        Pair<List<String>, Boolean> inp = controller.getCheckListsChoice();
        List<String> checkLists = inp.first;
        ArrayList<String> check = new ArrayList<>();
        check.add("Design a checklist");
        if (!inp.second) {
            check.add("Not using a checklist");
        } else if (checkLists != null && checkLists.size() > 1) {
            check.add("Current: " + checkLists.get(0));
        }

            mainList.add(check);

            //split

            ArrayList<String> split = new ArrayList<>();
            split.add("Design a workout");

            List<String> splits = controller.getSplitsList();

            if (splits != null && splits.size() > 0) {
                split.add("Current: " + splits.get(0));
            }

            mainList.add(split);

            //start

            ArrayList<String> begin = new ArrayList<>();
            begin.add("Add a workout");

            mainList.add(begin);

            //workouthistory
            ArrayList<String> wkhst = new ArrayList<>();
            wkhst.add("Edit existing workout");

            mainList.add(wkhst);

            //body measurement
            ArrayList<String> bdymsr = new ArrayList<>();
            bdymsr.add("Body Measurements");

            //mainList.add(bdymsr);

            //statistics
            ArrayList<String> stat = new ArrayList<>();
            stat.add("Statistics");

            //mainList.add(stat);


            recyclerView = fragView.findViewById(R.id.mainmenu_recycleView);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mm_Adapter = new Main_Menu_Adapter(mainList, this);
            recyclerView.setAdapter(mm_Adapter);


            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position, float x, float y) {

                   onClickResponse(position);

                }

                @Override
                public void onLongClick(View view, int position, float x, float y) {

                }


            }));


        }




        private void onClickResponse(int position)
        {

            switch (position)
            {
                case 0:
                   NavController n =  Navigation.findNavController(fragView);


                            n.navigate(R.id.action_main_Menu_Fragment_to_checkList_Chooser_Fragment);
                    break;

                case 1:
                    Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_routine_Builder_Fragment);
                    break;

                case 2:

                    if (!controller.getSubscription().grantAccess(this)){
                        return;
                    }

                    if ( controller.getSpecificSplitEditable(0) == null || controller.getSpecificSplitEditable(0).size() == 0){

                        new Instructions_Dialog("Workouts are pre planned. You must first create a workout").show(getChildFragmentManager(), "instructions dialog");

                    }else{
                        Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_split_Chooser_Fragment);
                    }
                    break;


                case 3:
                    Navigation.findNavController(fragView).navigate(R.id.action_global_history_Periods);

                    break;

                case 4:
                    Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_body_Measure);

                    break;

                case 5:
                    Navigation.findNavController(fragView).navigate(R.id.action_main_Menu_Fragment_to_statistics);
                    break;
            }
        }


    private void checkRating()
    {
        if (controller.showRating()){

            ratingsSolicitImageView.setImageResource(R.drawable.rating_images);
            ratingsSolicitImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    controller.ratingRecieved();
                }
            });



        }else{

            ratingsSolicitImageView.setVisibility(View.INVISIBLE);
        }

    }


}
