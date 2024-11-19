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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.nutrichefai.fragments.chat.Chat_menu;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;
import com.example.nutrichefai.fragments.perfiles.Perfil_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        EdgeToEdge.enable(this); // este es tu método para hacer la app a pantalla completa asi no se bugea en los demas dispositivos.
        setContentView(R.layout.activity_main);

        // Ajustar los márgenes de los system bars (barra de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a la barra de navegación y fragment container
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.fragment_container);
        toolbar = findViewById(R.id.toolbar);

        // Obtener el ancho de la pantalla en dp
        int screenWidthDp = getResources().getConfiguration().screenWidthDp;

        // Obtener el userId desde el Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        // Guardar el userId en SharedPreferences
        if (userId != -1) {
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", userId);
            editor.apply();
            Log.d("MainActivity", "userId guardado: " + userId);
        } else {
            Log.e("MainActivity", "Error: Usuario no identificado.");
            Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
        }
        // Cargar el fragmento Chat_menu con el userId si es la primera vez
        if (savedInstanceState == null) {
            if (userId != -1) {
                loadFragment(new Chat_menu(), userId, screenWidthDp >= 600);
            } else {
                Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_LONG).show();
            }
        }


        // Configurar la toolbar como ActionBar
        setSupportActionBar(toolbar);
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

            // Llamar al método loadFragment con la condición del tamaño de pantalla
            return loadFragment(fragment, userId, screenWidthDp >= 600); // Pasar la condición del tamaño
        });
    }

    // Método para detectar si el dispositivo es una tableta
    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Modificar el método loadFragment para aceptar el tercer parámetro
    private boolean loadFragment(Fragment fragment, int userId, boolean isLargeScreen) {
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putInt("userId", userId);
            args.putBoolean("isLargeScreen", isLargeScreen);
            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Verifica si el fragment actual necesita manejar el botón de retroceso
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila de retroceso, llama al comportamiento predeterminado
            super.onBackPressed();
        } else {
            // Si no hay más fragmentos, muestra el diálogo de confirmación
            new AlertDialog.Builder(this)
                    .setTitle("Salir de la aplicación")
                    .setMessage("¿Estás seguro de que deseas salir?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Finaliza la actividad y cierra la aplicación
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Cierra el diálogo sin hacer nada
                        dialog.dismiss();
                    })
                    .setCancelable(false) // Impide cerrar el diálogo tocando fuera de él
                    .show();
        }
    }



}
