package com.cagneymoreau.fitlog.views.split_design;

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
import com.cagneymoreau.fitlog.data.Splits;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.split_design.recycleview.ListItem;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.Description_Dialog;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.split_design.recycleview.Split_Edit_Adapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Split_Edit_Fragment extends MyFragment {


    private View fragView;
    Controller controller;


    TextView editInstructions_TextView, splitTitle_TextView;
    RecyclerView recyclerView;
    Split_Edit_Adapter split_adapter;
    RecyclerView.LayoutManager layoutManager;


    private int toEdit = -1;

    ArrayList<ListItem> structured = new ArrayList<>(); //this is a single arraylist designed for viewing
    ArrayList<ArrayList<String>> inputs; //this is the rwa data from the sql database
    int listItemBeingEdited = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.edit, container, false);

        editInstructions_TextView = fragView.findViewById(R.id.edit_instruction_textView);
        splitTitle_TextView = fragView.findViewById(R.id.edit_title_textView);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        toEdit = controller.getSplitToModify();

        buildRecycleView(toEdit);

        return fragView;

    }



    private void buildRecycleView(int toEdit)
    {
        MyFragment parent = this;
        //This checklist has all fields in place. UI should show empty for unused
        inputs = controller.getSpecificSplitEditable(toEdit);

        //create the list UI list
        convertSplitArraysIntoList();

        recyclerView = fragView.findViewById(R.id.edit_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        split_adapter = new Split_Edit_Adapter(structured, this);
        recyclerView.setAdapter(split_adapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                listItemBeingEdited = position;
                new Description_Dialog(structured.get(position).getEditPrompt(), structured.get(position).title, parent, structured.get(position).isField()).show(getChildFragmentManager(), " Edit Item Dialog");

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {



            }


        }));

        enableSwipeDelete();

    }


    /**
     * update our nested lists with result, build a new UI list and save
     * @param result
     */
    @Override
    public void sendDialogResult(String result) {

        if (structured.get(listItemBeingEdited).isField()){
            controller.addMovement(result);
        }

        int main = structured.get(listItemBeingEdited).mainList;
        int sub = structured.get(listItemBeingEdited).subList;

        if (main == inputs.size()){
            inputs.add(new ArrayList<>());
        }
        if (sub == inputs.get(main).size()){
            inputs.get(main).add("");
        }
        inputs.get(main).set(sub, result);

        listItemBeingEdited = -1;

        convertSplitArraysIntoList();

        save();

    }



    private void save()
    {
        controller.updateSpecificSplitEdit(inputs, toEdit);
    }



    /**
     * Because a recycleview is designed to work with a single list we combine all lists into
     * single while maintaining their nested relationships
     *  raw list from sql
     * @return a single list for recycle view with ui components added
     */
    private void convertSplitArraysIntoList()
    {

        // needed for empty sql on first time starts
        if (inputs == null)
        {
            inputs = new ArrayList<>();
            ArrayList<String> t = new ArrayList<>();
            t.add( " New Split");
            inputs.add(0, t);
        }



        structured.clear();

        ListItem l = new ListItem();
        l.title = inputs.get(0).get(0);
        l.mainList = 0;
        l.subList = 0;
        l.myType = ListItem.type.TITLE;
        structured.add(l);

        for (int i = 1; i < inputs.size(); i++) {

            for (int j = 0; j < inputs.get(i).size(); j++) {

                ListItem ll = new ListItem();
                ll.title = inputs.get(i).get(j);
                ll.mainList = i;
                ll.subList = j;
                if (j == 0){
                    ll.myType = ListItem.type.HEADER;
                }else{
                    ll.myType = ListItem.type.FIELD;
                }
                structured.add(ll);
            }

            //each day needs an add new movement button
            ListItem Extra = new ListItem();
            Extra.title = "Add New Movement";
            Extra.mainList = i;
            Extra.subList = inputs.get(i).size();
            Extra.myType = ListItem.type.FIELD;
            Extra.uiPlaceHolder = true;
            structured.add(Extra);


        }

        //each split needs a new day button if max isnt reached
        if (inputs.size() < 10) {
            ListItem Two = new ListItem();
            Two.title = "Add New Day";
            Two.mainList = inputs.size();
            Two.subList = 0;
            Two.myType = ListItem.type.HEADER;
            Two.uiPlaceHolder = true;
            structured.add(Two);
        }

        if (split_adapter != null){
            split_adapter.notifyDataSetChanged();
        }


    }






    private void enableSwipeDelete()
    {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();

                if (position == 0){

                    Toast.makeText(fragView.getContext(), "Can't delete. This is the title. Click to Edit", Toast.LENGTH_SHORT).show();
                    split_adapter.notifyDataSetChanged();
                }else if (structured.get(position).uiPlaceHolder){

                    Toast.makeText(fragView.getContext(), "Can't delete. This is the button to add a new item", Toast.LENGTH_SHORT).show();
                    split_adapter.notifyDataSetChanged();
                }
                else {

                    int main = structured.get(position).mainList;
                    int sub = structured.get(position).subList;

                    ArrayList<String> val;

                    if (sub == 0){
                        val = inputs.get(main);
                        inputs.remove(main);
                    }else{
                        val = new ArrayList<>();
                        val.add(inputs.get(main).get(sub));
                        inputs.get(main).remove(sub);
                    }

                    convertSplitArraysIntoList();


                    Snackbar snackbar = Snackbar
                            .make(fragView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (sub == 0){
                                inputs.add(main, val);
                            }else{
                                inputs.get(main).add(sub, val.get(0));
                            }

                            convertSplitArraysIntoList();
                            recyclerView.scrollToPosition(position);
                            split_adapter.notifyDataSetChanged();
                        }
                    });


                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                    save();

                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }






}
