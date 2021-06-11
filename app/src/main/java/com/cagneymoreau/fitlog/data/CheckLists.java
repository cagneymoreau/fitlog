package com.cagneymoreau.fitlog.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class CheckLists {


    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "checkList_name")
    public String checkListName;

    // TODO: 6/3/2021 abandone this feature
    @ColumnInfo(name = "selected")
    public boolean selected;

    @ColumnInfo(name = "last_used")
    public long lastUsedMilli;

    @ColumnInfo(name = "checkList")
    public ArrayList<String> checkList;

    // TODO: 6/3/2021 this isnt really needed because its always true and no data is stored here
    //the actual notes are for that specific workout sesh
    @ColumnInfo(name = "notes")
    public boolean notes;




}
