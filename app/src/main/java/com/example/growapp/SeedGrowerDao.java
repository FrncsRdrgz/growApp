package com.example.growapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SeedGrowerDao {

    @Query("SELECT * from seedgrower WHERE isSent=0")
    LiveData<List<SeedGrower>> getAll();

    @Query("SELECT * from seedgrower WHERE isSent=1")
    List<SeedGrower> getSentForms();

    @Query("SELECT * from seedgrower WHERE sgId =:sgId")
    SeedGrower findFormById(String sgId);

    @Insert
    void insertSeedGrower(SeedGrower seedGrower);

    @Update
    void updateSeedGrower(SeedGrower seedGrower);

    @Delete
    void deleteSeedGrower(SeedGrower seedGrower);
}
