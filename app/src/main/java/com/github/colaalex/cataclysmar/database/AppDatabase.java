package com.github.colaalex.cataclysmar.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DisasterEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DisasterDao disasterDao();
}
