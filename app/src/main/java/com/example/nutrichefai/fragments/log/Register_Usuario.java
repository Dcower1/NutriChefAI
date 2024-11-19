package com.example.nutrichefai.fragments.log;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register_Usuario extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext, btn_atras, btn_registrarce;
    EditText RegisterUser, RegisterEmail, RegisterPasswordRepeat, RegisterPassword;

    Button bfueza, bsubirpeso, bbajarpeso, bmantenerpeso;

    private String dieta = "";

    SeekBar seekBarPeso, seekBarAltura;
    TextView textViewPeso, textViewAltura, textViewIMC;
    DBHelper dbHelper;
    ;
    EditText editBirthday;
    private boolean isPasswordVisible = false;
    private boolean isPasswordRepeatVisible = false;
    private ImageView passwordToggle;
    private ImageView passwordRepeatToggle;

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

        Spinner spinnerGender = view.findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);


        RegisterUser = view.findViewById(R.id.editTextRegisterUser);
        RegisterEmail = view.findViewById(R.id.editTextRegisterEmail);
        RegisterPasswordRepeat = view.findViewById(R.id.editTextRegisterPasswordRepeat);
        RegisterPassword = view.findViewById(R.id.editTextRegisterPassword);
        editBirthday = view.findViewById(R.id.editRegisterBirthday);

        // Inicializar los toggles de visibilidad
        passwordToggle = view.findViewById(R.id.passwordToggle);
        passwordRepeatToggle = view.findViewById(R.id.reppasswordToggle);
        // Inicializar SeekBar and TextView con los ID
        seekBarPeso = view.findViewById(R.id.seekBarPeso);
        seekBarAltura = view.findViewById(R.id.seekBarAltura);
        textViewPeso = view.findViewById(R.id.textViewPeso);
        textViewAltura = view.findViewById(R.id.textViewAltura);
        textViewIMC = view.findViewById(R.id.textViewIMC);

        passwordToggle.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    togglePasswordVisibility(RegisterPassword, passwordToggle, true);
                    v.performClick(); // Asegurarte de que el clic se maneje correctamente
                    return true; // Evento manejado

                case MotionEvent.ACTION_UP:
                    togglePasswordVisibility(RegisterPassword, passwordToggle, false);
                    return true;
            }
            return false;
        });


        passwordRepeatToggle.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    togglePasswordVisibility(RegisterPasswordRepeat, passwordRepeatToggle, true);
                    return true; // Evento manejado

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    togglePasswordVisibility(RegisterPasswordRepeat, passwordRepeatToggle, false);
                    return true; // Evento manejado
            }
            return false; // Evento no manejado
        });
        // Funcionamiento de los SeekBars
        setupSeekBarListeners();
        // Configurar el campo de fecha de cumpleaños
        editBirthday.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Mostrar el DatePickerDialog para seleccionar la fecha de cumpleaños
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, year1, month1, dayOfMonth) -> {
                        // Actualizar el EditText con la fecha seleccionada
                        editBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
        btn_registrarce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePage1() && validatePage2()) { // Validar ambas páginas
                    try {
                        // Obtener valores del formulario
                        String usuario = RegisterUser.getText().toString().trim();
                        String email = RegisterEmail.getText().toString().trim();
                        String password = RegisterPassword.getText().toString().trim();
                        String birthday = editBirthday.getText().toString().trim();
                        int peso = seekBarPeso.getProgress();
                        float altura = seekBarAltura.getProgress() / 100.0f;
                        float imc = peso / (altura * altura);

                        // Obtener el género seleccionado desde el Spinner
                        Spinner spinnerGender = view.findViewById(R.id.spinner_gender);
                        int sexo;
                        switch (spinnerGender.getSelectedItemPosition()) {
                            case 0:
                                sexo = 1; // Hombre
                                break;
                            case 1:
                                sexo = 2; // Mujer
                                break;
                            case 2:
                                sexo = 3; // Otro
                                break;
                            default:
                                sexo = 1; // Valor por defecto (Hombre)
                        }

                        // Validar y formatear la fecha de nacimiento
                        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = inputFormat.parse(birthday);
                        String formattedDate = outputFormat.format(date);

                        // Hashear la contraseña
                        String hashedPassword = Utilidades.hashPassword(password);

                        // Enviar datos al servidor o base de datos
                        insertarUsuario(usuario, email, hashedPassword, formattedDate, peso, sexo, altura, imc);

                        // Limpiar campos y cambiar de fragmento
                        limpiarCamposRegistro(v);
                        cambiarAFragmentoLogUsuario();

                        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        // Manejar errores de formato de fecha
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Formato de fecha no válido. Use dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Manejar cualquier otra excepción
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ocurrió un error al registrar. Inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostrar mensaje si hay errores en los campos
                    Toast.makeText(getContext(), "Por favor, corrija los errores en los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePage1()) {
                    // Obtener valores del formulario
                    String usuario = RegisterUser.getText().toString().trim();
                    String email = RegisterEmail.getText().toString().trim();

                    // Validar si el usuario y el email ya existen
                    checkUniqueFields(usuario, email, isUnique -> {
                        if (isUnique) {
                            // Si no hay duplicados, avanzar al siguiente layout
                            viewSwitcher.showNext();
                        } else {
                            // Mostrar mensaje de error si el usuario o el email ya están registrados
                            Toast.makeText(getContext(), "Usuario o correo ya registrados", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Mostrar mensaje si hay errores de validación en los campos
                    Toast.makeText(getContext(), "Por favor, corrija los errores en los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePage2()) {
                    viewSwitcher.showNext();
                } else {
                    Toast.makeText(getContext(), "Por favor, corrija los errores en los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    private void togglePasswordVisibility(EditText editText, ImageView toggle, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT); // Mostrar texto
            toggle.setImageResource(R.drawable.baseline_visibility_24); // Cambiar a ícono de "ver"
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // Ocultar texto
            toggle.setImageResource(R.drawable.baseline_visibility_off_24); // Cambiar a ícono de "no ver"
        }
        editText.setSelection(editText.getText().length()); // Colocar el cursor al final
    }

    // Método para limpiar los campos de registro
    private void limpiarCamposRegistro(View view) {
        RegisterUser.setText("");
        RegisterEmail.setText("");
        RegisterPassword.setText("");
        editBirthday.setText("");
        seekBarPeso.setProgress(0);
        seekBarAltura.setProgress(0);

        // Restablecer el Spinner de género a la primera opción
        Spinner spinnerGender = view.findViewById(R.id.spinner_gender);
        if (spinnerGender != null) {
            spinnerGender.setSelection(0);
        }
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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // funcionamiento del seekbar de altura
        seekBarAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAltura.setText("Altura: " + (progress / 100.0) + " m");
                updateIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    interface UniqueFieldCallback {
        void onResult(boolean isUnique);
    }
    private void checkUniqueFields(String usuario, String email, UniqueFieldCallback callback) {
        String apiUrl = "http://98.82.247.63/NutriChefAi/check_user_email.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean userExists = jsonResponse.getBoolean("userExists");
                        boolean emailExists = jsonResponse.getBoolean("emailExists");

                        // Establecer errores en los campos si los datos ya existen
                        if (userExists) {
                            RegisterUser.setError("Este usuario ya está registrado");
                        }
                        if (emailExists) {
                            RegisterEmail.setError("Este email ya está registrado");
                        }

                        // Retornar si ambos campos son únicos
                        callback.onResult(!userExists && !emailExists);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onResult(false); // Error al procesar JSON
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Error en la conexión: " + error.getMessage());
                    callback.onResult(false); // Error de red o servidor
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private boolean validatePage1() {
        boolean isValid = true;

        // Validar usuario
        String usuario = RegisterUser.getText().toString().trim();
        if (usuario.isEmpty()) {
            RegisterUser.setError("El campo de usuario está vacío");
            isValid = false;
        } else if (usuario.length() < 4) {
            RegisterUser.setError("El usuario debe tener al menos 4 caracteres");
            isValid = false;
        } else if (!usuario.matches("[a-zA-Z0-9\\s\\-áéíóúÁÉÍÓÚñÑ]+")) {
            RegisterUser.setError("El usuario solo puede contener letras, números, espacios o guiones");
            isValid = false;
        }

        // Validar email
        String email = RegisterEmail.getText().toString().trim();
        if (email.isEmpty()) {
            RegisterEmail.setError("El campo de email está vacío");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            RegisterEmail.setError("El formato del email no es válido");
            isValid = false;
        }

        // Validar contraseña
        String password = RegisterPassword.getText().toString().trim();
        String passwordRepeat = RegisterPasswordRepeat.getText().toString().trim();
        if (password.isEmpty()) {
            RegisterPassword.setError("El campo de contraseña está vacío");
            isValid = false;
        } else if (password.length() < 6) {
            RegisterPassword.setError("La contraseña debe tener al menos 6 caracteres");
            isValid = false;
        } else if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) {
            RegisterPassword.setError("La contraseña debe incluir al menos una mayúscula, una minúscula y un número");
            isValid = false;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(passwordRepeat)) {
            RegisterPasswordRepeat.setError("Las contraseñas no coinciden");
            isValid = false;
        }

        return isValid;
    }

    private boolean validatePage2() {
        boolean isValid = true;

        // Validar fecha de nacimiento
        String birthday = editBirthday.getText().toString().trim();
        if (birthday.isEmpty()) {
            editBirthday.setError("Debe seleccionar su fecha de nacimiento");
            isValid = false;
        } else {
            // Validar formato y rango de edad
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date birthDate = dateFormat.parse(birthday);
                Calendar birthCalendar = Calendar.getInstance();
                birthCalendar.setTime(birthDate);

                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                if (age < 16 || age > 130) {
                    editBirthday.setError("Debe tener entre 16 y 130 años");
                    isValid = false;
                }
            } catch (ParseException e) {
                editBirthday.setError("Formato de fecha no válido. Use dd/MM/yyyy");
                isValid = false;
            }
        }

        return isValid;
    }

    private void updateIMC() {
        int peso = seekBarPeso.getProgress();
        float altura = seekBarAltura.getProgress() / 100.0f;

        // calculadora IMC (BMI)
        if (altura > 0) {
            float imc = peso / (altura * altura);
            textViewIMC.setText(String.format("IMC: %.2f", imc));


        }
    }




    // Método para insertar un usuario
    private void insertarUsuario(String usuario, String email, String password, String birthday, int peso, int sexo, float altura, float imc) {
        String url = "http://98.82.247.63/NutriChefAi/inser_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        Log.d("Respuesta del servidor", response);
                        Toast.makeText(getContext(), "Respuesta: " + response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                Toast.makeText(getContext(), "Error en la solicitud: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("email", email);
                params.put("password", password);
                params.put("fechaNac", birthday);
                params.put("peso", String.valueOf(peso));
                params.put("altura", String.valueOf(altura));
                params.put("imc", String.valueOf(imc));
                params.put("sexo", String.valueOf(sexo));
                return params;
            }
        };

        // Agrega la petición a la cola de Volley usando getContext()
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

}