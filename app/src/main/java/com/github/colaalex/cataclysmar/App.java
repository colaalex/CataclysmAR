package com.github.colaalex.cataclysmar;

import android.app.Application;

import com.github.colaalex.cataclysmar.database.AppDatabase;

import androidx.room.Room;

public class App extends Application {

    public static App instance;
    private AppDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
