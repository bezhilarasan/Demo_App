package com.integra.demo_app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {TaskModel.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
