package com.cagneymoreau.fitlog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cagneymoreau.fitlog.R;

/**
 * Shows the enlarge image of a certain trophy
 */

public class Trophy_Dialog extends DialogFragment {

    int item;

    public Trophy_Dialog(int position) {

        item = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v;


        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v = inflater.inflate(R.layout.trophy_dialog, null));
        ImageView im = v.findViewById(R.id.trophy_image);
        im.setImageDrawable(getDrawable(item));

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Trophy_Dialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    // TODO: 6/8/2021 design trophies and link them to the numbers
    private Drawable getDrawable(int pos)
    {
        switch (pos)
        {
            case 0:

               return getResources().getDrawable(R.drawable.doomer);

        }
        return null;
    }



}
