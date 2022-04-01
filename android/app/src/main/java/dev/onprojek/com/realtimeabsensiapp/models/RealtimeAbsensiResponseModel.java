package dev.onprojek.com.realtimeabsensiapp.models;

import com.google.gson.annotations.SerializedName;

public class RealtimeAbsensiResponseModel {
    @SerializedName("nip")
    private String nip;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("jam")
    private String jam;

    @SerializedName("tanggal")
    private String tanggal;

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNip() {
        return nip;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getJam() {
        return jam;
    }

    public String getTanggal() {
        return tanggal;
    }
}
