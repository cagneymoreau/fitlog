package com.cagneymoreau.fitlog.logic;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.cagneymoreau.fitlog.data.ErrorLog;
import com.cagneymoreau.fitlog.data.WorkoutRecord;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;

import kotlin.Triple;

/**
 * 1) This class takes in an arraylist of periods(single month) of time as two longs start and end of month
 * 2) Searches the database for all workouts within that time period
 * 3) puts each month into its own structured csv file
 *
 *
 */
// TODO: 11/1/2021 title has long string of stuff because its a temp file

    
public class BackupFileGenerator {

    public static final String TAG = "BackupFileGenerator";

    Controller controller;

    Context mContext;

    public BackupFileGenerator(Controller cont, Context context)
    {
        controller = cont;
        mContext = context;
    }



    public ArrayList<File> generateBackupFiles(ArrayList<Triple<Long,Long, Boolean>> listToBackUp)
    {
        ArrayList<File> completedFiles = new ArrayList<>();
        ArrayList<File> errorCatcher = new ArrayList<>();

        for (int i = 0; i < listToBackUp.size(); i++) {

            ArrayList<WorkoutRecord> matches = controller.getMatchingWorkoutRecords(listToBackUp.get(i).getFirst(), listToBackUp.get(i).getSecond());

            if (matches == null || matches.size() == 0) continue;

            try {
                MonthTensor monthTensor = new MonthTensor(matches, listToBackUp.get(i));
                completedFiles.addAll(monthTensor.generateCSV());
            }catch (Exception e)
            {
                e.printStackTrace();

                FirebaseCrashlytics.getInstance().log(TAG + " monthtensor");
                FirebaseCrashlytics.getInstance().recordException(e);

                try {
                    ErrorLog log = new ErrorLog(matches, listToBackUp.get(i));
                    errorCatcher.add(ErrorLog.sendToFile(log, mContext));

                }catch (Exception x)
                {
                    x.printStackTrace();

                    FirebaseCrashlytics.getInstance().log(TAG + "errorlogger");
                    FirebaseCrashlytics.getInstance().recordException(x);
                }
            }


        }



        if (errorCatcher.size() > 0){
            completedFiles.addAll(errorCatcher);
        }


        return completedFiles;


    }

    public void debugRemoteData(ErrorLog log)
    {


        try {
            MonthTensor monthTensor = new MonthTensor(log.getRecords(), log.getPeriod());
            ArrayList<File> out = monthTensor.generateCSV();
        }catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log(TAG + " monthtensor");
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        int i = 0;
    }

    public static File getFilePath(String name, String suffix, Context context)
    {
        File file = new File("");



        //Log.i(TAG, "getFilePath: " + name + NAMEFORFILE);
        String fileName = Uri.parse(name + " -").getLastPathSegment();

        try {

            file = File.createTempFile(fileName, suffix, context.getCacheDir());
        } catch (IOException e) {
             Log.e(TAG, "getFilePath: failed", e);
            //Crashlytics.log(TAG + " " +  e);
            //Reporting.report_Event(mContext, Reporting.ERROR, "reportgenerator:618", e.toString());
        }

        return file;

    }








    /**
     * Pass a period and list of records in that period.
     * produce csv file
     *
     *  additional daily workouts on second page
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

    class MonthTensor

    {

        Triple<Long,Long, Boolean> period;

        //ArrayList<ArrayList<ArrayList<FormattedEntry>>> thismonth = new ArrayList<>();
        ArrayList<SingleDay> thismonth;

        String fileTitle;

        FormattedEntry blankEntry;
        SingleDay blankDay;

        int depth = 1; //largest number of entries on a single day // TODO: 11/1/2021

        public MonthTensor(ArrayList<WorkoutRecord> matches, Triple<Long,Long, Boolean> period){

            this.period = period;

            generateFileTitle(matches.get(0));


            ArrayList<FormattedEntry> entries =  buildCells(matches);

            thismonth =  pushToTwoDim(entries);


        }


        private void generateFileTitle(WorkoutRecord w)
        {
            LocalDate d = Instant.ofEpochMilli(w.datetime).atZone(ZoneId.systemDefault()).toLocalDate();

            fileTitle =  d.getMonth() + " " + d.getYear();
        }


        private ArrayList<FormattedEntry> buildCells(ArrayList<WorkoutRecord> matches)
        {

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

            blankEntry = new FormattedEntry(null);
            blankEntry.convert(maxWidth, maxHeight);

            return completedEntries;
        }

        //return total days in span with no datat or data
        private ArrayList<SingleDay> pushToTwoDim(ArrayList<FormattedEntry> entries)
        {
            ArrayList<SingleDay> builder = new ArrayList<>();

            int numdays = Period.between( Instant.ofEpochMilli(period.getFirst()).atZone(ZoneId.systemDefault()).toLocalDate() , Instant.ofEpochMilli(period.getSecond()).atZone(ZoneId.systemDefault()).toLocalDate() ).getDays();

            numdays++; //period.between exclusive so add it back in

            long day = period.getFirst() + (5*60*60*1000);//move this away from the midnight margin to avoid tiny errors equaling new days
            long increment = 1000*60*60*24;

            for (int i = 0; i < numdays; i++) {
                builder.add(new SingleDay(day));
                day += increment;
            }

            int pos = 0;
            for (int i = 0; i < entries.size(); i++) {

                boolean searching = true;

                while (searching) {
                    if (entries.get(i).getDate().isEqual(builder.get(pos).myDate)) {
                        builder.get(pos).add(entries.get(i));
                        searching = false;
                    } else if (builder.get(pos).thisday.size() == 0){
                        builder.get(pos).add(blankEntry);
                        pos++;

                        if (pos > builder.size()){
                            //  error
                            break;
                        }

                    }else{
                        pos++;
                    }
                    if (builder.get(pos).thisday.size() > depth) depth = builder.get(pos).thisday.size();
                }

            }


            //finish remaining days of month
            if (builder.get(pos).thisday.size() != 0) pos++;

            for (int i = pos; i < builder.size(); i++) {
                builder.get(i).add(blankEntry);
            }

            return builder;

        }


        //return a sheet. if days have multiple workouts then return multiple sheets
        public ArrayList<File> generateCSV()
        {

            ArrayList<ArrayList<SingleDay>> week = weekBuilder();

              ArrayList<File> files = new ArrayList<>();

            for (int i = 0; i < depth; i++) {

                ArrayList<String> weekdays = new ArrayList<>();

                for (int j = 0; j < week.size(); j++) {
                    weekdays.add(stringGenerator(week.get(j), j, i));
                }

               files.add(writer(weekdays));

            }

            return files;

        }

        //Generates a 2d grid of single days. But since a single day can have multiple entries it is in reality a 3d tensor
        private ArrayList<ArrayList<SingleDay>> weekBuilder()
        {
            ArrayList<ArrayList<SingleDay>> sevenDayWeek = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                sevenDayWeek.add(new ArrayList<>());
            }

            //prefill
            blankDay = new SingleDay(System.currentTimeMillis());
            blankDay.add(blankEntry);
            for (int i = 0; i < thismonth.get(0).getDayofWeekasInt(); i++) {
                sevenDayWeek.get(i).add(blankDay);
            }

            //datafill
            for (int i = 0; i < thismonth.size(); i++) {
                sevenDayWeek.get(thismonth.get(i).getDayofWeekasInt()).add(thismonth.get(i));
            }

            //postfill
            for (int i = thismonth.get(thismonth.size()-1).getDayofWeekasInt()+1   ; i < 7; i++) {
                sevenDayWeek.get(i).add(blankDay);
            }

            return sevenDayWeek;


        }

        //return a day of the week completed from a 1D list that has 2nd dimension as extra in same day
        private String stringGenerator(ArrayList<SingleDay> day, int pos, int deep)
        {
            ArrayList<StringBuilder> builder = new ArrayList<>();

            //create stringbuilder and fill first column
            for (int i = 0; i < day.get(0).thisday.get(0).descriptionGrid.size(); i++) {
                builder.add(new StringBuilder());

                if (i == 0 ){
                    switch (pos){

                        case 0:
                            builder.get(i).append("Monday ,");
                            break;

                        case 1:
                            builder.get(i).append("Tuesday ,");
                            break;

                        case 2:
                            builder.get(i).append("Wednesday ,");
                            break;

                        case 3:
                            builder.get(i).append("Thursday ,");
                            break;

                        case 4:
                            builder.get(i).append("Friday ,");
                            break;

                        case 5:
                            builder.get(i).append("Saturday ,");
                            break;

                        case 6:
                            builder.get(i).append("Sunday ,");
                            break;



                    }
                }else{
                    builder.get(i).append(" ,");
                }
            }

            for (int i = 0; i < day.size(); i++) {

                ArrayList<ArrayList<String>> data;

                if (day.get(i).thisday.size()-1 <= deep){
                   data = day.get(i).thisday.get(0).descriptionGrid;
                }else {
                   data = day.get(i).thisday.get(deep).descriptionGrid;
                }



                for (int j = 0; j < builder.size(); j++) {

                    for (int k = 0; k < data.get(j).size(); k++) {
                        String toWrite = data.get(j).get(k);
                        toWrite = toWrite.replaceFirst(",", ".");

                        builder.get(j).append(toWrite).append(",");
                    }



                }


            }
                StringBuilder out = new StringBuilder();
            for (int i = 0; i < builder.size(); i++) {
                //builder.get(j).setLength(builder.get(j).length()-1);
                builder.get(i).append("\n");
                out.append(builder.get(i));
            }

            return out.toString();
        }

        //writes each day
        private File writer(ArrayList<String> wk)
        {

            if (wk.size() != 7){
                // error
            }

            File toReturn = getFilePath(fileTitle, ".csv", mContext);
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;



            try {


                fileWriter = new FileWriter(toReturn);
                bufferedWriter = new BufferedWriter(fileWriter);

                for (int i = 0; i < wk.size(); i++) {
                    bufferedWriter.write(wk.get(i));
                }


            } catch (Exception e) {

                Log.e(TAG, "layoutData: ", e);

            } finally {
                try {

                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }

                } catch (IOException ioe) {
                    Log.e(TAG, "sheetCSV: ", ioe);

                }

            }
            return toReturn;
        }



    }

    /**
     * LocalDate and list of Formatted Entries from that day
     */
    class SingleDay
    {
        LocalDate myDate;

        ArrayList<FormattedEntry> thisday;


        public SingleDay(long milli)
        {
            myDate = Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDate();

                    thisday = new ArrayList<>();
        }

        public void add(FormattedEntry e)
        {
            thisday.add(e);
        }

        public int getDayofWeekasInt()
        {
            int v = myDate.getDayOfWeek().getValue()-1;
            return v;
        }

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

        WorkoutRecord mWR;
        ArrayList<ArrayList<String>> descriptionGrid = new ArrayList<>();
        int width;
        int height;

        int position; //0 = sunday through 6 - saturday

        public FormattedEntry(WorkoutRecord workoutRecord)
        {

            if (workoutRecord == null)
            {  emptyDay();
                return;
            }

            mWR = workoutRecord;

            firstRowPlusPOsition(workoutRecord);
            secondRow(workoutRecord);

            descriptionGrid.addAll(workoutRecord.workout);

            buildWidthHeight();

        }


        private void firstRowPlusPOsition(WorkoutRecord workoutRecord)
        {
            ArrayList<String> firstRow = new ArrayList<>();
            firstRow.add(workoutRecord.splitName);
            firstRow.add(workoutRecord.dayName);

            LocalDate d =  Instant.ofEpochMilli(workoutRecord.datetime).atZone(ZoneId.systemDefault()).toLocalDate();
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
                    s = p.first + "-T";
                }else{
                    s = p.first + "-F";
                }
                secR.add(s);
            }

            if (workoutRecord.notes != null){
                secR.add(workoutRecord.notes);
            }

            descriptionGrid.add(secR);
        }


        private void buildWidthHeight()
        {
            height = descriptionGrid.size();

            for (int i = 0; i < descriptionGrid.size(); i++) {

                if (descriptionGrid.get(i).size() > width) width = descriptionGrid.get(i).size();
            }

        }

        private void emptyDay()
        {

            ArrayList<String> f = new ArrayList<>();
            f.add("No entry");
            descriptionGrid.add(f);
            ArrayList<String> s = new ArrayList<>();
            descriptionGrid.add(s);

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

        public LocalDate getDate()
        {
            return Instant.ofEpochMilli(mWR.datetime).atZone(ZoneId.systemDefault()).toLocalDate();
        }

    }



}
