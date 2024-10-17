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
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Chat_menu extends Fragment {

    TextView nombreusuario,id;

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

        nombreusuario=view.findViewById(R.id.nomUsuario);
        id=view.findViewById(R.id.id_usuario);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // Cambia "userId" según tu clave

        if (userId != -1) {
            loadUserData(userId);
        } else {
            // Manejar el caso donde no se encontró el ID del usuario
            nombreusuario.setText("Usuario no encontrado");
            id.setText("ID no disponible");
        }

        return view;
    }
    private void loadUserData(int userId) {
        String url = "http://44.215.236.242/NutriChefAI/cargar_usuario.php?id=" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String estado = response.getString("estado");
                            if (estado.equals("1")) {
                                JSONObject userData = response.getJSONObject("data");
                                // Asumiendo que el JSON tiene campos 'usuario' y 'id_usu'
                                nombreusuario.setText(userData.getString("usuario"));
                                id.setText(userData.getString("id_usu"));
                            } else {
                                Toast.makeText(requireContext(), "Error al cargar los datos del usuario", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error en la respuesta del servidor", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        // Add request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

}