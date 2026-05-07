package com.example.iot_lab4_20216352;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v -> {
            if (!isNetworkAvailable()) {
                showNoInternetDialog();
            } else {
                startActivity(new Intent(this, AppActivity.class));
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities caps = cm.getNetworkCapabilities(network);
            return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }

    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_internet_title)
                .setMessage(R.string.no_internet_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.configuracion, (dialog, which) ->
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }
}
