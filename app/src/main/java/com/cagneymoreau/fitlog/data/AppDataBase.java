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

    // TODO: 5/14/2021 users need to understand that they cant disable auto backups, they need wifi, must have a play store account and they must email themselves for long term data backup

    @Database(entities = {UserProfile.class, CheckLists.class, Splits.class, WorkoutRecord.class}, version = 1)
    @TypeConverters({ListConverters.class})
    public abstract class AppDataBase extends RoomDatabase {

        public abstract UserProfileDao userProfileDao();

        public abstract CheckListsDao checkListsDao();

        public abstract  SplitsDao splitsDao();

        public abstract WorkoutRecordDao workoutRecordDao();

    }



