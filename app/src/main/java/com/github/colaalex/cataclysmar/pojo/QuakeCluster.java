package com.github.colaalex.cataclysmar.pojo;

import java.util.Locale;

import androidx.annotation.NonNull;

public class QuakeCluster extends Disaster {

    private float size;

    public QuakeCluster(float latitude, float longitude, float size) {
        super(latitude, longitude);
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Latitude %f\nLongitude %f", latitude, longitude);
    }
}
