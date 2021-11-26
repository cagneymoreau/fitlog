package com.cagneymoreau.fitlog.data;

// https://developer.android.com/guide/topics/data/autobackup
//https://developer.android.com/training/data-storage/room#java

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

/**
 * Data is stored via autobackup
 *
 *
 */


    @Database(entities = {UserProfile.class, CheckLists.class, Splits.class, WorkoutRecord.class}, version = 1)
    @TypeConverters({ListConverters.class})
    public abstract class AppDataBase extends RoomDatabase {

        public abstract UserProfileDao userProfileDao();

        public abstract CheckListsDao checkListsDao();

        public abstract  SplitsDao splitsDao();

        public abstract WorkoutRecordDao workoutRecordDao();

    }



