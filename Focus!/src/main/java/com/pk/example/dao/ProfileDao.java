package com.pk.example.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pk.example.entity.ProfileEntity;

import java.util.List;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM profiles")
    LiveData<List<ProfileEntity>> loadAllProfiles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProfileEntity> profiles);

    @Query("select * from profiles where _name = :profileName")
    LiveData<ProfileEntity> loadProfile(String profileName);

    @Query("select * from profiles where _name = :profileName")
    ProfileEntity loadProfileSync(String profileName);



}