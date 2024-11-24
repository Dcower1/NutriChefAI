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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nutrichefai.databinding.ActivityMainBinding;
import com.example.nutrichefai.fragments.chat.Chat_menu;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;
import com.example.nutrichefai.fragments.perfiles.Perfil_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hacer la app pantalla completa
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar vistas
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configurar Navigation Component
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.chat_menu, R.id.freezer_inv, R.id.perfil_usuario
        ).build();


        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Eliminar backstack al cambiar de sección en BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            navController.popBackStack(R.id.freezer_inv, false); // Borra Inventario_usuario al cambiar
            return handled;
        });

        // Obtener el userId del Intent
        userId = getIntent().getIntExtra("userId", -1);

        // Guardar el userId en SharedPreferences
        if (userId != -1) {
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            preferences.edit().putInt("userId", userId).apply();
            Log.d("MainActivity", "User ID guardado en SharedPreferences: " + userId);
        } else {
            Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error: Usuario no identificado.");
        }

        // Pasar el userId al gráfico de navegación
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        navController.setGraph(navController.getGraph(), args);
    }

    @Override
    public void onBackPressed() {
        // Si hay fragmentos en la pila de retroceso, manejarlo normalmente
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            // Mostrar un diálogo para confirmar salir
            new AlertDialog.Builder(this)
                    .setTitle("Salir de la aplicación")
                    .setMessage("¿Estás seguro de que deseas salir?")
                    .setPositiveButton("Sí", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
        }
    }
}