package com.example.nutrichefai.fragments.freezer;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;

import com.example.nutrichefai.adapters.GrupoAdapter;
import com.example.nutrichefai.adapters.IngredienteAdapter;
import com.example.nutrichefai.adapters.TipoAlimentoAdapter;
import com.example.nutrichefai.bd.Grupo;
import com.example.nutrichefai.bd.Ingrediente;
import com.example.nutrichefai.bd.RefrigeratorDragListener;
import com.example.nutrichefai.bd.TipoAlimento;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Freezer_inv extends Fragment {
    private RecyclerView recyclerView;
    private List<Grupo> grupoList;
    private List<TipoAlimento> tipoAlimentoList;
    private List<Ingrediente> ingredienteList;

    private GrupoAdapter grupoAdapter;
    private TipoAlimentoAdapter tipoAlimentoAdapter;
    private IngredienteAdapter ingredienteAdapter;

    private boolean showingGrupos = true; // Estado inicial: mostrando grupos

    private ImageView backButton; // Botón de retroceso

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freezer_inv, container, false);

        recyclerView = view.findViewById(R.id.selector_ingredientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        grupoList = new ArrayList<>();
        tipoAlimentoList = new ArrayList<>();
        ingredienteList = new ArrayList<>();

        backButton = view.findViewById(R.id.bt_atras);
        backButton.setVisibility(View.GONE); // Ocultar el botón inicialmente
        backButton.setOnClickListener(v -> handleBackNavigation());

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        int idUsuario = preferences.getInt("idUsuario", -1); // ID guardado, usa un valor por defecto si no existe

        // Configurar el DragListener
        ImageView refrigerator = view.findViewById(R.id.image_refrigerator);
        refrigerator.setOnDragListener(new RefrigeratorDragListener(requireContext(), idUsuario));

        loadGrupos(); // Carga los grupos inicialmente

        return view;
    }


    private void loadGrupos() {
        String url = "http://98.82.247.63/NutriChefAi/obtener_grupos.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            grupoList.clear();
                            JSONArray grupos = response.getJSONArray("grupos");

                            for (int i = 0; i < grupos.length(); i++) {
                                JSONObject grupo = grupos.getJSONObject(i);
                                grupoList.add(new Grupo(
                                        grupo.getInt("id_grupo"),
                                        grupo.getString("nombre_grupo"),
                                        grupo.getString("imagen_grupo")
                                ));
                            }

                            grupoAdapter = new GrupoAdapter(grupoList, requireContext(), Freezer_inv.this);
                            recyclerView.setAdapter(grupoAdapter);
                            backButton.setVisibility(View.GONE); // Ocultar el botón al mostrar grupos
                            showingGrupos = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    public void loadTiposAlimentos(int idGrupo) {
        String url = "http://98.82.247.63/NutriChefAi/get_tipo.php?id_grupo=" + idGrupo;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            tipoAlimentoList.clear();
                            JSONArray tipos = response.getJSONArray("tipos");

                            for (int i = 0; i < tipos.length(); i++) {
                                JSONObject tipo = tipos.getJSONObject(i);
                                tipoAlimentoList.add(new TipoAlimento(
                                        tipo.getInt("id_tipo"),
                                        tipo.getString("nombre_tipo"),
                                        tipo.getString("imagen_tipo"),
                                        idGrupo
                                ));
                            }

                            tipoAlimentoAdapter = new TipoAlimentoAdapter(tipoAlimentoList, requireContext(), Freezer_inv.this);
                            recyclerView.setAdapter(tipoAlimentoAdapter);
                            backButton.setVisibility(View.VISIBLE); // Mostrar el botón en tipos de alimentos
                            showingGrupos = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    public void loadIngredientes(int idTipo) {
        String url = "http://98.82.247.63/NutriChefAi/get_ingredientes.php?id_tipo=" + idTipo;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            ingredienteList.clear();
                            JSONArray ingredientes = response.getJSONArray("ingredientes");

                            for (int i = 0; i < ingredientes.length(); i++) {
                                JSONObject ingrediente = ingredientes.getJSONObject(i);
                                ingredienteList.add(new Ingrediente(
                                        ingrediente.getInt("id_ingrediente"),
                                        ingrediente.getString("nombre"),
                                        ingrediente.getString("descripcion"),
                                        ingrediente.getString("image_nombre")
                                ));
                            }

                            ingredienteAdapter = new IngredienteAdapter(ingredienteList, requireContext());
                            recyclerView.setAdapter(ingredienteAdapter);
                            backButton.setVisibility(View.VISIBLE); // Mostrar el botón en ingredientes
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        queue.add(request);
    }


    private void asociarIngredienteConUsuario(int idIngrediente) {
        SharedPreferences preferences = requireContext().getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        int idUsuario = preferences.getInt("idUsuario", -1); // Obtiene el ID del usuario actual

        if (idUsuario == -1) {
            Toast.makeText(requireContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://98.82.247.63/NutriChefAi/asociar_ingrediente.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Toast.makeText(requireContext(), "Ingrediente agregado al inventario", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Error: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error procesando respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Error al asociar ingrediente: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
                return params;
            }
        };

        queue.add(request);
    }
    public void handleBackNavigation() {
        if (recyclerView.getAdapter() instanceof IngredienteAdapter) {
            recyclerView.setAdapter(tipoAlimentoAdapter);
            backButton.setVisibility(View.VISIBLE); // Botón sigue visible en tipos de alimentos
        } else if (recyclerView.getAdapter() instanceof TipoAlimentoAdapter) {
            recyclerView.setAdapter(grupoAdapter);
            backButton.setVisibility(View.GONE); // Ocultar el botón en grupos
            showingGrupos = true;
        } else {
            Toast.makeText(requireContext(), "Ya estás en la vista inicial", Toast.LENGTH_SHORT).show();
        }
    }
}