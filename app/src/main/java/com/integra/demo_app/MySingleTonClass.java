package com.integra.demo_app;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by integra on 24-04-2019.
 */

public class MySingleTonClass {
    private static MySingleTonClass ourInstance;
    private Context context;
    private AppDataBase appDataBase;

    private MySingleTonClass(Context context) {
        this.context = context;
        appDataBase = Room.databaseBuilder(context, AppDataBase.class, "tasks").build();
    }

    public static synchronized MySingleTonClass getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MySingleTonClass(context);
        }
        return ourInstance;
    }

    public AppDataBase getAppDataBase() {
        return appDataBase;
    }


}
