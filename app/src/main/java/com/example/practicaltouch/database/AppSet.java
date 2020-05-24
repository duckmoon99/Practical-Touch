package com.example.practicaltouch.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "appset_table")
public class AppSet {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @TypeConverters(AppIdsListConverter.class)
    private AppIdsList appIdsList;

    public AppSet(String name, AppIdsList appIdsList) {
        this.name = name;
        this.appIdsList = appIdsList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AppIdsList getAppIdsList() {
        return appIdsList;
    }
}
