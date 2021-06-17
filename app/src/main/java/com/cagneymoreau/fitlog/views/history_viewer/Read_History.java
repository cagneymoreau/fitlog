package com.cagneymoreau.fitlog.views.history_viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

import java.util.ArrayList;

/**
 * Displays a non interact-able grid of a past workouts different sets and reps for a quick review
 *
 */
// TODO: 6/16/2021 does frag need refresh if buildview called?

public class Read_History extends Fragment {

    public static final String TAG = "Easy_History";

    private View fragView;
    Controller controller;

    TableLayout tableLayout;

    private int uid;

    public Read_History(int uid)
    {
        this.uid = uid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.easy_history, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        tableLayout = fragView.findViewById(R.id.read_history_TableLayout);

        buildView();

        return fragView;

    }


    public void buildView()
    {
        ArrayList<ArrayList<String>> record = new ArrayList<>(controller.openExistingWorkoutRecord(uid).workout);

        if (record.size() == 0) return;

        for (int i = 0; i < record.size(); i++) {

            addRow(record.get(i));
        }

    }

    private void addRow(ArrayList<String> row)
    {
        TableRow tb = new TableRow(tableLayout.getContext());

        for (int i = 0; i < row.size(); i++) {

            TextView tv = new TextView(tb.getContext());
            tv.setText(row.get(i));
            tb.addView(tv);
            // TODO: 6/16/2021 params?
        }

        tableLayout.addView(tb);
    }



}
