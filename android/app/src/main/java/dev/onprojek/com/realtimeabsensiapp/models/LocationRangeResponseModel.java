package dev.onprojek.com.realtimeabsensiapp.models;

import com.google.gson.annotations.SerializedName;

public class LocationRangeResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("longitudeMin")
    private double longitudeMin;

    @SerializedName("latitudeMin")
    private double latitudeMin;

    @SerializedName("longitudeMax")
    private double longitudeMax;

    @SerializedName("latitudeMax")
    private double latitudeMax;

    public void setId(int id) {
        this.id = id;
    }

    public void setLongitudeMin(double longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public void setLatitudeMin(double latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public void setLongitudeMax(double longitudeMax) {
        this.longitudeMax = longitudeMax;
    }

    public void setLatitudeMax(double latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

    public int getId() {
        return id;
    }

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }
}
