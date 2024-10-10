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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private Button bt_ingresar;
    private ViewSwitcher viewSwitcher;

    EditText txtloginusu, txtpassword;
    RequestQueue datos;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Si usas este método para pantalla completa
        setContentView(R.layout.activity_log);

        // Ajustar los márgenes de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar botones y ViewSwitcher
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        viewSwitcher = findViewById(R.id.viewSwitcher);

        bt_ingresar = findViewById(R.id.btn_ingresar);
        txtloginusu = findViewById(R.id.editTextLoginUsername);
        txtpassword = findViewById(R.id.editTextLoginPassword);
        datos = Volley.newRequestQueue(this);




        bt_ingresar.setOnClickListener(v -> {
            String usernameOrEmail = txtloginusu.getText().toString().trim();
            String password = txtpassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(Log_Usuario.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            consultardatos(usernameOrEmail, password);
        });



        // Alternar entre las vistas de login y registro
        btnLogin.setOnClickListener(v -> {
            moveButton(btnLogin, false);
            moveButton(btnRegister, true);
            viewSwitcher.setDisplayedChild(0); // Mostrar layout de login
        });

        btnRegister.setOnClickListener(v -> {
            moveButton(btnRegister, false);
            moveButton(btnLogin, true);
            viewSwitcher.setDisplayedChild(1); // Mostrar layout de registro
        });

        // Detectar si es una tableta y realizar ajustes si es necesario
        if (esTablet(this)) {
            Toast.makeText(this, "Este es un dispositivo tipo tableta", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Este es un dispositivo tipo teléfono", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para animar el botón al hacer clic
    private void moveButton(final Button button, boolean moveUp) {
        float targetY = moveUp ? -50f : 0f;

        button.animate()
                .translationY(targetY)
                .setDuration(300)
                .start();
    }

    // Método para detectar si el dispositivo es una tableta
    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Método para verificar si hay conexión a Internet
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void consultardatos(String loginInput, String pass) {
        String url = "http://44.215.236.242/NutriChefAI/consultar_usuario.php?login=" + loginInput + "&password=" + pass; // Updated URL
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String estado = response.getString("estado");
                    if (estado.equals("0")) {
                        Toast.makeText(Log_Usuario.this, "Usuario no Existe", Toast.LENGTH_LONG).show();
                    } else {
                        Intent ventana = new Intent(Log_Usuario.this, MainActivity.class);
                        startActivity(ventana);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        datos.add(request);
    }



}