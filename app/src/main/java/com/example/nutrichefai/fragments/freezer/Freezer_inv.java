package com.example.nutrichefai.fragments.freezer;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
<<<<<<< Updated upstream
=======
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
>>>>>>> Stashed changes
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    private int userId; // ID del usuario

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
        backButton.setVisibility(View.GONE);
        backButton.setOnClickListener(v -> handleBackNavigation());

        // Recuperar el userId desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("idUsuario", -1);
        Log.d("Freezer_inv", "userId recuperado: " + userId);

        if (userId == -1) {
            Toast.makeText(requireContext(), "Usuario no identificado. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return view; // Evita continuar si no hay usuario identificado
        }

        // Configurar el DragListener
        ImageView refrigerator = view.findViewById(R.id.image_refrigerator);
        refrigerator.setOnDragListener(new RefrigeratorDragListener(requireContext(), userId, this));

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

    private Ingrediente obtenerIngredientePorId(int idIngrediente) {
        for (Ingrediente ingrediente : ingredienteList) {
            if (ingrediente.getId() == idIngrediente) {
                return ingrediente;
            }
        }
        return null;
    }

<<<<<<< Updated upstream
    private void asociarIngredienteConUsuario(int idIngrediente, String cantidad) {
=======
    private void asociarIngredienteConUsuario(int idIngrediente, String cantidadStr) {
        try {
            // Convertir cantidad a un número entero
            int cantidad = Integer.parseInt(cantidadStr.split(" ")[0].trim());

            // Verificar si el ingrediente ya está en el inventario
            if (ingredienteCantidadMap.containsKey(idIngrediente)) {
                // Sumar la cantidad al inventario
                int nuevaCantidad = (int) (ingredienteCantidadMap.get(idIngrediente) + cantidad);
                ingredienteCantidadMap.put(idIngrediente, Double.valueOf(nuevaCantidad));

                // Notificar al servidor sobre la suma actualizada
                enviarActualizacionAlServidor(idIngrediente, nuevaCantidad);

                Toast.makeText(requireContext(), "Cantidad actualizada: " + nuevaCantidad, Toast.LENGTH_SHORT).show();
            } else {
                // Añadir nuevo ingrediente al inventario
                ingredienteCantidadMap.put(idIngrediente, Double.valueOf(cantidad));

                // Notificar al servidor sobre el nuevo ingrediente
                enviarActualizacionAlServidor(idIngrediente, cantidad);

                Toast.makeText(requireContext(), "Ingrediente añadido con cantidad: " + cantidad, Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Cantidad inválida. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para enviar la actualización al servidor
    private void enviarActualizacionAlServidor(int idIngrediente, int cantidad) {
>>>>>>> Stashed changes
        String url = "http://98.82.247.63/NutriChefAi/asociar_ingrediente.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(requireContext(), "Ingrediente agregado al inventario", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(requireContext(), "Error al agregar ingrediente", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
<<<<<<< Updated upstream
                params.put("cantidad", cantidad);
=======
                params.put("cantidad", String.valueOf(cantidad)); // Enviar como entero convertido a String
>>>>>>> Stashed changes
                return params;
            }
        };

        queue.add(request);
    }

    public void showDynamicCard(int idIngrediente) {
        CardView cardView = requireView().findViewById(R.id.card_view_dynamic);
        TextView foodName = cardView.findViewById(R.id.text_food_name_dynamic);
        ImageView foodImage = cardView.findViewById(R.id.image_food_dynamic);
        EditText editQuantity = cardView.findViewById(R.id.edit_quantity_dynamic);
        Spinner spinnerUnit = cardView.findViewById(R.id.spinner_unit_dynamic);
        Button buttonAdd = cardView.findViewById(R.id.button_add_dynamic);
        Button buttonCancel = cardView.findViewById(R.id.button_cancel_dynamic);

        // Configurar EditText para aceptar solo números enteros
        editQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Obtener los detalles del ingrediente por ID
        Ingrediente ingrediente = obtenerIngredientePorId(idIngrediente);
        if (ingrediente != null) {
            // Establecer el nombre del ingrediente
            foodName.setText(ingrediente.getNombre());

            // Buscar la imagen en drawable usando el nombre almacenado en la tabla
            int imageResourceId = requireContext().getResources().getIdentifier(
                    ingrediente.getImageName(), "drawable", requireContext().getPackageName());

            // Establecer la imagen si se encuentra; de lo contrario, usar una imagen predeterminada
<<<<<<< Updated upstream
            if (imageResourceId != 0) {
                foodImage.setImageResource(imageResourceId);
            } else {
                foodImage.setImageResource(R.drawable.default_image); // Imagen predeterminada
=======
            foodImage.setImageResource(imageResourceId != 0 ? imageResourceId : R.drawable.default_image);

            // Configurar visibilidad y opciones del Spinner o texto basado en la descripción
            String descripcion = ingrediente.getDescripcion();
            if ("1".equals(descripcion)) { // Mostrar solo unidades
                spinnerUnit.setVisibility(View.GONE); // Ocultar el Spinner
                editQuantity.setHint("Unidades");
            } else if ("2".equals(descripcion)) { // Mostrar solo gramos
                spinnerUnit.setVisibility(View.GONE); // Ocultar el Spinner
                editQuantity.setHint("Gramos");
            } else if ("3".equals(descripcion)) { // Mostrar ambos en Spinner
                spinnerUnit.setVisibility(View.VISIBLE); // Mostrar el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        requireContext().getResources().getStringArray(R.array.units_array)
                );
                spinnerUnit.setAdapter(adapter);
                editQuantity.setHint("Cantidad");
>>>>>>> Stashed changes
            }
        }

        // Mostrar la tarjeta
        cardView.setVisibility(View.VISIBLE);

        // Manejar el botón "Añadir"
        buttonAdd.setOnClickListener(v -> {
            String cantidad = editQuantity.getText().toString();
<<<<<<< Updated upstream
            String unidad = spinnerUnit.getSelectedItem().toString();
=======
>>>>>>> Stashed changes

            // Validar que la cantidad no esté vacía y sea un número entero válido
            if (cantidad.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingresa la cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int cantidadEntera = Integer.parseInt(cantidad); // Convertir directamente a entero
                String unidad = spinnerUnit.getSelectedItem() != null ? spinnerUnit.getSelectedItem().toString() : "unidad";

                // Asociar el ingrediente con el usuario
                asociarIngredienteConUsuario(idIngrediente, cantidadEntera + " " + unidad);

                // Ocultar la tarjeta después de agregar
                cardView.setVisibility(View.GONE);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Por favor, ingresa un número entero válido.", Toast.LENGTH_SHORT).show();
            }
        });

        // Manejar el botón "Cancelar"
        buttonCancel.setOnClickListener(v -> cardView.setVisibility(View.GONE));
    }

<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
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