package com.cagneymoreau.fitlog.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SplitsDao {

    @Query("Select * FROM splits")
    List<Splits> getAll();

    @Query("Select * FROM splits WHERE uid")
    Splits getSpecific();

    @Insert
    void insert(Splits splits);

    @Update
    int update(Splits splits);

    @Delete
    int delete(Splits splits);

}
