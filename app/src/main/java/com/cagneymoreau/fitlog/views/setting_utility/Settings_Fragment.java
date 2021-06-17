package com.cagneymoreau.fitlog.views.setting_utility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

public class Settings_Fragment extends Fragment {


    private View fragView;
    Controller controller;

    EditText nameEditText, emailEditText;
    CheckBox statisticsCheckedText, bodymeasureCheckedText, checklistCheckedText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.settings, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        buildLayout();

        return fragView;
    }


    private void buildLayout()
    {
        nameEditText = fragView.findViewById(R.id.name_editText);
        String name = controller.getUserName();
        if (name != null){
            nameEditText.setText(name);
        }

        emailEditText = fragView.findViewById(R.id.email_editText);
        String email = controller.getEmail();
        if(email != null){
            emailEditText.setText(email);
        }

        statisticsCheckedText = fragView.findViewById(R.id.statistics_checkedText);
         statisticsCheckedText.setChecked(controller.bodyMeasureBool());
        statisticsCheckedText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.setStatistics(!controller.getStatisticsBool());
                //statisticsCheckedText.setChecked(controller.getStatisticsBool());
            }
        });


        bodymeasureCheckedText = fragView.findViewById(R.id.bodymeasure_checkedText);
       bodymeasureCheckedText.setChecked(controller.bodyMeasureBool());
        bodymeasureCheckedText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.setBodyMeasure(!controller.bodyMeasureBool());
                //bodymeasureCheckedText.setChecked(controller.bodyMeasureBool())
            }
        });

        checklistCheckedText = fragView.findViewById(R.id.checkList_checkedText);
        checklistCheckedText.setChecked(controller.getcheckListBool());
        checklistCheckedText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.setChecklist(!controller.getcheckListBool());
                //checklistCheckedText.setChecked(controller.getcheckListBool());
            }
        });



    }





    @Override
    public void onPause() {
        super.onPause();

        saveEverything();

    }

    //save all current info to database
    private void saveEverything()
    {
        controller.setEmail(emailEditText.getText().toString());
        controller.setUsername(nameEditText.getText().toString());

        controller.saveUserInfoDataBase();

    }


}
