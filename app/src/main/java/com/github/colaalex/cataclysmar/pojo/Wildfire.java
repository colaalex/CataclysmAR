package com.github.colaalex.cataclysmar.pojo;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Wildfire implements Serializable {
    private float latitude;
    private float longitude;
    private int confidence;

    public Wildfire(float latitude, float longitude, int confidence) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.confidence = confidence;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Latitude %f\nLongitude %f\nConfidence %d", latitude, longitude, confidence);
    }
}
