package com.example.nutrichefai.fragments.log;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutrichefai.R;

public class Log_Usuario extends AppCompatActivity {
    private Button btnLogin, btnRegister;

    private ViewSwitcher viewSwitcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        viewSwitcher = findViewById(R.id.viewSwitcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButton(btnLogin, false);
                moveButton(btnRegister, true);
                viewSwitcher.setDisplayedChild(0); // Show login layout
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButton(btnRegister, false);
                moveButton(btnLogin, true);
                viewSwitcher.setDisplayedChild(1); // Show register layout
            }
        });

    }

    private void moveButton(final Button button, boolean moveUp) {
        float targetY = moveUp ? -50f : 0f;

        button.animate()
                .translationY(targetY)
                .setDuration(300)
                .start();
    }


}