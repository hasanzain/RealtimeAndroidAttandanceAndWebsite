package dev.onprojek.com.realtimeabsensiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    final static String KEY_NAME = "MySharedPrefs";
    final static String NAMA = "username_logged_in";
    final static String NIP = "nip_logged_in";
    final static String PANGKAT = "pangkat_logged_in";
    final static String LAT = "lat_logged_in";
    final static String LONG = "long_logged_in";
    final static String BACKGROUND_JOB = "background_job_has_started";


    public static void setBackgroundJob(Context context, boolean started) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(BACKGROUND_JOB, started);
        editor.apply();
    }

    public static boolean getBackgroundJob(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(BACKGROUND_JOB, false);
    }

    public static boolean checkBackgroundJob(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.contains(BACKGROUND_JOB);
    }

    public static void setNama(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAMA, name);
        editor.apply();
    }

    public static String getNama(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(NAMA, "");
    }

    public static boolean checkNama(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.contains(NAMA);
    }

    public static void removeNama(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(NAMA);
        editor.apply();
    }

    public static void setNip(Context context, String nip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NIP, nip);
        editor.apply();
    }

    public static String getNip(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(NIP, "");
    }

    public static void removeNip(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(NIP);
        editor.apply();
    }

    public static void setPangkat(Context context, String pangkat) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PANGKAT, pangkat);
        editor.apply();
    }

    public static String getPangkat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(PANGKAT, "");
    }

    public static void removePangkat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(PANGKAT);
        editor.apply();
    }

    public static void setLat(Context context, float latitude) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(LAT, latitude);
        editor.apply();
    }

    public static float getLat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        float lat = (float) 0.0;
        return sharedPreferences.getFloat(LAT, lat);
    }

    public static void setLong(Context context, float longitude) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(LONG, longitude);
        editor.apply();
    }

    public static float getLong(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        float longitude = (float) 0.0;
        return sharedPreferences.getFloat(LONG, longitude);
    }

    public static void clearPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

}
