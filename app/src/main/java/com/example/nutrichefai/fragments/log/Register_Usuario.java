package com.example.nutrichefai.fragments.log;

import static com.example.nutrichefai.utils.Utilidades.isValidPassword;

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


        //botones de la dieta
        bfueza = view.findViewById(R.id.bt_1);
        bsubirpeso = view.findViewById(R.id.bt_2);
        bbajarpeso = view.findViewById(R.id.bt_3);
        bmantenerpeso = view.findViewById(R.id.bt_4);

        RegisterUser = view.findViewById(R.id.editTextRegisterUser);
        RegisterEmail = view.findViewById(R.id.editTextRegisterEmail);
        RegisterPasswordRepeat = view.findViewById(R.id.editTextRegisterPasswordRepeat);
        RegisterPassword = view.findViewById(R.id.editTextRegisterPassword);
        editBirthday = view.findViewById(R.id.editRegisterBirthday);

        // Inicializar los toggles de visibilidad
        passwordToggle = view.findViewById(R.id.imageViewPasswordToggle);
        passwordRepeatToggle = view.findViewById(R.id.imageViewPasswordRepeatToggle);
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
                if (validar()) {
                    String usuario = RegisterUser.getText().toString().trim();
                    String email = RegisterEmail.getText().toString().trim();
                    String password = RegisterPassword.getText().toString().trim();
                    String birthday = editBirthday.getText().toString().trim();
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    int peso = seekBarPeso.getProgress();
                    float altura = seekBarAltura.getProgress() / 100.0f;
                    float imc = peso / (altura * altura);

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
                            sexo = 1;
                    }

                    if (!dieta.isEmpty()) {
                        String hashedPassword = Utilidades.hashPassword(password);
                        limpiarCamposRegistro(view);

                        try {
                            Date date = inputFormat.parse(birthday);
                            String formattedDate = outputFormat.format(date);

                            cambiarAFragmentoLogUsuario();
                            insertarUsuario(usuario, email, hashedPassword, formattedDate, peso, sexo, altura, imc);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Formato de fecha no válido. Por favor, use dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                        }
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
        dieta = ""; // Limpiar selección de dieta

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

    //esto es boolean porque me deberia retornar un true o un false
    private boolean validar() {
        String usuario = RegisterUser.getText().toString().trim();
        String email = RegisterEmail.getText().toString().trim();
        String password = RegisterPassword.getText().toString().trim();
        String passwordRepeat = RegisterPasswordRepeat.getText().toString().trim();
        String birthday = editBirthday.getText().toString().trim();

        boolean isValid = true;

        if (usuario.isEmpty()) {
            RegisterUser.setError("El campo de usuario está vacío");
            isValid = false; // Set valid to false
        } else if (usuario.length() < 4) {
            RegisterUser.setError("El usuario debe tener al menos 4 letras");
            isValid = false;
        } else if (!usuario.matches("[a-zA-Z0-9]+")) {
            RegisterUser.setError("El usuario solo puede contener letras y números");
            isValid = false;
        }

        if (email.isEmpty()) {
            RegisterEmail.setError("El campo de email está vacío");
            isValid = false;
        } else if (!Utilidades.isValidEmail(email)) {
            RegisterEmail.setError("El formato del email no es válido");
            isValid = false;
        }
        if (password.isEmpty()) {
            RegisterPassword.setError("El campo de contraseña está vacío");
            isValid = false;
        } else if (!isValidPassword(password)) {
            RegisterPassword.setError("La contraseña debe tener al menos 6 caracteres y 1 mayúscula");
            isValid = false;
        }

        if (passwordRepeat.isEmpty()) {
            RegisterPasswordRepeat.setError("Debe repetir la contraseña");
            isValid = false; // Set valid to false
        }

        if (birthday.isEmpty()) {
            editBirthday.setError("El campo de fecha de nacimiento está vacío");
            isValid = false;
        } else {
            // Validar la edad con la fecha de nacimiento
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date birthDate = dateFormat.parse(birthday);
                Calendar birthCalendar = Calendar.getInstance();
                birthCalendar.setTime(birthDate);

                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

                // Si el cumpleaños aún no ha ocurrido este año, restar 1 a la edad
                if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                if (age < 16 || age > 130) {
                    editBirthday.setError("Debes tener entre 16 y 130 años");
                    isValid = false;
                }
            } catch (ParseException e) {
                editBirthday.setError("Formato de fecha no válido");
                isValid = false;
            }
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