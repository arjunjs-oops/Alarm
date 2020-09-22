package com.arjun.alaram.Room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arjun.alaram.POJO.Data;

import java.util.List;

@Dao
public interface DAO{

    @Insert
    void createAlarm(Data data);


    @Update
    void updateAlarm(Data data);

    @Delete
    void deleteAlarm(Data...data);


    @Query("SELECT * from alarm ")
    LiveData<Data>getAllData();
}
