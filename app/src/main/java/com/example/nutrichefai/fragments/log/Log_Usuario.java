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
import androidx.fragment.app.FragmentManager;
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
        EdgeToEdge.enable(this);  // este es tu método para hacer la app a pantalla completa asi no se bugea en los demas dispositivos.
        setContentView(R.layout.activity_log);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        datos = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(v -> {


            showFragment(new Usuario_log());
        });

        btnRegister.setOnClickListener(v -> {


            showFragment(new Register_Usuario());
        });

        if (savedInstanceState == null) {
            showFragment(new Usuario_log());
        }
    }

    // Metodo para activar la animacion de los botones
    private void moveButton(final Button button, boolean moveUp) {
        float targetY = moveUp ? -50f : 0f;

        button.animate()
                .translationY(targetY)
                .setDuration(300)
                .start();
    }

    // Metodo para mostrar el fragmento
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplaza el fragmento actual
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        if (fragment instanceof Usuario_log) {

            moveButton(btnLogin, false);
            moveButton(btnRegister, true);
        } else if (fragment instanceof Register_Usuario) {

            moveButton(btnRegister, false);
            moveButton(btnLogin, true);
        }
    }


    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}