package com.example.practicaltouch.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppSetDAO {
    @Insert
    void insert(AppSet appSet);

    @Update
    void update(AppSet appSet);

    @Delete
    void delete(AppSet appSet);

    @Query("DELETE FROM appset_table")
    void deleteAllAppSets();

    @Query("SELECT * FROM appset_table ORDER BY id DESC")
    LiveData<List<AppSet>> getAllAppSets();

    @Query("SELECT * FROM appset_table ORDER BY id DESC")
    List<AppSet> getList();
}
