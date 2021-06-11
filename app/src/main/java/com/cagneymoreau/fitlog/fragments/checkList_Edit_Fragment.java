package com.cagneymoreau.fitlog.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.recycleviews.CheckList_Edit_Adapter;
import com.cagneymoreau.fitlog.recycleviews.RecycleDragCallback;
import com.cagneymoreau.fitlog.recycleviews.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Opens a recycleview of the chosen checklist.
 * Each item is clickable to open a pop up for editing
 * or swipable to delete
 *
 * // TODO: 5/25/2021 need to be able to edit the title etc or this wont work 
 *
 */

public class checkList_Edit_Fragment extends MyFragment {


    private View fragView;
    Controller controller;


    TextView editInstructions_TextView, checklistTitle_TextView;
    RecyclerView recyclerView;
    CheckList_Edit_Adapter checkList_adapter;
    RecyclerView.LayoutManager layoutManager;


    private int toEdit = -1;

    List<String> checklistinputs;
    int listItemBeingEdited = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.edit, container, false);

        editInstructions_TextView = fragView.findViewById(R.id.edit_instruction_textView);
        checklistTitle_TextView = fragView.findViewById(R.id.edit_title_textView);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        toEdit = controller.getCheckListToModify();

        return fragView;

    }


    @Override
    public void onResume() {
        super.onResume();
        buildRecycleView(toEdit);
    }


    private void buildRecycleView(int toEdit)
    {

        //This checklist has all fields in place. UI should show empty for unused
        checklistinputs = controller.getSpecificCheckListEditable(toEdit);
        if (checklistinputs == null)
        {
            checklistinputs = new ArrayList<>();
        }

        String title = controller.getSpecificCheckListName(toEdit);
        if (title == null){
            title = " New CheckList";
            checklistinputs.add(0, title);
        }
        MyFragment parent = this;





        checklistinputs.add("Add New Item");


        recyclerView = fragView.findViewById(R.id.edit_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        checkList_adapter = new CheckList_Edit_Adapter(checklistinputs, this);
        recyclerView.setAdapter(checkList_adapter);


        RecycleDragCallback recycleDragCallback = new RecycleDragCallback(this.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                checklistinputs.remove(position);
                save();
                checkList_adapter.notifyDataSetChanged();

            }
        } ;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recycleDragCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);




        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                listItemBeingEdited = position;
                //we only update the list here. saving to controll is done in the onPause
               new Description_Fragment("Checklist Item Edit", checklistinputs.get(position), parent).show(getChildFragmentManager(), " Edit Item Dialog");

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                // TODO: 5/21/2021 delete checklist component

            }

            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Log.d("ggg", "onSwipe: ");
            }
        }));


    }



    @Override
    public void sendDialogResult(String result) {

        checklistinputs.set(listItemBeingEdited, result);

        if (listItemBeingEdited == checklistinputs.size()-1){
            checklistinputs.add("Add New Item");
        }
        listItemBeingEdited = -1;
        checkList_adapter.notifyDataSetChanged();

        save();

    }



    private void save()
    {
        ArrayList<String> saveme = new ArrayList<>();
        for (int i = 0; i < checklistinputs.size()-1; i++) {

            saveme.add(checklistinputs.get(i));

        }
        controller.updateSpecificCheckListEdit(saveme, toEdit);


    }



}
