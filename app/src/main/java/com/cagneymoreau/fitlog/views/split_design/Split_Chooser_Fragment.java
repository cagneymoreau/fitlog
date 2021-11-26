package com.cagneymoreau.fitlog.views.split_design;

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
import com.cagneymoreau.fitlog.data.Splits;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.SwipeToDeleteCallback;
import com.cagneymoreau.fitlog.views.split_design.recycleview.Split_Adapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Split_Chooser_Fragment extends MyFragment {

    private View fragView;
    Controller controller;

    TextView chooserInstructions, chooserTitle;

    RecyclerView recyclerView;
    Split_Adapter split_adapter;
    RecyclerView.LayoutManager layoutManager;

    List<String> splitsList;

    ArrayList<Splits> toDelete;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        chooserInstructions = fragView.findViewById(R.id.chooser_textView);
        chooserInstructions.setText("Swipe to delete or tap to edit");

        chooserTitle = fragView.findViewById(R.id.chooser_title_textView);
        chooserTitle.setVisibility(View.INVISIBLE);
        
        buildRecycleView();

        return fragView;
    }
    
    
    
    private void buildRecycleView()
    {
        toDelete = new ArrayList<>();

        //Here we expect list of all splits ordered from most recently used or created to oldest
        //last on list should be a create new button//limit is 25 splits
        splitsList = controller.getSplitsList();

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        split_adapter = new Split_Adapter(splitsList, this);
        recyclerView.setAdapter(split_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                controller.setSplitToModify(position);
                Navigation.findNavController(fragView).navigate(R.id.action_split_Chooser_Fragment_to_split_Edit_Fragment);

            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

                List<String> sp = controller.selectSplit(position);
                split_adapter.insertList(sp);
                split_adapter.notifyDataSetChanged();

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

                if (position == splitsList.size()-1){

                    Toast.makeText(fragView.getContext(), "Can't delete. This is you button to add a new item", Toast.LENGTH_SHORT).show();

                }else {

                    final String s = splitsList.get(position);
                    final Splits split = controller.getSpecificSplit(position);
                    splitsList.remove(position);
                    toDelete.add(split);

                    Snackbar snackbar = Snackbar
                            .make(fragView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            splitsList.add(position, s);
                            toDelete.remove(split);
                            recyclerView.scrollToPosition(position);
                            split_adapter.notifyDataSetChanged();
                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                split_adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        controller.updateSplitsInDB();
    }
}
