package com.example.nutrichefai.fragments.log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.DBHelper;
import com.example.nutrichefai.utils.Utilidades;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Register_Usuario extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext,btn_atras,btn_registrarce;
    EditText RegisterUser,RegisterEmail,RegisterPasswordRepeat,RegisterPassword,Register_edad;

    Button bfueza,bsubirpeso,bbajarpeso,bmantenerpeso;

    private String dieta = "";

    SeekBar seekBarPeso, seekBarAltura;
    TextView textViewPeso, textViewAltura, textViewIMC;
    DBHelper dbHelper;;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_usuario, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getContext());

        viewSwitcher = view.findViewById(R.id.viewSwitcher_register);
        btnNext = view.findViewById(R.id.btn_next);
        btn_atras = view.findViewById(R.id.btn_back);
        btn_registrarce = view.findViewById(R.id.btn_registrar);

        //botones de la dieta
        bfueza = view.findViewById(R.id.bt_1);
        bsubirpeso = view.findViewById(R.id.bt_2);
        bbajarpeso = view.findViewById(R.id.bt_3);
        bmantenerpeso = view.findViewById(R.id.bt_4);

        RegisterUser = view.findViewById(R.id.editTextRegisterUser);
        RegisterEmail = view.findViewById(R.id.editTextRegisterEmail);
        RegisterPasswordRepeat = view.findViewById(R.id.editTextRegisterPasswordRepeat);
        RegisterPassword = view.findViewById(R.id.editTextRegisterPassword);
        Register_edad = view.findViewById(R.id.editRegister_edad);


        // Inicializar SeekBar and TextView con los ID
        seekBarPeso = view.findViewById(R.id.seekBarPeso);
        seekBarAltura = view.findViewById(R.id.seekBarAltura);
        textViewPeso = view.findViewById(R.id.textViewPeso);
        textViewAltura = view.findViewById(R.id.textViewAltura);
        textViewIMC = view.findViewById(R.id.textViewIMC);

        // Funcionamiento de los SeekBars
        setupSeekBarListeners();

        btn_registrarce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    // Registro local
                    registrarUsuario();

                    // Registro en el EC2
                    String usuario = RegisterUser.getText().toString().trim();
                    String mail = RegisterEmail.getText().toString().trim();
                    String password = RegisterPassword.getText().toString().trim();
                    String edadStr = Register_edad.getText().toString().trim();
                    int edad = Integer.parseInt(edadStr); // Convertir edad a entero
                    int peso = seekBarPeso.getProgress();
                    float altura = seekBarAltura.getProgress() / 100.0f; // Convertir a metros
                    float imc = peso / (altura * altura); // Calcular IMC

                    // Verificar que la dieta no esté vacía
                    if (!dieta.isEmpty()) {
                        String hashedPassword = Utilidades.hashPassword(password); // Hashear la contraseña
                        limpiarCamposRegistro();

                        // Cambiar al fragmento de inicio de sesión
                        cambiarAFragmentoLogUsuario();
                        // Llamar al método de registro en EC2
                        insertarUsuario(usuario, mail, hashedPassword, edad, peso, altura, imc, dieta);
                    } else {
                        Toast.makeText(getContext(), "Debe seleccionar una dieta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Si todos los campos están llenos y las contraseñas coinciden, pasamos a la siguiente vista
                if (validar()) {

                    viewSwitcher.showNext();
                }


            }
        });

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewSwitcher.showPrevious();
            }
        });
        return view;
    }
    // Método para limpiar los campos de registro
    private void limpiarCamposRegistro() {
        RegisterUser.setText("");
        RegisterEmail.setText("");
        RegisterPassword.setText("");
        Register_edad.setText("");
        seekBarPeso.setProgress(0);
        seekBarAltura.setProgress(0);
        dieta = ""; // Limpiar selección de dieta
    }

    // Método para cambiar al fragmento Log_Usuario
    private void cambiarAFragmentoLogUsuario() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new Usuario_log()); // 'fragment_container' es el id del contenedor de tus fragmentos
        transaction.addToBackStack(null); // Añadir a la pila de retroceso para que el usuario pueda volver si lo desea
        transaction.commit();
    }
    private void setupSeekBarListeners() {


        // funcionamiento del seekbar de peso
        seekBarPeso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewPeso.setText("Peso: " + progress + " kg");
                updateIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // funcionamiento del seekbar de altura
        seekBarAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAltura.setText("Altura: " + (progress / 100.0) + " m");
                updateIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void setupDietaButtons() {
        bfueza.setOnClickListener(v -> selectDietaButton(bfueza, "fuerza"));
        bsubirpeso.setOnClickListener(v -> selectDietaButton(bsubirpeso, "subir_peso"));
        bbajarpeso.setOnClickListener(v -> selectDietaButton(bbajarpeso, "bajar_peso"));
        bmantenerpeso.setOnClickListener(v -> selectDietaButton(bmantenerpeso, "mantener_peso"));
    }

    //accion para marcar los botones
    private void selectDietaButton(Button selectedButton, String dietaValue) {
        dieta = dietaValue; // Asigna la dieta seleccionada
        desmarcarBotonesDieta(); // Desmarcar botones previos
        selectedButton.setSelected(true); // Marcar el botón seleccionado
        selectedButton.setBackgroundColor(getResources().getColor(R.color.colorSelected)); // Resaltar
    }


    private void updateIMC() {
        int peso = seekBarPeso.getProgress();
        float altura = seekBarAltura.getProgress() / 100.0f;

        // calculadora IMC (BMI)
        if (altura > 0) {
            float imc = peso / (altura * altura);
            textViewIMC.setText(String.format("IMC: %.2f", imc));

            // Inicializar dieta según IMC
            inicializarDietaRecomendada(imc);
        }
    }
    private void inicializarDietaRecomendada(float imc) {
        // Limpiar selección previa
        desmarcarBotonesDieta();

        // Seleccionar dieta según IMC
        if (imc < 18.5) {
            // Subir peso
            bsubirpeso.setBackgroundColor(getResources().getColor(R.color.colorSelected)); // Resaltar el botón
            dieta = "subir_peso";
        } else if (imc >= 18.5 && imc < 25) {
            // Mantener peso
            bmantenerpeso.setBackgroundColor(getResources().getColor(R.color.colorSelected));
            dieta = "mantener_peso";
        } else if (imc >= 25 && imc < 30) {
            // Bajar peso
            bbajarpeso.setBackgroundColor(getResources().getColor(R.color.colorSelected));
            dieta = "bajar_peso";
        } else {
            // Fuerza (IMC ≥ 30)
            bfueza.setBackgroundColor(getResources().getColor(R.color.colorSelected));
            dieta = "fuerza";
        }
    }
    private void desmarcarBotonesDieta() {
        // Reiniciar el fondo de los botones para que no se vean seleccionados
        bfueza.setBackgroundColor(getResources().getColor(R.color.colorDefault));
        bsubirpeso.setBackgroundColor(getResources().getColor(R.color.colorDefault));
        bbajarpeso.setBackgroundColor(getResources().getColor(R.color.colorDefault));
        bmantenerpeso.setBackgroundColor(getResources().getColor(R.color.colorDefault));
    }

    private void registrarUsuario() {
        String usuario = RegisterUser.getText().toString().trim();
        String mail = RegisterEmail.getText().toString().trim();
        String password = RegisterPassword.getText().toString().trim();
        String edad = Register_edad.getText().toString().trim();
        int peso = seekBarPeso.getProgress();
        float altura = seekBarAltura.getProgress() / 100.0f; // Convertir altura a metros
        float imc = peso / (altura * altura); // Cálculo de IMC

        // Validar si se ha seleccionado una dieta
        if (dieta.isEmpty()) {
            Toast.makeText(getContext(), "Debe seleccionar una dieta", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = Utilidades.hashPassword(password); // Hashear contraseña
        try {
            long id = dbHelper.insertarUsuario(usuario, mail, hashedPassword, edad, String.valueOf(peso), String.valueOf(altura), String.valueOf(imc), dieta);
            // Verificar si la inserción fue exitosa
            if (id > 0) {
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //esto es boolean porque me deberia retornar un true o un false
    private boolean validar() {
        String usuario = RegisterUser.getText().toString().trim();
        String email = RegisterEmail.getText().toString().trim();
        String password = RegisterPassword.getText().toString().trim();
        String passwordRepeat = RegisterPasswordRepeat.getText().toString().trim();
        String edad = Register_edad.getText().toString().trim();

        boolean isValid = true; // Assume all inputs are valid initially

        if (usuario.isEmpty()) {
            RegisterUser.setError("El campo de usuario está vacío");
            isValid = false; // Set valid to false
        }

        if (email.isEmpty()) {
            RegisterEmail.setError("El campo de email está vacío");
            isValid = false; // Set valid to false
        }

        if (password.isEmpty()) {
            RegisterPassword.setError("El campo de contraseña está vacío");
            isValid = false; // Set valid to false
        }

        if (passwordRepeat.isEmpty()) {
            RegisterPasswordRepeat.setError("Debe repetir la contraseña");
            isValid = false; // Set valid to false
        }

        if (edad.isEmpty()) {
            Register_edad.setError("El campo de edad está vacío");
            isValid = false; // Set valid to false
        }

        // Validar si las contraseñas coinciden
        if (!password.equals(passwordRepeat)) {
            RegisterPassword.setError("Las contraseñas no coinciden");
            RegisterPasswordRepeat.setError("Las contraseñas no coinciden");
            isValid = false; // Set valid to false
        }

        return isValid; // Return the result of the validation
    }

    // Método para insertar un usuario
    private void insertarUsuario(String usuario, String mail, String password, int edad, int peso, float altura, float imc, String dieta) {
        String url = "http://44.215.236.242/NutriChefAI/insert_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Maneja la respuesta del servidor
                        Log.d("Respuesta", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Maneja los errores
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("mail", mail);
                params.put("password", password);
                params.put("edad", String.valueOf(edad));
                params.put("peso", String.valueOf(peso));
                params.put("altura", String.valueOf(altura));
                params.put("imc", String.valueOf(imc));
                params.put("dieta", dieta);
                return params;
            }
        };

        // Agrega la petición a la cola de Volley usando getContext()
        RequestQueue queue = Volley.newRequestQueue(getContext()); // Usar getContext() aquí
        queue.add(stringRequest);
    }




}