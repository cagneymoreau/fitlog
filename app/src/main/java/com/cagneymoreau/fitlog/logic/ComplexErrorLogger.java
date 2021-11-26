package com.cagneymoreau.fitlog.logic;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cagneymoreau.fitlog.data.WorkoutRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Takes data that throws error in complex data outputs and dumps in simple error reduced way fro debugging
 */
public class ComplexErrorLogger {

    /*

    ArrayList<StringBuilder> errors = new ArrayList<>();
    ArrayList<File> serializedRecords = new ArrayList<>();

    Context context;

    public ComplexErrorLogger(Context c)
    {
        context = c;

    }

    public void addErrorList(ArrayList<WorkoutRecord> recs)
    {
        for (int i = 0; i < recs.size(); i++) {
            addErrorData(recs.get(i), i);
        }
    }

    public void addErrorData(WorkoutRecord wk, int p)
    {
        while(errors.size() <= p){

            errors.add(new StringBuilder());

        }


        errors.get(p).append("workout: ");

        if (wk.workout == null)
        {
            errors.get(p).append("master null");
        }
        else {
            errors.get(p).append("master: ");
            for (int i = 0; i < wk.workout.size(); i++) {

                if (wk.workout.get(i) == null){
                    errors.get(p).append("sub null");
                }else{
                    errors.get(p).append("sub:");
                    for (int j = 0; j < wk.workout.get(i).size(); j++) {
                        errors.get(p).append(wk.workout.get(i).get(j));
                    }
                }


            }
        }

        errors.get(p).append("checklist: ");

        for (int i = 0; i < wk.checkList.size(); i++) {
            errors.get(p).append(wk.checkList.get(i).first);
        }

        errors.get(p).append("notes: ");
        errors.get(p).append(wk.notes);

    }




    public File gatherFile() throws Exception
    {
        File toReturn = BackupFileGenerator.getFilePath("errorlogger", ".txt", context);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

            fileWriter = new FileWriter(toReturn);
            bufferedWriter = new BufferedWriter(fileWriter);

        String nextLine = System.lineSeparator();

            for (int i = 0; i < errors.size(); i++) {
                bufferedWriter.write(errors.get(i).toString());
                bufferedWriter.write(nextLine);
            }


                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }


        return toReturn;
    }


    public String openFile(String filename) throws IOException
    {
        String in = "";
        StringBuilder sb = new StringBuilder();

        InputStream is = context.openFileInput(filename);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((in = reader.readLine()) != null)
        {

            sb.append(in).append("\n");

        }
        is.close();

        return sb.toString();
    }



    //serializable strategy

    public void addSerializeErrorList(ArrayList<WorkoutRecord> recs) throws Exception
    {

        for (int i = 0; i < recs.size(); i++) {

            File f = BackupFileGenerator.getFilePath(String.valueOf(recs.get(i).datetime), ".txt", context);

            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
            stream.writeObject(recs.get(i));
            stream.close();
            serializedRecords.add(f);

        }



    }

    public ArrayList<File> getFiles()
    {
        return serializedRecords;
    }


    public WorkoutRecord examinFile(File f)
    {



    }


     */

}
