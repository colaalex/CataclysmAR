package com.github.colaalex.cataclysmar.pojo;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Wildfire extends Disaster {

    private int confidence;

    public Wildfire(float latitude, float longitude, int confidence) {
        super(latitude, longitude);
        this.confidence = confidence;
    }

    public int getConfidence() {
        return confidence;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Latitude %f\nLongitude %f\nConfidence %d", latitude, longitude, confidence);
    }
}
