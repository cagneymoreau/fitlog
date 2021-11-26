package com.cagneymoreau.fitlog.data;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class WorkoutRecord implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "millis")
    public long datetime;

    @ColumnInfo(name = "split_name")
    public String splitName;

    @ColumnInfo(name = "day_name")
    public String dayName;




    @ColumnInfo(name = "checklist")
    public transient ArrayList<Pair<String, Boolean>> checkList;

    @ColumnInfo(name = "notes")
    public String notes;

    //each sublist will start with a movement description followed by users input
    @ColumnInfo(name = "workout")
    public ArrayList<ArrayList<String>> workout;


}
