package com.cagneymoreau.fitlog.views.active_workout.recycleview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.views.active_workout.Active_Workout;

public class Movement_ViewHolder extends RecyclerView.ViewHolder {

        EditText editText;
        View view;
        Button button;
        boolean buttonHidden = true;
        int position;
        LinearLayout layout;

        Active_Workout active_workout;

    public Movement_ViewHolder(@NonNull View itemView, Active_Workout active_workout, int position) {
        super(itemView);

        layout = itemView.findViewById(R.id.movecard_layout);
        editText = itemView.findViewById(R.id.movement_EditText);
        //button = itemView.findViewById(R.id.movement_Button);
        view = itemView;
        this.position = position;
        this.active_workout = active_workout;

        active_workout.addEditor(editText, position);

    }


    public TextView getEditText()
    {
        return editText;
    }

    //if last item in que we should show a button when user fills in some data
    public void showButton()
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && buttonHidden){

                    buttonHidden = false;

                    button = new Button(view.getContext());
                    button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    button.setText("Next");

                    layout.addView(button);


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            active_workout.newEntry(position);
                            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    });


                }

            }
        });




    }


}
