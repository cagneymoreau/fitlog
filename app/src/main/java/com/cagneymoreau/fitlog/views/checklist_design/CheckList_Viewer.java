package com.cagneymoreau.fitlog.views.checklist_design;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.checklist_design.recycleview.CheckList_Review_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Here the user can view their checklist and add notes.
 */
public class CheckList_Viewer extends Fragment {

    private final static String TAG = "CheckList_viewer";

    private View fragView;
    Controller controller;


    EditText notesEditText;

    RecyclerView recyclerView;
    CheckList_Review_Adapter checkList_adapter;
    RecyclerView.LayoutManager layoutManager;

    List<Pair<String, Boolean>> checklistinputs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser_buttons, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();


        notesEditText = fragView.findViewById(R.id.chooser_editText);

        return fragView;

    }

    @Override
    public void onResume() {
        super.onResume();
        buildRecycleView();
        buildotherUI();
    }

    private void buildRecycleView()
    {
        //get the active checklist
        ArrayList<String> list = controller.getSpecificCheckListEditable(0);
        for (String s :
                list) {
            checklistinputs.add(new Pair<>(s, true));
        }


        if (checklistinputs == null)
        {
            checklistinputs = new ArrayList<>();
            checklistinputs.add(new Pair<>("This checklist is empty!", true));
        }


        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
       checkList_adapter = new CheckList_Review_Adapter(checklistinputs, this);
        recyclerView.setAdapter(checkList_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                checklistinputs.set(position, new Pair<>(checklistinputs.get(position).first, !checklistinputs.get(position).second));
                checkList_adapter.notifyDataSetChanged();

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                // TODO: 5/27/2021 copy split

            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }));


    }


    private void buildotherUI()
    {
        String notes = controller.getNotes();
        if (notes != null){
            notesEditText.setText(notes);
        }



    }


    @Override
    public void onPause() {
        super.onPause();
        // TODO: 6/7/2021 save all ui data to controller
        Log.d(TAG, "onPause: save ui data to controller");
    }
}
