package com.cagneymoreau.fitlog.views.day_choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.split_design.recycleview.Split_Adapter;

import java.util.ArrayList;

/**
 *   This fragment display the day options available from the current split
 *   New workout records are created s this fragment leads to an active workout by displaying the workout_holder frag
 */

public class Day_Chooser_Fragment extends Fragment {


    private View fragView;
    Controller controller;

    TextView chooserInstructions;

    RecyclerView recyclerView;
    Choose_Adapter choose_adapter;
    RecyclerView.LayoutManager layoutManager;

    long time = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        chooserInstructions = fragView.findViewById(R.id.chooser_textView);
        chooserInstructions.setText("Select todays workout");

        try {
            time = getArguments().getLong("time", -1);
        }catch (Exception e){
            //do nothing
        }


        buildRecycleView();

        return fragView;
    }



    private void buildRecycleView()
    {
        //The 0 index split is the current split
        //List<String> checklists = controller.getSplitsList();

        ArrayList<ArrayList<String>> days = controller.getSpecificSplitEditable(0);

        ArrayList<String> prepared = buildDaysList(days);

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        choose_adapter = new Choose_Adapter(prepared, this);
        recyclerView.setAdapter(choose_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                if (position == 0) return;

                controller.setSplitToModify(position);

                controller.createNewWorkoutRecord(position, time );
                Bundle b = new Bundle();
                b.putInt("uid", -1);
                Navigation.findNavController(fragView).navigate(R.id.action_day_Chooser_Fragment_to_workout_Holder_Fragment, b);

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                // TODO: 5/27/2021 copy split

            }


        }));


    }

    //no editing here so we can be sure that list is static after on resume is called
    private ArrayList<String> buildDaysList(ArrayList<ArrayList<String>> fromsql)
    {
        ArrayList<String> response = new ArrayList<>();

        for (int i = 0; i < fromsql.size(); i++) {

            response.add(fromsql.get(i).get(0));

        }
        return response;
    }




}
