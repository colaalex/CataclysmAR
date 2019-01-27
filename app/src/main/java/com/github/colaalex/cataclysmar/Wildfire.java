package com.github.colaalex.cataclysmar;

import com.opencsv.bean.CsvBindByName;

public class Wildfire {
    @CsvBindByName
    private String latitude;
    @CsvBindByName
    private String longitude;
    @CsvBindByName
    private String brightness;
    @CsvBindByName
    private String scan;
    @CsvBindByName
    private String track;
    @CsvBindByName
    private String acq_date;
    @CsvBindByName
    private String acq_time;
    @CsvBindByName
    private String satellite;
    @CsvBindByName
    private String confidence;
    @CsvBindByName
    private String version;
    @CsvBindByName
    private String bright_t31;
    @CsvBindByName
    private String frp;
    @CsvBindByName
    private String daynight;

    public Wildfire(String latitude, String longitude, String brightness, String scan, String track, String acq_date, String acq_time, String satellite, String confidence, String version, String bright_t31, String frp, String daynight) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.brightness = brightness;
        this.scan = scan;
        this.track = track;
        this.acq_date = acq_date;
        this.acq_time = acq_time;
        this.satellite = satellite;
        this.confidence = confidence;
        this.version = version;
        this.bright_t31 = bright_t31;
        this.frp = frp;
        this.daynight = daynight;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAcq_date() {
        return acq_date;
    }

    public void setAcq_date(String acq_date) {
        this.acq_date = acq_date;
    }

    public String getAcq_time() {
        return acq_time;
    }

    public void setAcq_time(String acq_time) {
        this.acq_time = acq_time;
    }

    public String getSatellite() {
        return satellite;
    }

    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBright_t31() {
        return bright_t31;
    }

    public void setBright_t31(String bright_t31) {
        this.bright_t31 = bright_t31;
    }

    public String getFrp() {
        return frp;
    }

    public void setFrp(String frp) {
        this.frp = frp;
    }

    public String getDaynight() {
        return daynight;
    }

    public void setDaynight(String daynight) {
        this.daynight = daynight;
    }
}
