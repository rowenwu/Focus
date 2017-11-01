package com.pk.example.database.dao;

//import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pk.example.entity.ProfileEntity;

import java.util.List;

@Dao
public interface ProfileDao {

//    @Query("SELECT * FROM profiles")
//    LiveData<List<ProfileEntity>> loadAllProfiles();
//
//    @Query("SELECT * FROM profiles WHERE _active = 1 ")
//    LiveData<List<ProfileEntity>> loadActiveProfiles();

    @Query("SELECT * FROM profiles")
     List<ProfileEntity> loadAllProfilesAsync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProfileEntity> profiles);

    //insert a new profile
    @Insert
    void insert(ProfileEntity profile);

    //delete a profile
    @Delete
    void delete(ProfileEntity profile);

    @Query("DELETE FROM profiles")
    void deleteAll();

    //update a profile
    @Update
    void update(ProfileEntity profile);

//    //load profile
//    @Query("select * from profiles where _name = :profileName")
//    LiveData<ProfileEntity> loadProfile(String profileName);

    @Query("select * from profiles where _name = :profileName")
    ProfileEntity loadProfileSync(String profileName);

}