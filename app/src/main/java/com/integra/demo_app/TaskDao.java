package com.integra.demo_app;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM taskmodel")
    List<TaskModel> getAllTasks();

    @Insert
    void insert(TaskModel taskModel);

    @Update
    void update(TaskModel taskModel);

    @Delete
    void delete(TaskModel taskModel);

}
