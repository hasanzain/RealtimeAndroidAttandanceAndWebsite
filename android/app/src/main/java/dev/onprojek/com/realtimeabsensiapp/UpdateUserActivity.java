package dev.onprojek.com.realtimeabsensiapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import dev.onprojek.com.realtimeabsensiapp.models.ResponseUbahPassword;
import dev.onprojek.com.realtimeabsensiapp.services.RealtimeAbsensiInterface;
import dev.onprojek.com.realtimeabsensiapp.services.RetrofitApiClient;
import dev.onprojek.com.realtimeabsensiapp.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateUserActivity extends AppCompatActivity {

    private EditText mEditTextPassword, mEditTextKonfirmasiPassword;
    private MaterialButton mUpdateButton;

    private Retrofit retrofit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        initialComponents();
    }

    private void initialComponents() {
        mEditTextKonfirmasiPassword = (EditText) findViewById(R.id.passwordConfirmText);
        mEditTextPassword = (EditText) findViewById(R.id.passwordText);

        mUpdateButton = (MaterialButton) findViewById(R.id.updateBtn);
        mUpdateButton.setOnClickListener(updateBtnListener);

        retrofit = RetrofitApiClient.getRetrofitClientInstance();
        progressDialog = new ProgressDialog(UpdateUserActivity.this);

    }

    private final View.OnClickListener updateBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Update proses here

            progressDialog.setMessage("Loading ...");
            progressDialog.setTitle("Update password");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            String password = mEditTextPassword.getText().toString();
            String confirmPassword = mEditTextKonfirmasiPassword.getText().toString();

            if (password.isEmpty()) {
                // Error password kosong
                Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else if (confirmPassword.isEmpty()) {
                // Error konfirmasi password kosong
                Toast.makeText(getApplicationContext(), "Konfirmasi password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                // Check if password and confirm password are same
                if (isPasswordSame(password, confirmPassword)) {
                    // Proses update password
                    String nip = Preferences.getNip(getApplicationContext());
                    updatePassword(nip, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Password tidak sama", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }
    };

    private boolean isPasswordSame(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    };

    private void updatePassword(String nip, String password) {
        RealtimeAbsensiInterface realtimeAbsensiInterface = retrofit.create(RealtimeAbsensiInterface.class);

         Call<ResponseUbahPassword> call = realtimeAbsensiInterface.updatePassword(nip, password);
         call.enqueue(new Callback<ResponseUbahPassword>() {
             @Override
             public void onResponse(Call<ResponseUbahPassword> call, Response<ResponseUbahPassword> response) {
                if (response.code() == 200) {
                    // show update success
                    Toast.makeText(getApplicationContext(), "Update password berhasil", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    finish();
//                    Intent intent = new Intent(UpdateUserActivity.this, DashboardActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                } else {
                    // show update fail
                    Toast.makeText(getApplicationContext(), "Update password gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
             }

             @Override
             public void onFailure(Call<ResponseUbahPassword> call, Throwable t) {
                 Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
             }
         });
    }
}