package com.arjun.alaram.Room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arjun.alaram.POJO.Data;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PVM extends AndroidViewModel {
    ExecutorService executorService;
    DAO dao;
    public PVM(@NonNull Application application) {
        super(application);
        dao = AlarmDatabase.getDatabaseInstance(application.getApplicationContext()).getDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertData(final Data data){
         executorService.execute(new Runnable() {
             @Override
             public void run() {
                 dao.createAlarm(data);
             }
         });

    }
    public LiveData<List<Data>> getAllData(){
        return dao.getAllData();
    }
    public void updateData(final Data data){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateAlarm(data);
            }
        });
    }
    public void deleteData(final Data data){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAlarm(data);
            }
        });
    }
}
