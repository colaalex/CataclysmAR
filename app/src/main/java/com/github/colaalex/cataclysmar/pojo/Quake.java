package com.github.colaalex.cataclysmar.pojo;

import java.util.Locale;

import androidx.annotation.NonNull;

public class Quake extends Disaster {
    private float magnitude;

    public Quake(float latitude, float longitude, float magnitude) {
        super(latitude, longitude);
        this.magnitude = magnitude;
    }

    public float getMagnitude() {
        return magnitude;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Latitude %f\nLongitude %f\nConfidence %f", latitude, longitude, magnitude);
    }
}
