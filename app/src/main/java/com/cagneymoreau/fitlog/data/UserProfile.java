package com.cagneymoreau.fitlog.data;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import kotlin.Triple;

@Entity
public class UserProfile {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "first_launch")
    public boolean firstLaunch;

    @ColumnInfo(name = "check_list")
    public boolean checkList;

    @ColumnInfo(name = "statistics")
    public boolean statistics;

    @ColumnInfo(name = "body_measure")
    public boolean bodyMeasure;

    @ColumnInfo(name = "trophies")
    public ArrayList<Integer> trophies;

    @ColumnInfo(name = "movements")
    public ArrayList<String> movements;

    @ColumnInfo(name = "backups")
    public ArrayList<Triple<Long,Long, Boolean>> backups;

    @ColumnInfo(name = "timers")
    public ArrayList<Pair<Integer, Integer>> timers;

}
