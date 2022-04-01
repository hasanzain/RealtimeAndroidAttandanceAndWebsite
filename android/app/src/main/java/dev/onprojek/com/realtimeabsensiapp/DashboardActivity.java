package dev.onprojek.com.realtimeabsensiapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.onprojek.com.realtimeabsensiapp.models.LocationRangeResponseModel;
import dev.onprojek.com.realtimeabsensiapp.models.UserAbsensiResponseModel;
import dev.onprojek.com.realtimeabsensiapp.services.RealtimeAbsensiInterface;
import dev.onprojek.com.realtimeabsensiapp.services.RealtimeAbsensiWorker;
import dev.onprojek.com.realtimeabsensiapp.services.RetrofitApiClient;
import dev.onprojek.com.realtimeabsensiapp.utils.Preferences;
import dev.onprojek.com.realtimeabsensiapp.utils.TimeConstraints;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardActivity extends AppCompatActivity {

    private MaterialButton mButtonLogout, mButtonAbsen, mButtonNavtoupdate;
    private TextView mTextviewInformasiUser, mTextviewInformasiTanggal;

    private Retrofit retrofit;
    private FusedLocationProviderClient mFusedLocation;

    private RealtimeAbsensiInterface realtimeAbsensiInterface;
    private WorkManager mWorkManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initialComponents();
        initialBackgroundWork();
        initialAutomaticAbsensi();
    }

    public void initialAutomaticAbsensi() {
        startAbsenProcess();
    }

    public void initialBackgroundWork() {
        mWorkManager = WorkManager.getInstance(getApplicationContext());
        Log.d("TAG", "initialBackgroundWork: " + Preferences.getBackgroundJob(getApplicationContext()));
        if (!Preferences.getBackgroundJob(getApplicationContext()))
            setPeriodicSendLog();
    }

    private void setPeriodicSendLog() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(RealtimeAbsensiWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                    .setConstraints(Constraints.NONE)
                    .build();

            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RealtimeAbsensiWorker.class).build();

            mWorkManager.enqueue(oneTimeWorkRequest);
            mWorkManager.enqueue(periodicWorkRequest);

            Toast.makeText(getApplicationContext(), "Periodic send log started", Toast.LENGTH_SHORT).show();
            Preferences.setBackgroundJob(getApplicationContext(), true);
        }
    }

    public void initialComponents() {
        mButtonAbsen = (MaterialButton) findViewById(R.id.absenBtn);
        mButtonLogout = (MaterialButton) findViewById(R.id.logoutBtn);
        mButtonNavtoupdate = (MaterialButton) findViewById(R.id.navToUpdate);

        mTextviewInformasiTanggal = (TextView) findViewById(R.id.informasiTanggal);
        mTextviewInformasiUser = (TextView) findViewById(R.id.informasiUser);

        // mButtonLogout.setOnClickListener(new View.OnClickListener());
        mButtonLogout.setOnClickListener(logoutListener);
        mButtonAbsen.setOnClickListener(absensiListener);
        mButtonNavtoupdate.setOnClickListener(navListener);

        retrofit = RetrofitApiClient.getRetrofitClientInstance();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        realtimeAbsensiInterface = retrofit.create(RealtimeAbsensiInterface.class);

        mTextviewInformasiTanggal.setText("Today: " + TimeConstraints.getCurrentDate());
        mTextviewInformasiUser.setText(Preferences.getNama(getApplicationContext()));
    }

    private final View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Navigation to update user
            Intent intent = new Intent(getApplicationContext(), UpdateUserActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // 1. Clear all preferences
            Preferences.clearPrefs(getApplicationContext());

            // start new activity
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    };


    private final View.OnClickListener absensiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startAbsenProcess();
        }
    };

    private void startAbsenProcess() {
        if (!TimeConstraints.checkContraintsIn() && !TimeConstraints.checkConstraintsOut()) {
            // Absensi gagal
            // Diluar batas waktu absensi
            Toast.makeText(DashboardActivity.this, "Absensi gagal, di luar waktu absen", Toast.LENGTH_SHORT).show();
        } else {
            absenLokasi();
        }
    }

    @SuppressLint("MissingPermission")
    private void absenLokasi() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Do: permission request
            requestPermissions();
            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String message = "Lokasi anda telah dikonfirmasi " + "Latitude: " + latitude + " Longitude: " + longitude;
                    Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();

                    double[] userLocation = new double[2];

                    userLocation[0] = latitude;
                    userLocation[1] = longitude;

                    // userLocation[0] = -8.114084;
                    // userLocation[1] = 111.796544;
                    getLocation(userLocation);
                } else
                    Log.d("TAGLOC", "onSuccess: Gagal dapat lokasi");
            }
        });
    }

    private void requestPermissions() {
        // REQUEST FOR PERMISSIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Toast.makeText(DashboardActivity.this, "MAKE PERMISSIONS", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, 44);
        }
    }


    private void getLocation(double[] userLocation) {
        Call<List<LocationRangeResponseModel>> call = realtimeAbsensiInterface.getLocationRange();

        call.enqueue(new Callback<List<LocationRangeResponseModel>>() {
            @Override
            public void onResponse(Call<List<LocationRangeResponseModel>> call, Response<List<LocationRangeResponseModel>> response) {
                if (response.code() != 200) {
                    // Get code
                    // Error here
                    Toast.makeText(DashboardActivity.this, "Absensi gagal. Gagal mendapatkan lokasi dari server", Toast.LENGTH_SHORT).show();
                } else {
                    double latMax = response.body().get(0).getLatitudeMax();
                    double latMin = response.body().get(0).getLatitudeMin();

                    double longMax = response.body().get(0).getLongitudeMax();
                    double longMin = response.body().get(0).getLongitudeMin();

                    // Reversed latitude
                    boolean latInRange = userLocation[0] <= latMin && userLocation[0] >= latMax;
                    boolean lonInRange = userLocation[1] >= longMin && userLocation[1] <= longMax;

                    // String posLong = String.valueOf(longMax) + ", " + String.valueOf(longMin);
                    // String posLat = String.valueOf(latMax) + ", " + String.valueOf(latMin);

                    // Log.d("TAG LONG", "onResponse: " + posLong);
                    // Log.d("TAG LAT", "onResponse: " + posLat);

                    if (latInRange && lonInRange) {
                        // Toast.makeText(DashboardActivity.this, "Absensi sukses", Toast.LENGTH_SHORT).show();
                        absenProses(userLocation);
                    } else {
                        Toast.makeText(DashboardActivity.this, "Absensi gagal. Di luar area absensi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LocationRangeResponseModel>> call, Throwable t) {
                // Error here
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void absenProses(double[] location) {

        Context context = getApplicationContext();

        String name = Preferences.getNama(context);
        String nip = Preferences.getNip(context);
        String pangkat = Preferences.getPangkat(context);
        String latitude = String.valueOf(location[0]);
        String longitude = String.valueOf(location[1]);

        final RequestBody rNama = RequestBody.create(MediaType.parse("text/plain"), name);
        final RequestBody rNip = RequestBody.create(MediaType.parse("text/plain"), nip);
        final RequestBody rPangkat = RequestBody.create(MediaType.parse("text/plain"), pangkat);
        final RequestBody rLatitude = RequestBody.create(MediaType.parse("text/plain"), latitude);
        final RequestBody rLongitude = RequestBody.create(MediaType.parse("text/plain"), longitude);

        Call<UserAbsensiResponseModel> call = realtimeAbsensiInterface.absen(rNama, rPangkat, rNip, rLatitude, rLongitude);
        call.enqueue(new Callback<UserAbsensiResponseModel>() {
            @Override
            public void onResponse(Call<UserAbsensiResponseModel> call, Response<UserAbsensiResponseModel> response) {
                if (response.code() == 200) {
                    Toast.makeText(DashboardActivity.this, "Absensi berhasil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Absensi gagal " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAbsensiResponseModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}