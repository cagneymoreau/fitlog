package com.cagneymoreau.fitlog.views.setting_utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.logic.BackupFileGenerator;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.Emailer;
import com.cagneymoreau.fitlog.views.setting_utility.recycleview.Backup_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

/**
 * Opens from settings
 *
 * explain data policy
 * recycleview with
 *  create new backup <-- this will create backup up all that aren't backed up, unless some specific month is chosen
 *  show recent backups... <-- each of these can be mannualy selected to be backed up and show a backed vs non backeded up warning
 * // TODO: 6/8/2021 if old backup is pressed try resend
 *
 */

public class DataBackup extends Fragment {

    public static final String TAG = "Data_Backup";

    private View fragView;
    Controller controller;

    EditText editTextEmail;
    TextView textViewExplain;

    RecyclerView recyclerView;
    Backup_Adapter backup_adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Triple<Long, Long, Boolean>> backups;

    ArrayList<Pair<String, Boolean>> humanReadable;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.backup, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        fillEditText();
        buildRecycleView();

        return fragView;

    }

    private void fillEditText()
    {
        editTextEmail = fragView.findViewById(R.id.backup_EditText);

         String s = controller.getUserName();
         if (s != null)
         {
             editTextEmail.setText(s);
         }
        // TODO: 6/14/2021 save
    }

    @Override
    public void onPause() {
        super.onPause();
        controller.setEmail(editTextEmail.getText().toString());
    }

    private void buildRecycleView()
    {

        backups = controller.getBackups();
        buildUIList();

        recyclerView = fragView.findViewById(R.id.backup_recycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        backup_adapter = new Backup_Adapter(humanReadable, this);
        recyclerView.setAdapter(backup_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {


                if (position == 0){

                    sendToBackUp();

                }else{

                  humanReadable.set(position, new Pair<>(humanReadable.get(position).first, !humanReadable.get(position).second));
                   backup_adapter.notifyDataSetChanged();
                }

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


    /**
     * Turn an arraylist of pair long long which represent two points in time
     * into a readable description
     */
    private void  buildUIList()
    {
        humanReadable.clear();

        humanReadable.add(new Pair<>("Create backups Now!", Boolean.FALSE));

        for (int i = 0; i < backups.size(); i++) {

            LocalDate d = LocalDate.ofEpochDay(backups.get(i).getFirst());

            String yet = "  !";

            if (backups.get(i).getThird()) yet = "";

           String description =  d.getMonth() + " " + d.getYear() + yet;

           humanReadable.add(new Pair<>(description, Boolean.FALSE));

        }


    }



    private void sendToBackUp()
    {
        controller.setEmail(editTextEmail.getText().toString());

        if (controller.getEmail() == null || controller.getEmail().equals("")){
            Toast.makeText(getContext(), "You must input your email into app setting first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isMailClientPresent(getContext())){
            Toast.makeText(getContext(), "You must have an email app installed", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Triple<Long, Long, Boolean>>  toBackup = new ArrayList<>();
        //if we have a custom send selection
        for (int i = 0; i < humanReadable.size(); i++) {
            if (humanReadable.get(i).second){
                toBackup.add(backups.get(i));
                humanReadable.set(i, new Pair<>(humanReadable.get(i).first, Boolean.FALSE));
            }
        }
        //send anything not backed up
        if (toBackup.size() == 0)
        {
            for (int i = 0; i < backups.size(); i++) {
                if (!backups.get(i).getThird()){
                    toBackup.add(backups.get(i));
                }
            }

        }

        new Thread(() -> {
            BackupFileGenerator backupFileGenerator = new BackupFileGenerator(controller);
            ArrayList<File> backups = backupFileGenerator.generateBackupFiles(toBackup);
            Emailer emailer = new Emailer(backups);
            emailer.send();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    backup_adapter.notifyDataSetChanged();
                }
            });
        }).start();
    }



    public static boolean isMailClientPresent(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);

        if(list.size() == 0)
            return false;
        else
            return true;
    }



}
