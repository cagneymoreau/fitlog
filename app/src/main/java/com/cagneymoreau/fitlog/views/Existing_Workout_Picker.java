package com.cagneymoreau.fitlog.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.checklist_design.recycleview.CheckList_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * This opens an existing workout as an active workout so the user can continue or edit or whatever
 */
public class Existing_Workout_Picker extends MyFragment {

    public static final String TAG = "Existing_Workout_Picker";


    private View fragView;
    Controller controller;

    TextView chooserInstructions;

    RecyclerView recyclerView;
    CheckList_Adapter checkList_adapter;
    RecyclerView.LayoutManager layoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        chooserInstructions = fragView.findViewById(R.id.chooser_textView);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        chooserInstructions.setText("Swipe to delete or tap to edit");

        return fragView;

    }

    @Override
    public void onResume() {
        super.onResume();
        controller.getRecentWorkoutDesc(this);

    }

    private void buildRecycleView(ArrayList<String> records)
    {
        if (records == null || records.size() == 0){
            chooserInstructions.setText("Nothing to Display");
            return;
        }

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        checkList_adapter = new CheckList_Adapter(records);
        recyclerView.setAdapter(checkList_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                // TODO: 6/7/2021 do something
                Log.d(TAG, "onClick: do something");

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                controller.selectCheckList(position);

            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));


    }


    // we don't need to worry about updating database onpause as database updates are done as changes are made
    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void sendSqlResults(ArrayList<String> result) {
      buildRecycleView(result);
    }
}