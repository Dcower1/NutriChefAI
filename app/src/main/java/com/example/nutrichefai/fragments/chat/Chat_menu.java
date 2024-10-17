package com.example.nutrichefai.fragments.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Chat_menu extends Fragment {

    TextView nombreusuario, id;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_menu, container, false);

        // Configuración del padding según los insets del sistema (barra de estado, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombreusuario = view.findViewById(R.id.nomUsuario);
        id = view.findViewById(R.id.id_usuario);

        // Recibir el userId desde los argumentos del fragmento
        Bundle args = getArguments();
        if (args != null) {
            int userId = args.getInt("userId", -1); // -1 si no hay userId en los argumentos
            if (userId != -1) {
                loadUserData(userId); // Cargar los datos del usuario usando el userId
            } else {
                Toast.makeText(requireContext(), "Error: Usuario no identificado", Toast.LENGTH_LONG).show();
            }
        }

        return view;
    }

    private void loadUserData(int userId) {
        // URL para obtener los datos del usuario (ajusta según tu API)
        String url = "http://44.215.236.242/NutriChefAI/cargar_usuario.php?id_usu=" + userId;

        // Crear una solicitud para obtener los datos del usuario
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Analiza la respuesta JSON
                        JSONObject jsonResponse = new JSONObject(response);
                        String estado = jsonResponse.getString("estado");

                        if (estado.equals("1")) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            String nombre = data.getString("usuario");
                            String edad = data.getString("edad");
                            String peso = data.getString("peso");
                            String altura = data.getString("altura");
                            String imc = data.getString("IMC");

                            // Establece los datos en los TextViews
                            nombreusuario.setText(nombre);
                            id.setText("ID: " + data.getString("id_usu")); // Muestra el ID del usuario cargado
                        } else {
                            Toast.makeText(requireContext(), "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Maneja errores de la solicitud
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(requireContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                }
        );

        // Añadir la solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}