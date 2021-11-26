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

    // abandon
    @ColumnInfo(name = "selected")
    public boolean selected;

    @ColumnInfo(name = "last_used")
    public long lastUsedMilli;

    @ColumnInfo(name = "checkList")
    public ArrayList<String> checkList;

    //abandon
    @ColumnInfo(name = "notes")
    public boolean notes;




}
