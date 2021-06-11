package com.cagneymoreau.fitlog.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 *
 * A split has 7 possible days with 10 possible exercises
 *
 */



@Entity
public class Splits {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "split_name")
    public String splitName;

    @ColumnInfo(name = "last_used")
    public long lastUsedMilli;

    @ColumnInfo(name = "day_1")
    public ArrayList<String> dayOne;

    @ColumnInfo(name = "day_2")
    public ArrayList<String> dayTwo;

    @ColumnInfo(name = "day_3")
    public ArrayList<String> dayThree;

    @ColumnInfo(name = "day_4")
    public ArrayList<String> dayFour;

    @ColumnInfo(name = "day_5")
    public ArrayList<String> dayFive;

    @ColumnInfo(name = "day_6")
    public ArrayList<String> daySix;

    @ColumnInfo(name = "day_7")
    public ArrayList<String> daySeven;

    @ColumnInfo(name = "day_8")
    public ArrayList<String> dayEight;

    @ColumnInfo(name = "day_9")
    public ArrayList<String> dayNine;

    @ColumnInfo(name = "day_10")
    public ArrayList<String> dayTen;



}
