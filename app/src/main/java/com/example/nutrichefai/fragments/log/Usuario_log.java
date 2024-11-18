package com.example.nutrichefai.fragments.log;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.MainActivity;
import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.DBHelper;
import com.example.nutrichefai.utils.Utilidades;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Usuario_log extends Fragment {

    private Button bt_ingresar;
    private EditText txtloginusu, txtpassword;
    private RequestQueue datos;

    private ImageView passwordToggle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_log, container, false);

        // Inicializa las vistas
        txtloginusu = view.findViewById(R.id.editTextLoginUsername);
        txtpassword = view.findViewById(R.id.editTextLoginPassword);
        bt_ingresar = view.findViewById(R.id.btn_ingresar);
        passwordToggle = view.findViewById(R.id.passwordToggle);

        datos = Volley.newRequestQueue(requireContext());


        bt_ingresar.setOnClickListener(v -> {
            String usernameOrEmail = txtloginusu.getText().toString().trim();
            String password = txtpassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = Utilidades.hashPassword(password);

            // Valida al usuario en la nube
            String url = "http://98.82.247.63/NutriChefAi/consultar_usuario.php?login=" + usernameOrEmail + "&password=" + hashedPassword;
            validarUsuario(url);
        });


        //funcion https://developer.android.com/develop/ui/views/components/pickers?hl=es-419
        passwordToggle.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    togglePasswordVisibility(txtpassword, passwordToggle, true);
                    return true;
                case MotionEvent.ACTION_UP:
                    togglePasswordVisibility(txtpassword, passwordToggle, false);
                    return true;
            }
            return false;
        });
        return view;
    }

    private void validarUsuario(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        // Log de la respuesta completa
                        Log.d("Response", response);

                        JSONObject jsonResponse = new JSONObject(response);
                        String estado = jsonResponse.getString("estado");

                        if (estado.equals("1")) { // Login exitoso
                            // Obtener el ID del usuario desde la respuesta
                            int userId = jsonResponse.getJSONObject("data").getInt("id_usu");

                            // Redirigir a MainActivity pasando el userId
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            intent.putExtra("userId", userId); // Pasar el userId a MainActivity
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            Toast.makeText(requireContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Log del error de red
                    Log.e("VolleyError", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("NetworkResponse", new String(error.networkResponse.data));
                    }
                    Toast.makeText(requireContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("login", txtloginusu.getText().toString().trim());
                parametros.put("password", Utilidades.hashPassword(txtpassword.getText().toString().trim()));
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
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
}