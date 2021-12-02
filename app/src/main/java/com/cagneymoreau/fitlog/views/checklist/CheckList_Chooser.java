package com.cagneymoreau.fitlog.views.checklist;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.data.CheckLists;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.checklist.recycleview.CheckList_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CheckList_Chooser extends MyFragment {

    private View fragView;
    Controller controller;

    TextView chooserInstructions;

    RecyclerView recyclerView;
    CheckList_Adapter checkList_adapter;
    RecyclerView.LayoutManager layoutManager;


    Pair<List<String>, Boolean> data;

    boolean saved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        chooserInstructions = fragView.findViewById(R.id.chooser_textView);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        chooserInstructions.setText("Swipe to delete/tap to edit/hold to activate");

        buildRecycleView();

        saved = false;

        return fragView;

    }



    private void buildRecycleView()
    {

        //Here we expect list of all checklists ordered from most recently used or created to oldest
        //last on list should be a create new button//limit is 25 checklists
        data = controller.getCheckListsChoice();


        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        checkList_adapter = new CheckList_Adapter(data);
        recyclerView.setAdapter(checkList_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                //edit
                controller.setCheckListToModify(position);
                Navigation.findNavController(fragView).navigate(R.id.action_checkList_Chooser_Fragment_to_checkList_Edit_Fragment);

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                //make active
                data = controller.selectCheckList(position);
                checkList_adapter.insertList(data);
                checkList_adapter.notifyDataSetChanged();

            }


        }));
        enableSwipeDelete();

    }


    private void enableSwipeDelete()
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();

                if (position == data.first.size()-1){

                    Toast.makeText(fragView.getContext(), "Can't delete. This is the button to add a new item", Toast.LENGTH_SHORT).show();

                }else {

                    final CheckLists check = controller.getspecificCheckList(position);
                    controller.deleteChecklist(check);
                    data = controller.getCheckListsChoice();
                    checkList_adapter.insertList(data);
                    checkList_adapter.notifyDataSetChanged();

                    Snackbar snackbar = Snackbar
                            .make(fragView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            controller.reverseDelete(check);
                            data = controller.getCheckListsChoice();
                            checkList_adapter.insertList(data);
                            checkList_adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(position);

                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }




            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    private void save()
    {
        if (saved) return;
        controller.updateCheckListInDB();
            saved = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        save();

    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }
}
