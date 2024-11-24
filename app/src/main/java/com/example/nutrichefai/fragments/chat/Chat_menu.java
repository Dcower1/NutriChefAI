package com.example.nutrichefai.fragments.chat;

import android.annotation.SuppressLint;
import android.app.BroadcastOptions;
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

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;
import com.example.nutrichefai.fragments.platillos.Platillos;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Chat_menu extends Fragment {

    private TextView nombreusuario, id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_menu, container, false);

        // Inicializar las vistas
        nombreusuario = view.findViewById(R.id.nomUsuario);
        id = view.findViewById(R.id.id_usuario);

        // Recuperar el userId desde los argumentos o SharedPreferences
        int userId = getArguments() != null ? getArguments().getInt("userId", -1) : -1;
        if (userId == -1) {
            SharedPreferences preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            userId = preferences.getInt("userId", -1);
        }

        if (userId != -1) {
            Log.d("Chat_menu", "userId recuperado: " + userId);
            loadUserData(userId);
        } else {
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadUserData(int userId) {
        String url = "http://98.82.247.63/NutriChefAi/cargar_usuario.php?id_usu=" + userId;
        Log.d("Chat_menu", "Cargando datos del usuario con URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String estado = jsonResponse.getString("estado");

                        if (estado.equals("1")) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            String nombre = data.getString("usuario");

                            nombreusuario.setText(nombre);
                            id.setText("ID: " + userId);
                        } else {
                            Toast.makeText(requireContext(), "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e("Chat_menu", "Error al cargar datos: " + error.toString())
        );

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}