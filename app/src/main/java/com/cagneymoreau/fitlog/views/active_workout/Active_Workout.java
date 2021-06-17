package com.cagneymoreau.fitlog.views.active_workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.active_workout.recycleview.Active_Workout_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Child window of workout holder.
 * Displays the current days movements with fields the user can enter
 *
 * Display composed of a recycleview
 *
 */
// TODO: 6/15/2021 deletes the data on next
// TODO: 6/15/2021 doesnt hide keyboard on next

public class Active_Workout extends Fragment {

    public static final String TAG = "Active_Workout";

    private View fragView;
    Controller controller;

    ArrayList<ArrayList<String>> currentWorkout;
    ArrayList<ArrayList<EditText>> editors = new ArrayList<>();
    TextView debug; // TODO: 6/15/2021 remove

    RecyclerView recyclerView;
    Active_Workout_Adapter active_workout_adapter;
    RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);


        MainActivity mainActivity = (MainActivity) getActivity();

        controller = mainActivity.getConroller();

        buildRecycleView();

        return fragView;

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO: 6/16/2021 save workout data
    }

    private void buildRecycleView()
    {

        debug = fragView.findViewById(R.id.chooser_textView);
        debug.setText("go");

        currentWorkout = controller.getCurrentWorkout();

        for (int i = 0; i < currentWorkout.size(); i++) {
            currentWorkout.get(i).add("");
            editors.add(new ArrayList<>());
        }

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        active_workout_adapter = new Active_Workout_Adapter(currentWorkout, this);
        recyclerView.setAdapter(active_workout_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {
                //nothing happens here
                Log.d(TAG, "onClick: " + position); // TODO: 6/15/2021 remove

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {



            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));


    }



    public void newEntry(int whichList)
    {
        currentWorkout.get(whichList).add("");
        pullEdits();
        for (int i = 0; i < editors.size(); i++) {
            editors.get(i).clear();
        }
        active_workout_adapter.notifyDataSetChanged();

    }

    public void addEditor(EditText editText, int row)
    {
        editors.get(row).add(editText);

    }

    private void pullEdits()
    {
        for (int i = 0; i < editors.size(); i++) {

            for (int j = 0; j < editors.get(i).size(); j++) {
                currentWorkout.get(i).set(j+1, editors.get(i).get(j).getText().toString());
            }

        }
    }

}
