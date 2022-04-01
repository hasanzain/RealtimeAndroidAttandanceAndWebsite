package dev.onprojek.com.realtimeabsensiapp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TimeConstraints {

    final static String DATE_PATTERN = "yyyy-MM-dd";
    final static String START_IN = "08:00";
    final static String END_IN = "09:00";

    final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm";
    final static String START_OUT = "15:00";
    final static String END_OUT = "16:00";

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(new Date());
    }

    public static boolean checkContraintsIn() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime timeNow =LocalDateTime.now();

            String currentDate = getCurrentDate();
            String startAbsenMasuk = currentDate + " " + START_IN;
            String endAbsenMasuk = currentDate + " " + END_IN;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

            LocalDateTime start = LocalDateTime.parse(startAbsenMasuk, formatter);
            LocalDateTime end = LocalDateTime.parse(endAbsenMasuk, formatter);

            return timeNow.isAfter(start) && timeNow.isBefore(end);
        } else return false;
    }

    public static boolean checkConstraintsOut() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime timenow = LocalDateTime.now();

            String start_ = getCurrentDate() + " " + START_OUT;
            String end_ = getCurrentDate() + " " + END_OUT;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

            LocalDateTime start = LocalDateTime.parse(start_, formatter);
            LocalDateTime end = LocalDateTime.parse(end_, formatter);

            return timenow.isAfter(start) && timenow.isBefore(end);
        } else return false;
    }
}
