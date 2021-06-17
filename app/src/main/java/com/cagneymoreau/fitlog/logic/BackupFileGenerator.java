package com.cagneymoreau.fitlog.logic;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.cagneymoreau.fitlog.R;
import com.cagneymoreau.fitlog.data.WorkoutRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
// TODO: 6/14/2021 strip special characters

public class BackupFileGenerator {

    public static final String TAG = "BackupFileGenerator";

    Controller controller;
    Context mContext;

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
        LocalDate d = LocalDate.ofEpochDay(matches.get(0).datetime);
        String description =  d.getMonth() + " " + d.getYear();

        // TODO: 6/14/2021 sql does this alreadY?
        //sort first to last
        Collections.sort(matches, new Comparator<WorkoutRecord>() {
            @Override
            public int compare(WorkoutRecord o1, WorkoutRecord o2) {
                if (o1.datetime > o2.datetime) return 1;
                else  if (o1.datetime == o2.datetime){
                    Log.e(TAG, "compare: ", null);
                    return 0;
                }
                else return -1;
            }
        });
        
        //measure
        ArrayList<FormattedEntry> completedEntries = new ArrayList<>();

        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < matches.size(); i++) {
            FormattedEntry f = new FormattedEntry(matches.get(i));
            if (f.getWidth() > maxWidth) maxWidth = f.getWidth();
            if (f.getHeight() > maxHeight) maxHeight = f.getHeight();
            completedEntries.add(f);
        }

        //fill out to make square blocks
        for (int i = 0; i < completedEntries.size(); i++) {
            completedEntries.get(i).convert(maxWidth, maxHeight);
        }


        //At this point each entry is uniform.
        //write
        File toReturn = getFilePath(description);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;


        try{

            fileWriter = new FileWriter(toReturn);
            bufferedWriter = new BufferedWriter(fileWriter);

            int startBlock = completedEntries.get(0).getPosition();

            for (int i = 1; i < 8; i++) {

                for (int k = 0; k < maxHeight; k++) {

                    // fill in column 1 with blank data
                    if (i < startBlock){
                        for (int j = 0; j < maxWidth; j++) {
                            bufferedWriter.write(" ,");
                        }
                    }

                for (int j = 0; j < completedEntries.size(); j++) {

                    if (completedEntries.get(j).getPosition() == i ){

                        for (String s :
                               completedEntries.get(j).descriptionGrid.get(k)) {
                            bufferedWriter.write(s);
                            bufferedWriter.write(",");
                        }
                    }

                }
                    bufferedWriter.write("\n");
            //each row
            }

                //seven days
            }


        }catch (Exception e){

            Log.e(TAG, "layoutData: ", e);

        }finally {
            try {

                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null){
                    fileWriter.close();
                }

            }catch (IOException ioe){
                Log.e(TAG, "sheetCSV: ",ioe );

            }


        }

        return toReturn;

    }







    public File getFilePath(String name)
    {
        File file = new File("");
        String suffix = ".csv";


        //Log.i(TAG, "getFilePath: " + name + NAMEFORFILE);
        String fileName = Uri.parse(name).getLastPathSegment();

        try {

            file = File.createTempFile(fileName, suffix, mContext.getCacheDir());
        } catch (IOException e) {
             Log.e(TAG, "getFilePath: failed", e);
            //Crashlytics.log(TAG + " " +  e);
            //Reporting.report_Event(mContext, Reporting.ERROR, "reportgenerator:618", e.toString());
        }

        return file;

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

        int position; //0 = sunday through 6 - saturday

        public FormattedEntry(WorkoutRecord workoutRecord)
        {
          firstRowPlusPOsition(workoutRecord);
          secondRow(workoutRecord);

          descriptionGrid.addAll(workoutRecord.workout);


        }

        private void firstRowPlusPOsition(WorkoutRecord workoutRecord)
        {
            ArrayList<String> firstRow = new ArrayList<>();
            firstRow.add(workoutRecord.splitName);
            firstRow.add(workoutRecord.dayName);

            LocalDate d = LocalDate.ofEpochDay(workoutRecord.datetime);
            String date = d.toString();
            firstRow.add(date);

            position = d.getDayOfWeek().getValue()-1;

            descriptionGrid.add(firstRow);
        }

        private void secondRow(WorkoutRecord workoutRecord)
        {
            ArrayList<String> secR = new ArrayList<>();


            for (Pair<String, Boolean> p :
                    workoutRecord.checkList) {
                String s = "";
                if (p.second){
                    s = p.first + "&#x1F5F8;";
                }else{
                    s = p.first + "x";
                }
                secR.add(s);
            }

            descriptionGrid.add(secR);
        }





        public void convert(int width, int height)
        {
            int missingRows = height - descriptionGrid.size();
            for (int i = 0; i < missingRows; i++) {
                descriptionGrid.add(new ArrayList<>());
            }

            for (int i = 0; i < descriptionGrid.size(); i++) {
                int missingCol = width - descriptionGrid.get(i).size();

                for (int j = 0; j < missingCol; j++) {
                    descriptionGrid.get(i).add(" ");
                }
            }

        }


        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }

        public int getPosition()
        {
            return position;
        }

    }


}
