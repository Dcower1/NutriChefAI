package com.example.nutrichefai.fragments.log;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutrichefai.MainActivity;
import com.example.nutrichefai.R;

public class Log_Usuario extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private Button bt_ingresar;
    private ViewSwitcher viewSwitcher;

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

        // Acción del botón "Ingresar"
        bt_ingresar.setOnClickListener(v -> {
            Intent intent = new Intent(Log_Usuario.this, MainActivity.class);
            startActivity(intent);
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
            // Puedes ajustar el layout aquí si necesitas hacer cambios específicos para tabletas
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
}