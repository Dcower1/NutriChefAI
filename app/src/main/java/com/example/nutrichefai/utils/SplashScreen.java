package com.example.nutrichefai.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutrichefai.R;
import com.example.nutrichefai.fragments.log.Log_Usuario;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.SplashScreen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, Log_Usuario.class));

                finish();
            }
        }, 2000);
    }
}