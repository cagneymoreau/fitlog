package com.cagneymoreau.fitlog.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private static final String TAG = " myFragment";

    boolean displyed;

    public void sendDialogResult(String result)
    {

    }

    public void sendSqlResults(ArrayList<String> result)
    {

    }


    @Override
    public void onResume() {
        super.onResume();
        catchChange(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        catchChange(true);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        catchChange(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        catchChange(false);
        Log.e(TAG, "onDestroyView: ");
    }

    public void catchChange(boolean visible)
    {
        if (visible == displyed){
            return;
        }
        displyed = visible;
        //you now have notice the view is dissapearing that will only be called once

    }

    public boolean getDisplayed()
    {
        return displyed;
    }




}
