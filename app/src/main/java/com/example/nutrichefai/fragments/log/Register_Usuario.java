package com.example.nutrichefai.fragments.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.nutrichefai.R;

import org.jetbrains.annotations.Nullable;

public class Register_Usuario extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext,btn_atras;
    EditText RegisterUser,RegisterEmail,RegisterPasswordRepeat,RegisterPassword,Register_edad;


    SeekBar seekBarPeso, seekBarAltura;
    TextView textViewPeso, textViewAltura, textViewIMC;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_usuario, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewSwitcher = view.findViewById(R.id.viewSwitcher_register);
        btnNext = view.findViewById(R.id.btn_next);
        btn_atras = view.findViewById(R.id.btn_back);

        // Initialize SeekBar and TextView references
        seekBarPeso = view.findViewById(R.id.seekBarPeso);
        seekBarAltura = view.findViewById(R.id.seekBarAltura);
        textViewPeso = view.findViewById(R.id.textViewPeso);
        textViewAltura = view.findViewById(R.id.textViewAltura);
        textViewIMC = view.findViewById(R.id.textViewIMC);

        // Set up SeekBar for Peso
        seekBarPeso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current value of the SeekBar
                textViewPeso.setText("Peso: " + progress + " kg");
                // Optionally, update the IMC calculation here
                updateIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: do something when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: do something when the user stops touching the SeekBar
            }
        });
        // Set up SeekBar for Altura
        seekBarAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current value of the SeekBar
                textViewAltura.setText("Altura: " + (progress / 100.0) + " m"); // Convert cm to m
                // Optionally, update the IMC calculation here
                updateIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: do something when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: do something when the user stops touching the SeekBar
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewSwitcher.showNext();
            }
        });
        return view;
    }

    private void updateIMC() {
        // Get the current values from the SeekBars
        int peso = seekBarPeso.getProgress(); // Weight in kg
        float altura = seekBarAltura.getProgress() / 100.0f; // Height in m

        // Calculate IMC (BMI)
        if (altura > 0) { // Avoid division by zero
            float imc = peso / (altura * altura);
            textViewIMC.setText(String.format("IMC: %.2f", imc));
        }
    }
}