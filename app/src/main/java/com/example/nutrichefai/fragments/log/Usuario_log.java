package com.example.nutrichefai.fragments.log;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.example.nutrichefai.bd.DBHelper;
import com.example.nutrichefai.utils.Utilidades;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class Usuario_log extends Fragment {

    private Button bt_ingresar;
    private EditText txtloginusu, txtpassword;
    private RequestQueue datos;
    private DBHelper dbHelper;

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

        dbHelper = new DBHelper(requireContext());
        bt_ingresar.setOnClickListener(v -> {
            String usernameOrEmail = txtloginusu.getText().toString().trim();
            String password = txtpassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            String hashedPassword = Utilidades.hashPassword(password);
            // Check credentials locally first
            if (checkLocalCredentials(usernameOrEmail, password)) {
                // Login successful locally
                Intent ventana = new Intent(requireActivity(), MainActivity.class);
                startActivity(ventana);
                requireActivity().finish();
            } else {
                // Check for internet connection
                if (isNetworkAvailable()) {
                    // If local check fails and internet is available, check on the server
                    consultardatos(usernameOrEmail, hashedPassword);
                } else {
                    // No internet connection, inform the user
                    Toast.makeText(requireContext(), "Sin conexión a Internet. No se puede verificar con el servidor.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
    private boolean checkLocalCredentials(String usernameOrEmail, String password) {

        return dbHelper.validateUser(usernameOrEmail, Utilidades.hashPassword(password));
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
                                // Obtén el id_usu de la respuesta
                                int userId = response.getInt("id_usu"); // Asumiendo que se devuelve en la respuesta

                                // Guardar el id_usu en SharedPreferences
                                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("userId", userId);
                                editor.apply();

                                // Ahora, llama a cargar_usuario.php con el id_usu
                                cargarUsuario(userId);
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

    // Método para cargar los datos del usuario
    private void cargarUsuario(int userId) {
        String url = "http://44.215.236.242/NutriChefAI/cargar_usuario.php?id=" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String estado = response.getString("estado");
                            if (estado.equals("1")) {
                                JSONObject userData = response.getJSONObject("data");
                                // Aquí puedes pasar los datos al siguiente fragmento
                                Intent ventana = new Intent(requireActivity(), MainActivity.class);
                                ventana.putExtra("userData", userData.toString()); // Convierte a string para pasar
                                startActivity(ventana);
                                requireActivity().finish();
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
        datos.add(request);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}