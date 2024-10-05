package com.example.nutrichefai;

import android.os.Bundle;
import android.widget.FrameLayout;

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

    //Botones del navegacion de la APP;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    //Para inicializar los fragmentos;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.fragment_container);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

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

