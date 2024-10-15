package com.example.nutrichefai.fragments.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.MainActivity;
import com.example.nutrichefai.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Usuario_log extends Fragment {

    private Button bt_ingresar;
    private EditText txtloginusu, txtpassword;
    private RequestQueue datos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario_log, container, false);

        // Initialize views
        txtloginusu = view.findViewById(R.id.editTextLoginUsername);
        txtpassword = view.findViewById(R.id.editTextLoginPassword);
        bt_ingresar = view.findViewById(R.id.btn_ingresar); // Initialize button


        datos = Volley.newRequestQueue(requireContext());


        bt_ingresar.setOnClickListener(v -> {
            String usernameOrEmail = txtloginusu.getText().toString().trim();
            String password = txtpassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            consultardatos(usernameOrEmail, password);
        });

        return view;
    }

    public void consultardatos(String loginInput, String pass) {
        String url = "http://44.215.236.242/NutriChefAI/consultar_usuario.php?login=" + loginInput + "&password=" + pass;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String estado = response.getString("estado");
                            if (estado.equals("0")) {
                                Toast.makeText(requireContext(), "Usuario no Existe", Toast.LENGTH_LONG).show();
                            } else {
                                // Proceed to MainActivity if login is successful
                                Intent ventana = new Intent(requireActivity(), MainActivity.class);
                                startActivity(ventana);
                                requireActivity().finish(); // Optional: Close the login activity
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
        datos.add(request);
    }
}