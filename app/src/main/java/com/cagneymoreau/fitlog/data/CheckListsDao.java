package com.cagneymoreau.fitlog.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CheckListsDao {

    @Query("Select * FROM checklists")
    List<CheckLists> getAll();

    @Query("Select * FROM checklists WHERE uid")
    CheckLists getSpecific();

    @Insert
    void insert(CheckLists checkLists);

    @Update
    int update(CheckLists checkLists);

    @Delete
    int delete(CheckLists checkLists);


}
