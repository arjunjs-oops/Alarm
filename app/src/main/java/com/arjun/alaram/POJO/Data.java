package com.arjun.alaram.POJO;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "alarm")
public class Data {


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "calender")
    private String calender;

    @ColumnInfo(name="title")
    private String title;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Data() {
    }

    public Data(String date,String title) {
        this.calender = date;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Data{" +
                "uid=" + uid +
                ", calender='" + calender + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getCalender() {
        return calender;
    }

    public void setCalender(String calender) {
        this.calender = calender;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title;}
}
