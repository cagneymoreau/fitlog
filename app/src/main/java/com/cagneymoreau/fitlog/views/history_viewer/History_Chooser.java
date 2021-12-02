package com.cagneymoreau.fitlog.views.history_viewer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.Description_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.History_Adapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Remember this is not a long term storage app
 *
 * Display list of recent workouts
 *
 * open them as editable active workouts or as read only
 *
 */

public class History_Chooser extends MyFragment {

    public static final String TAG = "History_Chooser";

    private View fragView;
    Controller controller;


    ArrayList<HistoryItem> recentRecords;
    RecyclerView recRecyclerView;
    History_Adapter recHistory_adapter;
    RecyclerView.LayoutManager recLayoutManager;

    History_Viewer hv;

    TextView nodataTextView;

    History_Chooser mHistoryChooser = this;

    ArrayList<HistoryItem> toDelete;

    boolean saved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.history_chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();
        //coordinatorLayout = fragView.findViewById(R.id.co)

        nodataTextView = fragView.findViewById(R.id.no_hist_data_textView);

        int period = -1;

        try {
            period = getArguments().getInt("period");
        }catch (Exception e){
            //do nothing
        }

        controller.getRecentList(this, period);

        saved = false;

        return fragView;

    }



    private void buildRecentRecycleView()
    {
        toDelete = new ArrayList<>();

        if (recentRecords.size() == 0){
            nodataTextView.setText("No History Exists");
        }

        //remove the current workout from the history
        if (hv != null) {
            for (int i = 0; i < recentRecords.size(); i++) {
                if (recentRecords.get(i).uid == controller.getCurrentUID()) {
                    recentRecords.remove(i);
                }
            }
        }

        recRecyclerView = fragView.findViewById(R.id.histChooser_recycleView);
        recLayoutManager = new LinearLayoutManager(getActivity());
        recRecyclerView.setLayoutManager(recLayoutManager);

        recHistory_adapter = new History_Adapter(recentRecords);
        recRecyclerView.setAdapter(recHistory_adapter);

        recRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {


                if (hv == null){

                    Bundle b = new Bundle();
                    b.putInt("uid", recentRecords.get(position).uid);
                    Navigation.findNavController(fragView).navigate(R.id.action_history_Chooser_to_workout_Holder_Fragment, b);

                }else{


                   hv.placeReadHistory(recentRecords.get(position).uid);

                }

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {


            }



        }));

        //only allow in editing not in history viewer
        if (hv == null){
            enableSwipeDelete();
        }


    }


    public void setHistoryViewer(History_Viewer historyViewer)
    {
        hv = historyViewer;
    }




    public void setHistory(ArrayList<HistoryItem> hist)
    {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recentRecords = hist;
                buildRecentRecycleView();
            }
        });


    }

    public void updateList(int position)
    {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        recentRecords.remove(position);
        recHistory_adapter.notifyDataSetChanged();
            }
        });
    }


    private void enableSwipeDelete()
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final HistoryItem item = recHistory_adapter.getData().get(position);

                recHistory_adapter.removeItem(position);

                toDelete.add(item);

                Snackbar snackbar = Snackbar
                        .make(fragView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        toDelete.remove(item);
                        recHistory_adapter.restoreItem(item, position);
                        recRecyclerView.scrollToPosition(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recRecyclerView);
    }





    private void saveAll()
    {
        if (saved) return;
        for (HistoryItem i :
                toDelete) {

            controller.deleteWorkoutRecord(i.uid);

        }
        saved = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveAll();
    }


    @Override
    public void onPause() {
        super.onPause();
        saveAll();
    }
}
