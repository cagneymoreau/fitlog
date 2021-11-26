package com.cagneymoreau.fitlog.logic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.cagneymoreau.fitlog.MainActivity;
import com.cagneymoreau.fitlog.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Our UI passes this class a list of files to be emailed and our app opens
 * the users existing email app outside of this app to send data to backup.
 *
 * Our app has no actual knowledge of correct backup. Only that a attempt to open the
 * email provider was made
 *
 *
 */




public class Emailer {

    private final static String TAG = "EMAILER";

    Controller controller;

    ArrayList<Integer> toReport;

    private String[] recipientOfEmail = new String[3];
    private String subjectOfEmail;
    private String bodyOfEmail = "";
    private String reportErrorExplain = "";
    Context mContext;


    File[] files;

    boolean DEBUG = false;

    private String filterChosen;

    ArrayList<File> fileList;


    public Emailer(ArrayList<File> fileList, Context context, Controller c)
    {
        mContext = context;

        controller = c;
        recipientOfEmail[0] = controller.getEmail();

        subjectOfEmail = fileList.get(0).getName() + " through " + fileList.get(fileList.size()-1).getName();

        this.fileList = fileList;



    }


    public void send()
    {

        for (int i = 0; i < fileList.size(); i++) {
            bodyOfEmail += fileList.get(i).getName() + "\n";
        }

        new Thread(() -> {

            sendEmail(fileList);

        }).start();

    }




    private void sendEmail(ArrayList<File> files)
    {

            Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
            i.setType("text/html");
            i.putExtra(Intent.EXTRA_EMAIL, recipientOfEmail);
            i.putExtra(Intent.EXTRA_SUBJECT, subjectOfEmail);
            i.putExtra(Intent.EXTRA_TEXT, bodyOfEmail);


            ArrayList<Uri> arr = new ArrayList<>();

        for (int j = 0; j < files.size(); j++) {
            Uri uri = FileProvider.getUriForFile(mContext, "com.cagneymoreau.fitlog.fileprovider", files.get(j));
            arr.add(uri);
        }
            i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arr);



            try {

                Intent sendEmailIntent = Intent.createChooser(i, "Send email backup");
                sendEmailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(sendEmailIntent);


            } catch (Exception e) {
                Log.e(TAG, "sendEmail: ",e );

            }



    }








}
