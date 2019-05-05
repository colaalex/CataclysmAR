package com.github.colaalex.cataclysmar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DisasterEntity {
    @ColumnInfo(name = "type")
    public String disasterType;
    @ColumnInfo(name = "datetime")
    public long datetime;
    @ColumnInfo(name = "latitude")
    public float latitude;
    @ColumnInfo(name = "longitude")
    public float longitude;
    @ColumnInfo(name = "size")
    public float size;
    @PrimaryKey(autoGenerate = true)
    int id;
}
