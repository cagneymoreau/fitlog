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

public class Emailer {

    private final static String TAG = "EMAILER";

    Workhistory workhistory;

    ArrayList<Integer> toReport;

    private String[] recipientOfEmail = new String[3];
    private String subjectOfEmail;
    private String bodyOfEmail = "";
    private String reportErrorExplain = "";
    Context mContext;

    ReportGenerator reportGenerator;
    File[] files;

    boolean DEBUG = false;

    private String filterChosen;

    public Emailer(Context context, ArrayList<Integer> list, boolean debug, String filter)
    {
        DEBUG = debug;
        filterChosen = filter;
        mContext = context;
        subjectOfEmail = mContext.getString(R.string.tcr);
        toReport = list;
        workhistory = Workhistory.getInstance(context);
        reportGenerator = new ReportGenerator(mContext);


    }



    public void send()
    {

        GenerateReport generateReport = new GenerateReport();

        generateReport.execute();

        //this gets set to allow app rating
        workhistory.setBoolean(Workhistory.EMAILER_USED, true);


    }



    private void sendEmail()
    {
        Log.i(TAG, "sendEmail: ");
        workhistory.deleteUnretrievableData();

        boolean infoToReport = createReport();

        int[] days = new int[toReport.size()];

        for (int i = 0; i < days.length; i++) {
            days[i] = toReport.get(i);
        }

        files = reportGenerator.makeReport(days);

        if (infoToReport) {
            Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
            i.setType("text/html");
            i.putExtra(Intent.EXTRA_EMAIL, recipientOfEmail);
            i.putExtra(Intent.EXTRA_SUBJECT, subjectOfEmail);
            i.putExtra(Intent.EXTRA_TEXT, bodyOfEmail);


            ArrayList<Uri> arr = new ArrayList<Uri>();

            Uri uri = FileProvider.getUriForFile(mContext, "ccom.cagneymoreau.simpletimekeeper.fileprovider", files[0]);
            arr.add(uri);

            Uri uriOne = FileProvider.getUriForFile(mContext, "ccom.cagneymoreau.simpletimekeeper.fileprovider", files[1]);
            arr.add(uriOne);

            i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arr);



            try {
                //Log.d(TAG, "sendEmail: attempting");

                Intent sendEmailIntent = Intent.createChooser(i,mContext.getResources().getString(R.string.sendemail));
                sendEmailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(sendEmailIntent);
                //Answers.getInstance().logCustom(new CustomEvent(" Email report being sent ")); // TODO: 4/29/2019 my reporting interface needs a makeover
                //startActivity(Intent.createChooser(i, "Send email..."));


            } catch (android.content.ActivityNotFoundException ex) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(mContext, R.string.no_email_client, Toast.LENGTH_LONG).show();

                    }
                });


            }

        }else if (reportErrorExplain.equals(mContext.getString(R.string.must_fill_name)) || reportErrorExplain.equals(mContext.getString(R.string.must_fill_recipient)))  {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, reportErrorExplain, Toast.LENGTH_SHORT).show();
                }
            });


        }
        else {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, reportErrorExplain, Toast.LENGTH_SHORT).show();
                }
            });

        }




    }



    public boolean createReport()
    {
        Log.i(TAG, "createReport: ");

        recipientOfEmail[0] = "";
        recipientOfEmail[1] = "";
        recipientOfEmail[2] = "";



        boolean isThereInfo = false;


        //create the message recipients
        boolean singleGoodEmail = false;

        String email_1=  workhistory.getData(MainActivity.MY_EMAIL_ONE);
        String email_2=  workhistory.getData(MainActivity.MY_EMAIL_TWO);
        String email_3=  workhistory.getData(MainActivity.MY_EMAIL_THREE);

        if (!email_1.equals("")){recipientOfEmail[0] = email_1; singleGoodEmail = true;}
        if (!email_2.equals("")){recipientOfEmail[1] = email_2;singleGoodEmail = true;}
        if (!email_3.equals("")){recipientOfEmail[2] = email_3;singleGoodEmail = true;}

        if (!singleGoodEmail){reportErrorExplain = mContext.getString(R.string.must_fill_recipient); return isThereInfo;}




        // create the message body

        String myName = workhistory.getData(MainActivity.MY_NAME);

        if (myName.equals("")){ reportErrorExplain = mContext.getString(R.string.must_fill_name); return isThereInfo;}

        //check to make sure we are not sending a half finished day
        boolean today = false;
        for (int i = 0; i < toReport.size(); i++) {

            if (toReport.get(i) == 0){
                today = true;
            }

        }

        if (workhistory.clockedInNow() && today){reportErrorExplain = mContext.getString(R.string.must_clock_out); return isThereInfo;}


        bodyOfEmail = "";

        //each days data into emailbody
        if (!toReport.isEmpty()) {

            isThereInfo = true;

            bodyOfEmail += myName + " " + mContext.getString(R.string.whos_reporting) + "\n \n";

            bodyOfEmail += createEmailList(toReport);

            bodyOfEmail += mContext.getString(R.string.report_created_by);



        }else{ reportErrorExplain = mContext.getString(R.string.must_select_days);}


        return isThereInfo;

    }



    public String createEmailList(List<Integer> daysBeingSent)
    {
        //Log.i(TAG, "createEmailList: ");

        StringBuilder sb = new StringBuilder();
        StringBuilder hold = new StringBuilder();

        int tWkMn = 0;
        int tWkHr = 0;
        Object[] objects; // string hours, int work minutes

        boolean hoursTotaled = true;

        for (Integer day :
                daysBeingSent) {

            objects = workhistory.getDayLongSummary(day, filterChosen);

            try{

                hold.append( objects[0]);

                tWkMn += (Integer) objects[1];

            }catch (Exception e)
            {
                Reporting.report_Event(mContext, Reporting.MISMATCH, "emailer 257", "null");
                hoursTotaled = false;
            }


        }

        if(hoursTotaled) {


            if (tWkMn >= 60) {

                tWkHr += tWkMn / 60;

                tWkMn = tWkMn % 60;

            }

            String workminutes = "";
            if (tWkMn< 10){
                workminutes += 0 + String.valueOf(tWkMn);
            }else  {workminutes = String.valueOf(tWkMn);}


            sb.append(String.valueOf(daysBeingSent.size())).append(" days of work added to ").append(String.valueOf(tWkHr)).append(":").append(workminutes)
                    .append(" ").append(mContext.getString(R.string.hoursworked)).append("\n\n");
        }



        sb.append(hold.toString());

        sb.append("\n\n\n");

        String almost = sb.toString();

        if (DEBUG) {

            sb = new StringBuilder();

            String version = " unkown";

            try{

                PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                version = pi.versionName;
            }catch (Exception e)
            {
                Reporting.report_Event(mContext, Reporting.ERROR, "emailer305 " ,"");
            }
            sb.append(version).append("\n");

            for (Integer day :
                    daysBeingSent) {

                String rkey = workhistory.createCalendarDate(day) + workhistory.utcPrefCode;  //utc
                sb.append(rkey).append("\n")
                        .append(workhistory.getData(rkey)).append("\n");

                rkey = workhistory.createCalendarDate(day);  //old
                sb.append("old:\n")
                        .append(workhistory.getData(rkey)).append("\n");

                rkey = workhistory.createCalendarDate(day) + Workhistory.longDescPrefCode;
                sb.append("long desc:\n")
                        .append(workhistory.getData(rkey)).append("\n");

            }

            almost += sb.toString();
        }

        return almost;

    }



    class GenerateReport extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            sendEmail();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }





    }





}
