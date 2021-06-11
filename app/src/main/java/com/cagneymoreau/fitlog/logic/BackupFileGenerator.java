package com.cagneymoreau.fitlog.logic;

import android.net.Uri;
import android.util.Log;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.data.WorkoutRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

/**
 * 1) This class takes in an arraylist of periods(single month) of time as two longs start and end of month
 * 2) Searches the database for all workouts within that time period
 * 3) puts each month into its own structured csv file
 *
 *                  Macro structure *
 * sun  workout 1       workout 5
 * mon  workout 2       workout 6
 * tue  workout 3         "   "
 * wed  workout 4
 *  "     "
 *
 *                    Micro Structure
 * splitname    day     date            splitname    day     date
 * check1 Y check2 N check1 Y note      check1 Y check2 N check1 Y note
 * movement set1 set2 set3              movement set1 set2 set3
 * movement set1 set2 set3              movement set1 set2 set3
 *      "       "                              "       "
 *
 *
 * splitname    day     date
 * check1 Y check2 N check1 Y note
 * movement set1 set2 set3
 * movement set1 set2 set3
 *  "           "
 *
 */
// TODO: 6/10/2021 what if two exercises are executed in the same day? how will it format?

public class BackupFileGenerator {

    Controller controller;

    public BackupFileGenerator(Controller cont)
    {
        controller = cont;

    }



    public ArrayList<File> generateBackupFiles(ArrayList<Triple<Long,Long, Boolean>> listToBackUp)
    {
        ArrayList<File> completedFiles = new ArrayList<>();

        for (int i = 0; i < listToBackUp.size(); i++) {

            ArrayList<WorkoutRecord> matches = controller.getMatchingWorkoutRecords(listToBackUp.get(i).getFirst(), listToBackUp.get(i).getSecond());

             completedFiles.add(layoutData(matches));
        }

        return completedFiles;

    }


    //takes in a months worth of data and return an organized csv file
    private File layoutData(ArrayList<WorkoutRecord> matches)
    {

        // TODO: 6/10/2021 matches must be sorted from first to last chronology
        
        //organize and measure
        ArrayList<FormattedEntry> completedEntries = new ArrayList<>();

        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < matches.size(); i++) {
            FormattedEntry f = new FormattedEntry(matches.get(i));
            if (f.getWidth() > maxWidth) maxWidth = f.getWidth();
            if (f.getHeight() > maxHeight) maxHeight = f.getHeight();
        }

        //fill out to make square blocks
        for (int i = 0; i < completedEntries.size(); i++) {
            completedEntries.get(i).convert(maxWidth, maxHeight);
        }

        File toReturn = getFilePath();
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;


        try{

            fileWriter = new FileWriter(toReturn);
            bufferedWriter = new BufferedWriter(fileWriter);








        }catch (Exception e){



        }




    }


    public File getFilePath(String name)
    {
        File pdfFile = new File("");
        String suffix = "";

        if (name.equals(TIMECARD)) suffix = mContext.getString(R.string.pdf);
        else if (name.equals(CSV)) suffix = mContext.getString(R.string.csv);


        Log.i(TAG, "getFilePath: " + name + NAMEFORFILE);
        String fileName = Uri.parse(name).getLastPathSegment();

        try {

            pdfFile = File.createTempFile(fileName, suffix, mContext.getCacheDir());
        } catch (IOException e) {
            // Log.e(TAG, "getFilePath: failed", e);
            //Crashlytics.log(TAG + " " +  e);
            Reporting.report_Event(mContext, Reporting.ERROR, "reportgenerator:618", e.toString());
        }

        return pdfFile;

    }


    /**
     * splitname    day     date
     * check1 Y check2 N check1 Y note
     * movement set1 set2 set3
     * movement set1 set2 set3
     *  "           "
     *
     *  Although the data in the workout record class is very similiar
     *  we need make sure each row and column is the same length by filling
     *  with blank data so that combining together later results in uniform spacing
     *  Also first two rows in example above need to be formatted into arraylists
     */
    class FormattedEntry{


        ArrayList<ArrayList<String>> descriptionGrid = new ArrayList<>();
        int width;
        int height;


        public FormattedEntry(WorkoutRecord workoutRecord)
        {

        }


        public void convert(int width, int height)
        {


        }


        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }


    }


}
