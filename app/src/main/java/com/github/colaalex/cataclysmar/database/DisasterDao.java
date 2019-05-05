package com.github.colaalex.cataclysmar.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DisasterDao {
    @Query("SELECT * FROM disasterentity WHERE type = :disType AND datetime BETWEEN :firstDate AND :secondDate")
    List<DisasterEntity> getDisastersByType(String disType, long firstDate, long secondDate);

    @Query("SELECT COUNT(*) FROM disasterentity WHERE type = :disType AND datetime BETWEEN :firstDate AND :secondDate")
    Float getDisastersCount(String disType, long firstDate, long secondDate);

    @Insert
    void insertDisaster(DisasterEntity... disasterEntities);
}
