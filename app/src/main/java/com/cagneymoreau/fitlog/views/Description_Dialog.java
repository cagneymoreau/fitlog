package com.cagneymoreau.fitlog.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.Controller;

import java.util.ArrayList;

public class Description_Dialog extends DialogFragment {


    String prompt;
    String previous;
    MyFragment parent;

    Controller controller;

    boolean spinFlag;


    public Description_Dialog(String prompt, String previous, MyFragment parent, boolean showSpin)
    {
        this.prompt = prompt;
        this.previous = previous;
        this.parent = parent;
        spinFlag = showSpin;

        MainActivity m = (MainActivity) parent.getActivity();
        controller = m.getConroller();

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v;


        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(prompt)
                .setView(v = inflater.inflate(R.layout.description_dialog, null));
        final EditText editable = v.findViewById(R.id.dialog_editText);

        final Spinner prevList = v.findViewById(R.id.dialog_spinner);

        if (spinFlag) {

            final ArrayList<String> items = new ArrayList<>(controller.getMovements());
            ArrayAdapter aa = new ArrayAdapter(parent.getContext(), android.R.layout.simple_spinner_item, items);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prevList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    editable.setText(items.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            prevList.setAdapter(aa);
        }else{
            prevList.setVisibility(View.INVISIBLE);
        }

        if (!previous.equals("")){
            editable.setText(previous);
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                parent.sendDialogResult(editable.getText().toString());

            }
        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Description_Dialog.this.getDialog().cancel();
                    }
                });





        return builder.create();
    }






}
