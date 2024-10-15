package com.example.nutrichefai;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Si este es tu método para hacer la app a pantalla completa
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



        // Configurar la toolbar como ActionBar
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            loadFragment(new Chat_menu());
        }
        // Manejar la selección de los elementos del BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.chat_nav) {
                return loadFragment(new Chat_menu());
            } else if (item.getItemId() == R.id.add_nav) {
                return loadFragment(new Freezer_inv());
            } else if (item.getItemId() == R.id.perfil_nav) {
                return loadFragment(new Perfil_Usuario());
            }
            return false;
        });
    }

    // Método para detectar si el dispositivo es una tableta
    public boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Método para cargar los fragmentos
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
