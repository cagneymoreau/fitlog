package com.cagneymoreau.fitlog.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WorkoutRecordDao {

    //get the entire history for user review
    @Query("Select * From workoutrecord")
    List<WorkoutRecord> getAll();

    //get most recent uid
    @Query("Select Max(uid) From workoutrecord")
    int getLastEntry();

    @Query("Select * From workoutrecord" + " Where millis Between :done And :start Order By millis")
    //@Query("Select * From workoutrecord Where millis ")
    List<WorkoutRecord> getSelection(long start, long done);

    @Query("Select * From workoutrecord Where uid=:uidR")
    WorkoutRecord getRecordbyUID(int uidR);

    @Query("Select * From workoutrecord Where day_name=:dayN And split_name=:splitN")
    List<WorkoutRecord> getCurated(String dayN, String splitN);

    @Insert
    void insert(WorkoutRecord workoutRecord);

    @Update
    int update(WorkoutRecord workoutRecord);

    @Query("Delete From workoutrecord Where uid = :uidVal")
    int delete(int uidVal);

    @Query("Delete From workoutrecord Where millis < :milliVal")
    void deleteOld(int milliVal);


}
