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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.Description_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
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


public class Active_Workout extends MyFragment {

    public static final String TAG = "Active_Workout";

    private View fragView;
    Controller controller;

    ArrayList<ArrayList<String>> currentWorkout;
    ArrayList<ArrayList<EditText>> editors = new ArrayList<>();
    TextView debug; // TODO: 6/15/2021 remove

    RecyclerView recyclerView;
    Active_Workout_Adapter active_workout_adapter;
    RecyclerView.LayoutManager layoutManager;

    TextView titleTv, bottomTv;
    ArrayList<String> title;

    boolean constructed = false;

    MyFragment parent = this;


    int noItem = -4;
    int toEdit = noItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        controller = mainActivity.getConroller();

        titleTv = fragView.findViewById(R.id.chooser_title_textView);
        float sd = getResources().getDisplayMetrics().scaledDensity;
        titleTv.setTextSize((titleTv.getTextSize()/sd) * 1.2f);
        titleTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toEdit = -1;
                new Description_Dialog("Rename Workout?", titleTv.getText().toString(), parent, false).show(getChildFragmentManager(), " Edit title Dialog");
                return false;
            }
        });

        bottomTv = fragView.findViewById(R.id.chooser_textView);
        bottomTv.setText("Enter your workout data");

        buildRecycleView();

        return fragView;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (constructed) deconstruct();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        pullEdits();
      if (constructed) deconstruct();
        // TODO: 11/9/2021 update checlist

        controller.updatecurrentWorkout(currentWorkout);

    }

    private void buildRecycleView()
    {
        constructed = true;

        debug = fragView.findViewById(R.id.chooser_textView);
        //debug.setText("debug.delete me");

        currentWorkout = controller.getCurrentWorkout();

        title = currentWorkout.get(0);
        currentWorkout.remove(0);
        titleTv.setText(title.get(0));


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
                //Log.d(TAG, "onClick: " + position); // TODO: 6/15/2021 remove
            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

            }

        }));


    }

    private void deconstruct()
    {
        constructed = false;

        for (int i = 0; i < currentWorkout.size(); i++) {
            currentWorkout.get(i).remove(currentWorkout.get(i).size()-1);
        }
        editors.clear();
        currentWorkout.add(0, title);
    }


    public void newEntry(int whichList)
    {
        currentWorkout.get(whichList).add("");
        pullEdits();
        updateDataSet();

        controller.updatecurrentWorkout(currentWorkout);

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


    public void insertFreeStyle(int pos)
    {
        ArrayList<String> free = new ArrayList<>();
        free.add("freestyle");
        free.add("");
        currentWorkout.add(pos+1, free);
        editors.add(pos+1, new ArrayList<>());
        updateDataSet();

    }

    public void changeMoveName(int pos)
    {   toEdit = pos;
        new Description_Dialog("Rename Movement?", currentWorkout.get(pos).get(0), parent, false).show(getChildFragmentManager(), " Edit title Dialog");

    }


    @Override
    public void sendDialogResult(String result) {

        if (toEdit < 0){
            titleTv.setText(result);
        }else{
            currentWorkout.get(toEdit).set(0, result);
        }

        updateDataSet();

        toEdit = noItem;
    }

    /**
     * You must clear editors because updating the dataset will build all new and youll have index overruns
     */
    private void updateDataSet()
    {
        for (int i = 0; i < editors.size(); i++) {
            editors.get(i).clear();
        }
        active_workout_adapter.notifyDataSetChanged();

    }


}
