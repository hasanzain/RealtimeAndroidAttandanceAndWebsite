package dev.onprojek.com.realtimeabsensiapp.models;

import com.google.gson.annotations.SerializedName;

public class UserLoginResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("nip")
    private String nip;

    @SerializedName("password")
    private String password;

    @SerializedName("pangkat")
    private String pangkat;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("email")
    private String email;

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPangkat(String pangkat) {
        this.pangkat = pangkat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNip() {
        return nip;
    }

    public String getPassword() {
        return password;
    }

    public String getPangkat() {
        return pangkat;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }

}
