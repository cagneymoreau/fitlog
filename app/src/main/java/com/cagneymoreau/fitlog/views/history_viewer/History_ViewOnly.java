package com.cagneymoreau.fitlog.views.history_viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Child window of workout holder
 * Display past workouts so the user can understand how this workout
 * compares to previous in real time
 *
 * Shows different tabs, a main tab that has lists to choose from
 * and new tabs that display data from those lists
 */
public class History_ViewOnly extends Fragment {


    public static final String TAG = "History_ViewOnly";

    private View fragView;
    Controller controller;

    TabLayout tabLayout;
    ViewPager viewPager;

    ArrayList<HistoryItem> openList;

    TabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.history_viewer, container, false);

        openList = new ArrayList<>();

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        tabLayout = fragView.findViewById(R.id.history_TabLayout);
        viewPager = fragView.findViewById(R.id.history_ViewPager);

        buildTabs();

        return fragView;

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void buildTabs()
    {

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager(),0);

        tabLayout.addTab(tabLayout.newTab().setText("Choose"));
        Easy_History easy_history = new Easy_History(this);
        adapter.addItem(easy_history);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    /**
     *
     * @param hist a history item user wishes to add
     */
    public void addNewTab(HistoryItem hist)
    {
        boolean found = false;
        int pos = 0;

        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).uid == hist.uid){
                found = true;
                pos = i;
            }
        }

        if (found){
            viewPager.setCurrentItem(pos);
        }else{

            tabLayout.addTab(tabLayout.newTab().setText(hist.date.toString()));
            Read_History read_history = new Read_History(hist.uid);
            adapter.addItem(read_history);

        }



    }


}
