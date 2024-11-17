package com.example.nutrichefai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.fragments.chat.Chat_menu;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;
import com.example.nutrichefai.fragments.perfiles.Perfil_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Botones de la navegación de la app
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    // Para inicializar los fragmentos
    FrameLayout frameLayout;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recibe el userId desde el Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        if (userId != -1) {
            saveUserId(userId); // Guardar el userId en SharedPreferences
            Log.d("MainActivity", "userId establecido: " + userId);
        } else {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_LONG).show();
        }

        // Configurar y cargar el primer fragmento
        if (savedInstanceState == null) {
            loadFragment(new Chat_menu(), userId, getResources().getConfiguration().screenWidthDp >= 600);
        }

        // Configuración del BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;

            if (item.getItemId() == R.id.chat_nav) {
                fragment = new Chat_menu();
            } else if (item.getItemId() == R.id.add_nav) {
                fragment = new Freezer_inv();
            } else if (item.getItemId() == R.id.perfil_nav) {
                fragment = new Perfil_Usuario();
            } else {
                return false;
            }

            return loadFragment(fragment, userId, getResources().getConfiguration().screenWidthDp >= 600);
        });
    }

    // Método para detectar si el dispositivo es una tableta
    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void saveUserId(int idUsuario) {
        SharedPreferences preferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("idUsuario", idUsuario);
        editor.apply();
    }

    private void loadUserData(int userId) {
        String url = "http://98.82.247.63/NutriChefAi/cargar_usuario.php?id_usu=" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String estado = jsonResponse.getString("estado");

                        if (estado.equals("1")) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            int idUsuario = data.getInt("id_usu");

                            // Guarda el ID del usuario en SharedPreferences
                            saveUserId(idUsuario);
                        } else {
                            Toast.makeText(this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(this, "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean loadFragment(Fragment fragment, int userId) {
        // Asume que no es una pantalla grande (valor por defecto)
        return loadFragment(fragment, userId, false);
    }
    // Modificar el método loadFragment para aceptar el tercer parámetro
    private boolean loadFragment(Fragment fragment, int userId, boolean isLargeScreen) {
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putInt("userId", userId);
            args.putBoolean("isLargeScreen", isLargeScreen); // Pasar el tamaño de pantalla
            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
