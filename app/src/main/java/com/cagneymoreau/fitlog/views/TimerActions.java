package com.cagneymoreau.fitlog.views;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.generic_recycler.Generic_Adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Child window of workout holder
 * Handy timers for measuring rest or sets
 *
 * simple stopwatch
 *
 * interval timer as delay x counts
 *
 *
 */

public class TimerActions extends Fragment {

    public static final String TAG = "TimerActions";

    private View fragView;
    Controller controller;

    Button startStopWatch_Btn, resetStopWatch_Btn, creatTimer_Btn, startTimer_Btn;

    EditText delay_ET, count_ET;

    TextView stopwatch_TV;

    RecyclerView recyclerView;
    Generic_Adapter generic_adapter;
    RecyclerView.LayoutManager layoutManager;


    long stopWatchStartTime, stopwatch_HoldAmount;

    boolean stopwatchRunning = false;
    Timer stopwatch;

    Timer upTimer;
    boolean timerRunning = false;
    int count = 0;
    int repeat;
    ArrayList<Pair<Integer, Integer>> timers;
    ArrayList<String> uiView;

    MediaPlayer mp;

    public TimerActions()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.timer_stopwatch_layout, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        mp = MediaPlayer.create(getContext(), R.raw.timer);

        startStopWatch_Btn = fragView.findViewById(R.id.startStopWatch_Button);
        creatTimer_Btn = fragView.findViewById(R.id.createTimer_Button);
        startTimer_Btn = fragView.findViewById(R.id.executeTimer_Button);
        resetStopWatch_Btn = fragView.findViewById(R.id.stopwatchReset_Button);

        delay_ET = fragView.findViewById(R.id.delay_EditText);
        count_ET = fragView.findViewById(R.id.count_EditText);

        stopwatch_TV = fragView.findViewById(R.id.stopwatchTextView);

        buildUI();
        buildRecycleView();

        return fragView;

    }




    private void buildUI()
    {

        startStopWatch_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStopWatch();
            }
        });

        resetStopWatch_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopWatch();
            }
        });

        creatTimer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTimer();
            }
        });

        startTimer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTimer();
            }
        });


    }


    private void buildRecycleView()
    {

        timers = controller.getTimers();

        uiView = new ArrayList<>();
        for (int i = 0; i < timers.size(); i++) {
            uiView.add(buildText(timers.get(i)));
        }


        recyclerView = fragView.findViewById(R.id.timer_RecycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        generic_adapter = new Generic_Adapter(uiView);
        recyclerView.setAdapter(generic_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                if (generic_adapter.getChosen() == position){
                    return;
                }

               generic_adapter.setChosen(position);
               generic_adapter.notifyDataSetChanged();

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                deleteTimer(position);

            }


        }));




    }


    private String buildText(Pair<Integer, Integer> inp)
    {
        return inp.first + " seconds - repeated " + inp.second;
    }

    private void deleteTimer(int pos)
    {
        timers.remove(pos);
        uiView.remove(pos);
        controller.deleteTimer(pos);

    }



    private void toggleTimer()
    {

        if (timerRunning){

            startTimer_Btn.setText("start");

            upTimer.cancel();
            timerRunning = false;

            return;

        }


        if (generic_adapter.getChosen() == -1){
            try {
                addNewTimer();
            }catch (Exception e){
                // TODO: 11/4/2021 error tried starting timer with nothing filled in
                return;
            }

            if (generic_adapter.getChosen() == -1){
                return;
            }

        }

        upTimer = new Timer();

        startTimer_Btn.setText("stop");

        //count = 0;

        long delay = timers.get(generic_adapter.getChosen()).first;
        delay *= 1000;
        repeat = timers.get(generic_adapter.getChosen()).second;

        upTimer.scheduleAtFixedRate(new TimerTask() {

            int tally = 0;

            @Override
            public void run() {

                mp.start();
                tally++;

                if (tally == repeat)  timerStop();

            }
        },0,delay);

        timerRunning = true;
    }

    private void addNewTimer()
    {


        int delay = Integer.valueOf(delay_ET.getText().toString());
        int repeat = Integer.valueOf(count_ET.getText().toString());

        if (delay < 1 || delay > 60 || repeat < 1 || repeat > 60){
            delay_ET.setText("");
            count_ET.setText("");

            Toast.makeText(this.getContext(), "Values between 1 and 60 only", Toast.LENGTH_SHORT).show();

        }

        timers.add(new Pair<>(delay, repeat));
        controller.addTimer(delay, repeat);
        uiView.add(buildText(timers.get(timers.size()-1)));

        generic_adapter.setChosen(timers.size()-1);
        generic_adapter.notifyDataSetChanged();

        delay_ET.setText("");
        count_ET.setText("");

    }

    private void timerStop()
    {
        this.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            toggleTimer();

        }
    });

    }

    //region -------------------------- stopwatch

    private void toggleStopWatch()
    {

        if (stopwatchRunning){

            startStopWatch_Btn.setText("Start");


            stopwatch.cancel();
            stopwatch_HoldAmount += System.currentTimeMillis() - stopWatchStartTime;

        }else{
            startStopWatch_Btn.setText("Stop");

            stopwatch = new Timer();

        stopWatchStartTime = System.currentTimeMillis();



                  stopwatch.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    updateStopWatchDisplay();

                }
            }, 0,1000);


        }

        stopwatchRunning = !stopwatchRunning;
    }

    private void updateStopWatchDisplay()
    {
        long currRun = System.currentTimeMillis() - stopWatchStartTime;
        currRun += stopwatch_HoldAmount;

        long hours = TimeUnit.MILLISECONDS.toHours(currRun);
        currRun -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currRun);
        currRun -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currRun);

        String minute, second;

        if (minutes < 10) {
            minute = "0" + minutes;
        }else {
            minute = String.valueOf(minutes);
        }

        if (seconds < 10){
            second = ":0" + seconds;
        }else{
            second = ":" + seconds;
        }

        String done = minute + second;


        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopwatch_TV.setText(done);

            }
        });

    }

    private void resetStopWatch()
    {
        stopwatch_HoldAmount = 0;
        stopwatch.cancel();
        stopwatchRunning = false;
        stopwatch_TV.setText("00:00");


    }

    //endregion



}
