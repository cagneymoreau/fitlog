package com.cagneymoreau.fitlog.views.split_design;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;
import com.cagneymoreau.fitlog.views.split_design.recycleview.Split_Adapter;

import java.util.List;

public class Split_Chooser_Fragment extends Fragment {

    private View fragView;
    Controller controller;

    TextView chooserInstructions;

    RecyclerView recyclerView;
    Split_Adapter split_adapter;
    RecyclerView.LayoutManager layoutManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.chooser, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        chooserInstructions = fragView.findViewById(R.id.chooser_textView);
        chooserInstructions.setText("Swipe to delete or tap to edit");
        
        buildRecycleView();

        return fragView;
    }
    
    
    
    private void buildRecycleView()
    {
        //Here we expect list of all splits ordered from most recently used or created to oldest
        //last on list should be a create new button//limit is 25 splits
        List<String> checklists = controller.getSplitsList();

        recyclerView = fragView.findViewById(R.id.chooser_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        split_adapter = new Split_Adapter(checklists, this);
        recyclerView.setAdapter(split_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {

                controller.setSplitToModify(position);
                Navigation.findNavController(fragView).navigate(R.id.action_split_Chooser_Fragment_to_split_Edit_Fragment);

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
    
    
    
    
    
}
