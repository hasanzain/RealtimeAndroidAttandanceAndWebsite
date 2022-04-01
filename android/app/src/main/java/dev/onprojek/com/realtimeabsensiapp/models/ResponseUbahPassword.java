package dev.onprojek.com.realtimeabsensiapp.models;

import com.google.gson.annotations.SerializedName;

public class ResponseUbahPassword {
    @SerializedName("password")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
