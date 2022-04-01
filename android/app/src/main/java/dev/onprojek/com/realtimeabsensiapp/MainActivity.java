package dev.onprojek.com.realtimeabsensiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import dev.onprojek.com.realtimeabsensiapp.models.UserLoginResponseModel;
import dev.onprojek.com.realtimeabsensiapp.services.RealtimeAbsensiInterface;
import dev.onprojek.com.realtimeabsensiapp.services.RetrofitApiClient;
import dev.onprojek.com.realtimeabsensiapp.utils.MD5;
import dev.onprojek.com.realtimeabsensiapp.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextNip, mEditTextPassword;
    private Button mButtonLogin;

    private Retrofit retrofit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialComponent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPreferences();
    }

    public void checkPreferences() {
        boolean exists = Preferences.checkNama(getApplicationContext());
        if (exists) {
            // Then start another activity
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void initialComponent() {
        mEditTextNip = (EditText) findViewById(R.id.nip);
        mEditTextPassword = (EditText) findViewById(R.id.password);

        mButtonLogin = findViewById(R.id.loginbtn);
        mButtonLogin.setOnClickListener(loginClickListener);

        retrofit = RetrofitApiClient.getRetrofitClientInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
    }


    private final View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            progressDialog.setMessage("Loading ...");
            progressDialog.setTitle("Login user");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            // LOGIN here
            String nip = mEditTextNip.getText().toString();
            String passwd = mEditTextPassword.getText().toString();

            if (nip.isEmpty())
                Toast.makeText(MainActivity.this, "NRP tidak boleh kosong", Toast.LENGTH_SHORT).show();
            else if (passwd.isEmpty())
                Toast.makeText(MainActivity.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            else {
                try {
                    String hashPassword = MD5.getMd5(passwd);
                    login(nip, hashPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "hashPassword failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void login(String nip, String passwd) {
        RealtimeAbsensiInterface realtimeAbsensiInterface = retrofit.create(RealtimeAbsensiInterface.class);
        Call<List<UserLoginResponseModel>> call = realtimeAbsensiInterface.login(nip, passwd);

        call.enqueue(new Callback<List<UserLoginResponseModel>>() {
            @Override
            public void onResponse(Call<List<UserLoginResponseModel>> call, Response<List<UserLoginResponseModel>> response) {

                // check result
                // if ok, then save to shared prefs
                // then navigate to dashboard page

                if (response.code() != 200) {
                    String err = response.message();
                    Toast.makeText(MainActivity.this, "Login fail " + err, Toast.LENGTH_SHORT).show();
                } else {

                    // If result is ok then save to shared preferences
                    String name = response.body().get(0).getNama();
                    String nip = response.body().get(0).getNip();
                    String pangkat = response.body().get(0).getPangkat();

                    Context context = getApplicationContext();

                    Preferences.setNama(context, name);
                    Preferences.setNip(context, nip);
                    Preferences.setPangkat(context, pangkat);

                    Toast.makeText(MainActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                    // close progress dialog
                    progressDialog.dismiss();

                    // Then start another activity
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<List<UserLoginResponseModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}