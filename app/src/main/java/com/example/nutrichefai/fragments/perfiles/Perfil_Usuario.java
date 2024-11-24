package com.example.nutrichefai.fragments.perfiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Perfil_Usuario extends Fragment {

    private TextView nombreusuario, edad, peso, altura, imc;
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        // Inicializa las vistas
        nombreusuario = view.findViewById(R.id.textView8); // Asegúrate de que el ID sea correcto
        edad = view.findViewById(R.id.textView10);
        peso = view.findViewById(R.id.textView12);

        // Recuperar userId desde los argumentos o SharedPreferences
        // Recuperar el userId desde los argumentos o SharedPreferences
        int userId = getArguments() != null ? getArguments().getInt("userId", -1) : -1;
        if (userId == -1) {
            SharedPreferences preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            userId = preferences.getInt("userId", -1);
        }

        if (userId != -1) {
            Log.d("Perfil_Usuario", "userId recuperado: " + userId);
            loadUserData(userId);
        } else {
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadUserData(int userId) {
        // URL para obtener los datos del usuario (ajusta según tu API)
        String url = "http://98.82.247.63/NutriChefAi/cargar_usuario.php?id_usu=" + userId;

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
                            String edadUsuario = data.getString("fechaNac");
                            String pesoUsuario = data.getString("peso");
                            String alturaUsuario = data.getString("altura");
                            String imcUsuario = data.getString("imc");

                            // Establece los datos en los TextViews
                            nombreusuario.setText(nombre);
                            edad.setText(edadUsuario);
                            peso.setText(pesoUsuario);


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