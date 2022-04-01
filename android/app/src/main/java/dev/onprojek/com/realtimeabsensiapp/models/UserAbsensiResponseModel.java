package dev.onprojek.com.realtimeabsensiapp.models;

import com.google.gson.annotations.SerializedName;

public class UserAbsensiResponseModel {
    @SerializedName("nama")
    private String nama;

    @SerializedName("pangkat")
    private String pangkat;

    @SerializedName("nip")
    private String nip;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("jam")
    private String jam;

    @SerializedName("tanggal")
    private String tanggal;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setPangkat(String pangkat) {
        this.pangkat = pangkat;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public String getPangkat() {
        return pangkat;
    }

    public String getNip() {
        return nip;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getJam() {
        return jam;
    }

    public String getTanggal() {
        return tanggal;
    }
}
