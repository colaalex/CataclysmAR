package com.github.colaalex.cataclysmar.domain.entity;

import java.io.Serializable;

public class Wildfire implements Serializable {
    private float latitude;
    private float longitutde;
    private int confidence;

    public Wildfire(float latitude, float longitutde, int confidence) {
        this.latitude = latitude;
        this.longitutde = longitutde;
        this.confidence = confidence;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitutde() {
        return longitutde;
    }

    public void setLongitutde(float longitutde) {
        this.longitutde = longitutde;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
}
