package com.cagneymoreau.fitlog.views.period_viewer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.Instructions_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.history_viewer.History_Viewer;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.History_Adapter;
import com.cagneymoreau.fitlog.views.period_viewer.recycleview.Period_Adapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import kotlin.Triple;

/**
 * Remember this is not a long term storage app
 *
 * Display list of recent workouts
 *
 * open them as editable active workouts or as read only
 *
 */

public class History_Periods extends MyFragment {

    public static final String TAG = "History_Periods";

    private View fragView;
    Controller controller;

    ArrayList<Triple<Long, Long, Boolean>> recentPeriods;
    RecyclerView recRecyclerView;
    Period_Adapter recHistory_adapter;
    RecyclerView.LayoutManager recLayoutManager;

    History_Viewer hv;

    MyFragment curr;

    TextView nodataTextView;

    History_Periods mHistoryChooser = this;

    ArrayList<HistoryItem> toDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.history_chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();
        //coordinatorLayout = fragView.findViewById(R.id.co)

        nodataTextView = fragView.findViewById(R.id.no_hist_data_textView);

        buildPeriodsRecycleView();

        curr = this;

        return fragView;

    }



    private void buildPeriodsRecycleView()
    {
        toDelete = new ArrayList<>();

        recentPeriods = controller.getBackups();

        if (recentPeriods.size() == 0){
            nodataTextView.setText("No History Exists");
            nodataTextView.setPadding(0,100,0,0);
        }

        recentPeriods.add(0,null);


        recRecyclerView = fragView.findViewById(R.id.histChooser_recycleView);
        recLayoutManager = new LinearLayoutManager(getActivity());
        recRecyclerView.setLayoutManager(recLayoutManager);

        recHistory_adapter = new Period_Adapter(recentPeriods);
        recRecyclerView.setAdapter(recHistory_adapter);

        recRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                if (position == 0){

                    if (!controller.getSubscription().grantAccess(curr)){
                        return;
                    }

                    if ( controller.getSpecificSplitEditable(0) == null || controller.getSpecificSplitEditable(0).size() == 0){

                        new Instructions_Dialog("Workouts are pre planned. You must first create a workout").show(getChildFragmentManager(), "instructions dialog");

                    }else{
                        LocalDate d = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
                        DatePickerDialog pick = new DatePickerDialog(fragView.getContext(), dateSetListener,d.getYear(), d.getMonth().getValue(), d.getDayOfMonth());
                        pick.show();
                    }



                }else{

                    Bundle b = new Bundle();
                    b.putInt("period", position-1);
                    Navigation.findNavController(fragView).navigate(R.id.action_global_history_Chooser, b);

                }


            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                    //nothing

            }



        }));

    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    Instant instant = LocalDate.of(year,month,dayOfMonth).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

                    long milli = instant.toEpochMilli();

                    Bundle b = new Bundle();
                    b.putLong("time",milli);

                    Navigation.findNavController(fragView).navigate(R.id.action_global_day_Chooser_Fragment, b);


                }
            };





}
