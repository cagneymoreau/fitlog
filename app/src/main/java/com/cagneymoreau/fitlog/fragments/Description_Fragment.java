package com.cagneymoreau.fitlog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.fitlog.R;

public class Description_Fragment extends DialogFragment {


    String prompt;
    String previous;
    MyFragment parent;

    public Description_Fragment(String prompt, String previous, MyFragment parent)
    {
        this.prompt = prompt;
        this.previous = previous;
        this.parent = parent;

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v;


        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(prompt)
                .setView(v = inflater.inflate(R.layout.description_dialog, null));
        final EditText editable = v.findViewById(R.id.dialog_editText);

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
                        Description_Fragment.this.getDialog().cancel();
                    }
                });





        return builder.create();
    }






}
