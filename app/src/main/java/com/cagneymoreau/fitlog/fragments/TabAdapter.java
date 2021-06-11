package com.cagneymoreau.fitlog.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * adapter for our histlry view so the user can view multiple previous workouts simultaneously
 */

public class TabAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs = 3;

    Easy_History easyHistory;
    ArrayList<Read_History> openTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        easyHistory = new Easy_History();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return easyHistory;
        }else{
            return openTabs.get(position -1);
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }


    public void addTab(int idunno)
    {
        Read_History read_history = new Read_History();
        openTabs.add(read_history);
    }


}
