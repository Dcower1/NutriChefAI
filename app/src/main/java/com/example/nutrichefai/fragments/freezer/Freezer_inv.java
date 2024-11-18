package com.example.nutrichefai.fragments.freezer;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    private Map<Integer, Integer> ingredienteCantidadMap; // Manejo de cantidades como enteros

    private int userId; // ID del usuario que inició sesión

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freezer_inv, container, false);

        // Recuperar el userId desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1); // Devuelve -1 si no se encuentra el ID

        if (userId == -1) {
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            Log.e("Freezer_inv", "Error: Usuario no identificado.");
        } else {
            Log.d("Freezer_inv", "userId recuperado: " + userId);
        }

        // Configuración inicial
        recyclerView = view.findViewById(R.id.selector_ingredientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        grupoList = new ArrayList<>();
        tipoAlimentoList = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        ingredienteCantidadMap = new HashMap<>();

        backButton = view.findViewById(R.id.bt_atras);
        backButton.setVisibility(View.GONE);
        backButton.setOnClickListener(v -> handleBackNavigation());

        // Configurar el DragListener con el userId
        ImageView refrigerator = view.findViewById(R.id.image_refrigerator);
        refrigerator.setOnDragListener(new RefrigeratorDragListener(requireContext(), this, userId));

        // Configurar animación de zoom
        refrigerator.setOnClickListener(v -> {
            Animation zoomIn = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in);
            refrigerator.startAnimation(zoomIn);

            // Navegar al inventario después de la animación
            zoomIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // Nada
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    navigateToInverntarioUsuario(); // Navega al fragmento del inventario
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Nada
                }
            });
        });

        //refrigerator.setOnClickListener(v -> navigateToInverntarioUsuario());

        loadGrupos();

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
                        Toast.makeText(requireContext(), "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos.", Toast.LENGTH_SHORT).show());

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
                        Toast.makeText(requireContext(), "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos.", Toast.LENGTH_SHORT).show());

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
                        Toast.makeText(requireContext(), "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error al cargar los datos.", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    public void asociarIngredienteConUsuario(int idIngrediente, int cantidad) {
        String url = "http://98.82.247.63/NutriChefAi/asociar_ingrediente.php";

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(requireContext(), "Ingrediente agregado al inventario", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(requireContext(), "Error al agregar el ingrediente: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
                params.put("cantidad", String.valueOf(cantidad));
                return params;
            }
        };

        queue.add(request);
    }


    private void navigateToInverntarioUsuario() {
        if (userId == -1) {
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            Log.e("Freezer_inv", "Error: Usuario no identificado.");
            return;
        }

        Fragment inverntarioUsuarioFragment = new Inverntario_usuario();

        // Pasar el userId al fragmento
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        inverntarioUsuarioFragment.setArguments(args);

        // Realizar la transacción del fragmento
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, inverntarioUsuarioFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.d("Freezer_inv", "Navegación a Inverntario_usuario con userId: " + userId);
    }

    public void showDynamicCard(int idIngrediente) {
        Log.d("Freezer_inv", "showDynamicCard llamado con ID: " + idIngrediente);

        CardView cardView = requireView().findViewById(R.id.card_view_dynamic);
        TextView foodName = cardView.findViewById(R.id.text_food_name_dynamic);
        ImageView foodImage = cardView.findViewById(R.id.image_food_dynamic);
        EditText editQuantity = cardView.findViewById(R.id.edit_quantity_dynamic);
        Spinner spinnerUnit = cardView.findViewById(R.id.spinner_unit_dynamic);
        Button buttonAdd = cardView.findViewById(R.id.button_add_dynamic);
        Button buttonCancel = cardView.findViewById(R.id.button_cancel_dynamic);

        // Busca el ingrediente por ID
        Ingrediente ingrediente = obtenerIngredientePorId(idIngrediente);
        if (ingrediente != null) {
            Log.d("Freezer_inv", "Ingrediente encontrado: " + ingrediente.getNombre());

            // Configura el nombre y la imagen del ingrediente
            foodName.setText(ingrediente.getNombre());
            int imageResourceId = requireContext().getResources().getIdentifier(
                    ingrediente.getImageName(), "drawable", requireContext().getPackageName());
            foodImage.setImageResource(imageResourceId != 0 ? imageResourceId : R.drawable.default_image);

            // Configurar el botón de agregar cantidad
            buttonAdd.setOnClickListener(v -> {
                String cantidadStr = editQuantity.getText().toString().trim();

                if (cantidadStr.isEmpty()) {
                    Toast.makeText(requireContext(), "Por favor, ingresa una cantidad válida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0) {
                        Toast.makeText(requireContext(), "La cantidad debe ser mayor a 0.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Asociar el ingrediente con la cantidad ingresada
                    asociarIngredienteConUsuario(idIngrediente, cantidad);

                    // Ocultar la tarjeta dinámica
                    cardView.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Cantidad enviada: " + cantidad, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Cantidad inválida. Por favor, ingresa un número.", Toast.LENGTH_SHORT).show();
                }
            });

            // Configurar el botón de cancelar
            buttonCancel.setOnClickListener(v -> cardView.setVisibility(View.GONE));

            // Mostrar la tarjeta dinámica
            cardView.setVisibility(View.VISIBLE);
        } else {
            Log.e("Freezer_inv", "Ingrediente no encontrado para ID: " + idIngrediente);
        }
    }


    private Ingrediente obtenerIngredientePorId(int idIngrediente) {
        for (Ingrediente ingrediente : ingredienteList) {
            if (ingrediente.getId() == idIngrediente) {
                return ingrediente;
            }
        }
        return null; // Devuelve null si no encuentra el ingrediente
    }

    public void handleBackNavigation() {
        if (recyclerView.getAdapter() instanceof IngredienteAdapter) {
            recyclerView.setAdapter(tipoAlimentoAdapter);
            backButton.setVisibility(View.VISIBLE);
        } else if (recyclerView.getAdapter() instanceof TipoAlimentoAdapter) {
            recyclerView.setAdapter(grupoAdapter);
            backButton.setVisibility(View.GONE);
            showingGrupos = true;
        } else {
            Toast.makeText(requireContext(), "Ya estás en la vista inicial", Toast.LENGTH_SHORT).show();
        }
    }


}