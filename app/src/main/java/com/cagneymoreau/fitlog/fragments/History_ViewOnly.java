package com.cagneymoreau.fitlog.fragments;

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
import com.google.android.material.tabs.TabLayout;

/**
 * Child window of workout holder
 * Display past workouts so the user can understand how this workout
 * compares to previous in real time
 */
public class History_ViewOnly extends Fragment {


    public static final String TAG = "History_ViewOnly";

    private View fragView;
    Controller controller;

    TabLayout tabLayout;
    ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.history_viewer, container, false);

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
        tabLayout.addTab(tabLayout.newTab().setText("Choose"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(),0);
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

}
