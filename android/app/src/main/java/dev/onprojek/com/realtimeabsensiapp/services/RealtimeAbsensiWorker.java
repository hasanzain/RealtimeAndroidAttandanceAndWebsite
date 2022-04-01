package dev.onprojek.com.realtimeabsensiapp.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.onprojek.com.realtimeabsensiapp.R;
import dev.onprojek.com.realtimeabsensiapp.models.RealtimeAbsensiResponseModel;
import dev.onprojek.com.realtimeabsensiapp.utils.Constants;
import dev.onprojek.com.realtimeabsensiapp.utils.Preferences;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static dev.onprojek.com.realtimeabsensiapp.utils.Constants.CHANNEL_ID;
import static dev.onprojek.com.realtimeabsensiapp.utils.Constants.NOTIFICATION_ID;
import static dev.onprojek.com.realtimeabsensiapp.utils.TimeConstraints.getCurrentDate;

public class RealtimeAbsensiWorker extends Worker {

    private FusedLocationProviderClient mFusedLocation;

    public RealtimeAbsensiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime localDateTime = LocalDateTime.now();

                String currentDate = getCurrentDate();
                String startAbsen = currentDate + " " + "08:00";
                String endAbsen = currentDate + " " + "17:00";

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime ldtStartAbsen = LocalDateTime.parse(startAbsen, dateTimeFormatter);
                LocalDateTime ldtEndAbsen = LocalDateTime.parse(endAbsen, dateTimeFormatter);

                if (localDateTime.isAfter(ldtStartAbsen) && localDateTime.isBefore(ldtEndAbsen)) {
                    startLocationUpdate();
                } else
                    makeNotification("Tidak mengirim ke server. diluar jam");
            }

            return Result.success();
        } catch (Throwable t) {
            return Result.failure();
        }

    }

    public void makeNotification(String message) {

        Context context = getApplicationContext();

        // Mendaftarkan channel notification ke sistem (hanya untuk API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // membuat channel
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            // mendaftakan channel ke sistem.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        // membuat notifikasi dasar -> dibutuhkan channel yang telah didaftarkan
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Log notifikasi")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);

        // menampilkan notifikasi
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        mFusedLocation = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    // Error, lokasi tidak didapatkan
                    makeNotification("Error, lokasi tidak didapatkan");
                } else {
                    String nip = Preferences.getNip(getApplicationContext());
                    String longitude = String.valueOf(location.getLongitude());
                    String latitude = String.valueOf(location.getLatitude());

                    String result = "Lokasi dikonfirmasi: Long " + longitude + " Lat: " + latitude;
                    sendingLog(nip, latitude, longitude);
                }
            }
        });
    }


    private void sendingLog(String nip, String latitude, String longitude) {
        Retrofit retrofit = RetrofitApiClient.getRetrofitClientInstance();

        RealtimeAbsensiInterface realtimeAbsensiInterface = retrofit.create(RealtimeAbsensiInterface.class);

        final MediaType contentType = MediaType.parse("text/plain");
        final RequestBody rNip = RequestBody.create(contentType, nip);
        final RequestBody rLatitude = RequestBody.create(contentType, latitude);
        final RequestBody rLongitude = RequestBody.create(contentType, longitude);

        Call<RealtimeAbsensiResponseModel> call = realtimeAbsensiInterface.realtime(rNip, rLongitude, rLatitude);
        call.enqueue(new Callback<RealtimeAbsensiResponseModel>() {
             @Override
             public void onResponse(Call<RealtimeAbsensiResponseModel> call, Response<RealtimeAbsensiResponseModel> response) {
                if (response.code() == 200) makeNotification("Lokasi berhasil dikirim ke server");
                else makeNotification("Lokasi gagal dikirim " + response.code());
             }

             @Override
             public void onFailure(Call<RealtimeAbsensiResponseModel> call, Throwable t) {
                makeNotification("Lokasi gagal dikirim " + t.getMessage());
             }
        });
    }
}
