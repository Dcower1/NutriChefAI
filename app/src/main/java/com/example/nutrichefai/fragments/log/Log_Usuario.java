package com.example.nutrichefai.fragments.log;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.MainActivity;
import com.example.nutrichefai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Log_Usuario extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private RequestQueue datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // If you are using this method for full screen
        setContentView(R.layout.activity_log);

        // Adjust margins for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        datos = Volley.newRequestQueue(this);

        // Set up button click listeners
        btnLogin.setOnClickListener(v -> {
            moveButton(btnLogin, false);
            moveButton(btnRegister, true);
            // Show the Usuario_log fragment
            showFragment(new Usuario_log());
        });

        btnRegister.setOnClickListener(v -> {
            moveButton(btnRegister, false);
            moveButton(btnLogin, true);
            // Show the Register_Usuario fragment
            showFragment(new Register_Usuario());
        });


        if (savedInstanceState == null) {
            showFragment(new Usuario_log());
        }
    }

    // Method to animate button on click
    private void moveButton(final Button button, boolean moveUp) {
        float targetY = moveUp ? -50f : 0f;

        button.animate()
                .translationY(targetY)
                .setDuration(300)
                .start();
    }

    // Method to show a fragment
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Use the ID of your container
        transaction.addToBackStack(null); // Add to back stack to enable navigation
        transaction.commit();
    }

    // Method to check if the device is a tablet
    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Method to check internet availability
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}