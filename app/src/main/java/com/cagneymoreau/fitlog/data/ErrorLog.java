package com.cagneymoreau.fitlog.data;


import android.content.Context;
import android.util.Pair;

import com.cagneymoreau.fitlog.logic.BackupFileGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import kotlin.Triple;

/**
 * Hold an array of workouts records and the period for email to user support
 * This allows debuggin of non local workout records
 * search "non local" to see how to open a workout record in app
 *
 */


public class ErrorLog implements Serializable {

    ArrayList<WorkoutRecord> records;
    ArrayList<ArrayList<String>> check;
    ArrayList<ArrayList<Boolean>> marks;
    Triple<Long,Long, Boolean> period;


    public ErrorLog(ArrayList<WorkoutRecord> records, Triple<Long,Long, Boolean> period)
    {
        this.records = records;
        this.period = period;

        check = new ArrayList<>();
        marks = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            ArrayList<Pair<String, Boolean>> arr = records.get(i).checkList;
            ArrayList<String> c = new ArrayList<>();
            ArrayList<Boolean> b = new ArrayList<>();

            for (int j = 0; j < arr.size(); j++) {

                c.add(arr.get(j).first);
                b.add(arr.get(j).second);

            }
            check.add(c);
            marks.add(b);
        }


    }


    public ArrayList<WorkoutRecord> getRecords()
    {
        if (check != null){

            for (int i = 0; i < check.size(); i++) {

                for (int j = 0; j < check.get(i).size(); j++) {

                    Pair<String, Boolean> pp = new Pair<>(check.get(i).get(j), marks.get(i).get(j));

                    records.get(i).checkList.add(pp);

                }

            }

        }


        return records;
    }

    public Triple<Long,Long, Boolean> getPeriod()
    {
        return period;
    }

    public String description()
    {
        return String.valueOf(period.getFirst());
    }


    public static File sendToFile(ErrorLog log, Context context) throws Exception
    {
        File f = BackupFileGenerator.getFilePath(String.valueOf(log.description()), ".txt", context);

        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
        stream.writeObject(log);
        stream.close();
        return f;
    }


    public static ErrorLog revertToObject(File f)throws Exception
    {
        ErrorLog log = null;

        FileInputStream inputStream = new FileInputStream(f);
        ObjectInputStream stream = new ObjectInputStream(inputStream);

        log = (ErrorLog) stream.readObject();

        stream.close();
        inputStream.close();

        return log;

    }

}
