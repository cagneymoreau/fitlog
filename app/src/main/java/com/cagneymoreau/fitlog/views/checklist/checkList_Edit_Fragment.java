package com.cagneymoreau.fitlog.views.checklist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.checklist.recycleview.CheckList_Edit_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.Description_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.google.android.material.snackbar.Snackbar;

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

    boolean edited;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.edit, container, false);

        editInstructions_TextView = fragView.findViewById(R.id.edit_instruction_textView);
        checklistTitle_TextView = fragView.findViewById(R.id.edit_title_textView);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        toEdit = controller.getCheckListToModify();

        buildRecycleView(toEdit);

        edited = false;

        return fragView;

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
            title = " Click me to edit title";
            checklistinputs.add(0, title);
        }
        MyFragment parent = this;





        checklistinputs.add("Add New Item");


        recyclerView = fragView.findViewById(R.id.edit_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        checkList_adapter = new CheckList_Edit_Adapter(checklistinputs, this);
        recyclerView.setAdapter(checkList_adapter);



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                edited = true;

                listItemBeingEdited = position;
                //we only update the list here. saving to controll is done in the onPause
               new Description_Dialog("Checklist Item Edit", checklistinputs.get(position), parent, false).show(getChildFragmentManager(), " Edit Item Dialog");

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {



            }


        }));

        enableSwipeDelete();

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
        if (!edited){
            return;
        }

        ArrayList<String> saveme = new ArrayList<>();
        for (int i = 0; i < checklistinputs.size()-1; i++) {

            saveme.add(checklistinputs.get(i));

        }
        controller.updateSpecificCheckListEdit(saveme, toEdit);


    }



    private void enableSwipeDelete()
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                edited = true;

                final int position = viewHolder.getAdapterPosition();

                if (position == 0){

                    Toast.makeText(fragView.getContext(), "Can't delete title. Click to rename", Toast.LENGTH_SHORT).show();

                }else if (position == checklistinputs.size()-1){

                    Toast.makeText(fragView.getContext(), "Can't delete. This is you button to add a new item", Toast.LENGTH_SHORT).show();

                }else {

                    final String s = checklistinputs.get(position);
                    checklistinputs.remove(position);

                    Snackbar snackbar = Snackbar
                            .make(fragView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            checklistinputs.add(position, s);
                            recyclerView.scrollToPosition(position);
                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }

                checkList_adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        save();
    }
}
