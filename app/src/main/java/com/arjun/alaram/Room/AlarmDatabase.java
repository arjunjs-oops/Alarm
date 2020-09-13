package com.arjun.alaram.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arjun.alaram.Fragments.alarm;
import com.arjun.alaram.POJO.Data;


@Database(entities = {Data.class},version = 1)
abstract class AlarmDatabase extends RoomDatabase {
    public static AlarmDatabase instance =null;
    private static final String dbName = "Room_Alarm";

    public static AlarmDatabase getDatabaseInstance(final Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AlarmDatabase.class,
                    dbName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract DAO getDAO();
}
