package com.cagneymoreau.fitlog.views.setting_utility;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.FileUtil;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.data.ErrorLog;
import com.cagneymoreau.fitlog.logic.BackupFileGenerator;
import com.cagneymoreau.fitlog.logic.ComplexErrorLogger;
import com.cagneymoreau.fitlog.logic.Controller;
import com.cagneymoreau.fitlog.logic.Emailer;
import com.cagneymoreau.fitlog.logic.Subscription;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.setting_utility.recycleview.Backup_Adapter;
import com.cagneymoreau.fitlog.logic.RecyclerTouchListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

import static android.app.Activity.RESULT_OK;

/**
 * Opens from settings
 *
 * explain data policy
 * recycleview with
 *  create new backup <-- this will create backup up all that aren't backed up, unless some specific month is chosen
 *  show recent backups... <-- each of these can be mannualy selected to be backed up and show a backed vs non backeded up warning
 * // TODO: 11/26/2021 load old damage make sure it works 
 *
 */

public class DataBackup extends MyFragment {

    public static final String TAG = "Data_Backup";

    private View fragView;
    Controller controller;

    EditText editTextEmail;
    TextView textViewExplain;
    Button backup_btn, policyButton, privacyButton;

    RecyclerView recyclerView;
    Backup_Adapter backup_adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Triple<Long, Long, Boolean>> backups;

    ArrayList<Pair<String, Boolean>> humanReadable;

    ScrollView scrollView;

    int freeCount;


    String policy ="POLICY - This is not a permanent data backup app." +
            " Old data is automatically deleted after 12 months to save space." +
            " Make sure to send regular backups to your email for long term storage." +
            " I recommend monthly data backups to prevent large losses." +
            " Perform a workout and make a backup to ensure that the backups meet your requirements." +
            " This app should auto-backup to your google drive if you get a new phone but please double check when switching device. " +
            "If you get an error making a data backup contact me and forward the generated email to tau6283@protonmail.com";

    String privacy = "PRIVACY - Your data is not private. If this app crashes the data related to that crash is sent to the app designer to fix the bug." +
            "There is no method to opt out. By using this app you consent to that usage. I have no intention of using your data other than making sure the app works." +
            " Further, governments and non government organizations purposely embed tracking software and hardware everywhere which I cannot control." +
            "Never publish private information in this app (or any app for that matter).";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.backup, container, false);

        freeCount = 0;

        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getConroller();

        scrollView = fragView.findViewById(R.id.scrollText);

        backup_btn = fragView.findViewById(R.id.create_backup_button);
        policyButton = fragView.findViewById(R.id.policy_Button);
        privacyButton = fragView.findViewById(R.id.privacy_Button);

        buildButtonText();

        textViewExplain = fragView.findViewById(R.id.backup_TextView);
        textViewExplain.setText(policy);

        fillEditText();
        buildRecycleView();

        return fragView;


    }

    private void buildButtonText()
    {
        backup_btn.setText("make backup");
        backup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToBackUp();
            }
        });

        backup_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                freemiumCounter();
                return true;
            }
        });

        policyButton.setText("Policy");
        policyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewExplain.setText(policy);
                scrollView.scrollTo(0,0);
            }
        });

        privacyButton.setText("Privacy");
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewExplain.setText(privacy);
                scrollView.scrollTo(0,0);
            }
        });


        if (controller.getEmail().equals("cagneyamoreau@gmail.com"))
        {

        }


        privacyButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (controller.getEmail().equals("cagneyamoreau@gmail.com"))
                {
                    examineFile();
                }

                return false;
            }
        });



    }

    private void fillEditText()
    {
        editTextEmail = fragView.findViewById(R.id.backup_EditText);
        editTextEmail.setHint("email to send backups");

         String s = controller.getEmail();
         if (s != null)
         {
             editTextEmail.setText(s);
         }


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

                  humanReadable.set(position, new Pair<>(humanReadable.get(position).first, !humanReadable.get(position).second));
                   backup_adapter.notifyDataSetChanged();


            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {

            }



        }));


    }


    /**
     * Turn an arraylist of pair long long which represent two points in time
     * into a readable description
     */
    private void  buildUIList()
    {
        humanReadable = new ArrayList<>();

        if (backups == null || backups.size() == 0){

            humanReadable.add(new Pair<>("no history yet", Boolean.FALSE));
            return;
        }


        for (int i = 0; i < backups.size(); i++) {

            LocalDate d =  Instant.ofEpochMilli(backups.get(i).getFirst()).atZone(ZoneId.systemDefault()).toLocalDate();


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
        if (!isMailClientPresent(getContext())){
            Toast.makeText(getContext(), "You must have an email app installed", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Triple<Long, Long, Boolean>>  toBackup = new ArrayList<>();
        //if we have a custom send selection
        for (int i = 0; i < humanReadable.size(); i++) {
            if (humanReadable.get(i).second){
                toBackup.add(backups.get(i));
                humanReadable.set(i, new Pair<>(humanReadable.get(i).first, Boolean.FALSE));
                //humanReadable.set(position, new Pair<>(humanReadable.get(position).first, !humanReadable.get(position).second));
            }
        }

        backup_adapter.notifyDataSetChanged();



        new Thread(() -> {
            BackupFileGenerator backupFileGenerator = new BackupFileGenerator(controller, getContext());
            ArrayList<File> backups = backupFileGenerator.generateBackupFiles(toBackup);
            if (backups.size() == 0){

                this.getActivity().runOnUiThread (new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "No workouts in this period", Toast.LENGTH_SHORT).show();

                    }
                });
                return;
            }
            Emailer emailer = new Emailer(backups, getContext(), controller);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        controller.setEmail(editTextEmail.getText().toString());
    }


    private void freemiumCounter()
    {
        if (freeCount > 6){
            Subscription s = controller.getSubscription();
            s.activateFreemium();
            Toast.makeText(getContext(), "Freemium Engaged", Toast.LENGTH_SHORT).show();
            freeCount = 0;
        }
        else {
            freeCount++;
        }

    }




    //region---------------Debugging a non local workout record. Not used by end users

    private static final int REQUEST_CODE = 103;

    public void examineFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Update with mime types
        intent.setType("text/plain");

        // Update with additional mime types here using a String[].
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // REQUEST_CODE = <some-integer>
        startActivityForResult(intent, REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user doesn't pick a file just return
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return;
        }

        try {
            ErrorLog l = ErrorLog.revertFromUri(data.getData(), getActivity());
            l.description();

            BackupFileGenerator bfg = new BackupFileGenerator(controller, getContext());
            bfg.debugRemoteData(l);

        }catch (Exception e)
        {
            Log.e(TAG, "tryAgain: ", e);
        }

    }




    //endregion


}
