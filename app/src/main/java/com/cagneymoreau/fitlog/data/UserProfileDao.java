package com.cagneymoreau.fitlog.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserProfileDao {

    @Query("Select * FROM userprofile")
    List<UserProfile> getAll();

    @Insert
    void insert(UserProfile userProfile);

    @Update
    void update(UserProfile userProfile);



}
