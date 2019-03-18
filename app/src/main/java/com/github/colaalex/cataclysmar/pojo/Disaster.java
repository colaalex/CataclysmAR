package com.github.colaalex.cataclysmar.pojo;

import java.io.Serializable;

import androidx.annotation.NonNull;

public abstract class Disaster implements Serializable {
    protected float latitude;
    protected float longitude;

    public Disaster(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public abstract String toString();
}
