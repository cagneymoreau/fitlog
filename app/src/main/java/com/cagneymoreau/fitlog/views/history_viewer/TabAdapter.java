package com.cagneymoreau.fitlog.views.history_viewer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * adapter for our history view so the user can view multiple previous workouts simultaneously
 *
 * holdes fragments and associates their display with pressing the tab page buttons
 *
 */

public class TabAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> openTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);


        openTabs = new ArrayList<>();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

     return openTabs.get(position);

    }

    @Override
    public int getCount() {
        return openTabs.size();
    }


    public void addItem(Fragment fragment)
    {
        openTabs.add(fragment);
    }

    public void removeItem()
    {

    }


}
