package com.cagneymoreau.fitlog.views.history_viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.Workout_Holder_Fragment;


/**
 * Child view of active workout. Sshows recent with quickview beneath
 *
 * // TODO: 11/4/2021  prevent todays workout from showing
 */


public class History_Viewer extends Fragment {

    public static final String TAG = "History_Chooser";

    private View fragView;

    Controller controller;

    History_Chooser historyChooser;

    Workout_Holder_Fragment wkHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.history_viewer, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        controller = mainActivity.getConroller();

        historyChooser = new History_Chooser();

        historyChooser.setHistoryViewer(this);

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.hist_viewer_top, historyChooser).addToBackStack(null).commit();

        if (wkHolder != null && wkHolder.getCurrUID() != -1)
        {
            placeReadHistory(wkHolder.getCurrUID());
        }

        return fragView;

    }


    public void placeReadHistory(int theUID)
    {
        if (wkHolder != null){
            wkHolder.setCurrUID(theUID);
        }

        //Read_History rh = new Read_History(theUID);
        View_History vh = new View_History(theUID);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.hist_viewer_bottom, vh).addToBackStack(null).commit();

    }

    public void setWkHolder(Workout_Holder_Fragment wk)
    {
        wkHolder = wk;

    }



}
