package com.example.nutrichefai.fragments.freezer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.R;
import com.example.nutrichefai.adapters.InventarioAdapter;
import com.example.nutrichefai.bd.InventarioIngrediente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inverntario_usuario extends Fragment {

    private RecyclerView recyclerView;
    private List<InventarioIngrediente> ingredientesList; // Lista de datos
    private InventarioAdapter adapter; // Adaptador
    private int userId;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inverntario_usuario, container, false);

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_ingredients);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3)); // Vista en rejilla (3 columnas)
        ingredientesList = new ArrayList<>();

        // Recuperar userId desde argumentos o SharedPreferences
        if (getArguments() != null && getArguments().containsKey("userId")) {
            userId = getArguments().getInt("userId", -1);
        } else {
            SharedPreferences preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            userId = preferences.getInt("userId", -1); // Ajustar a la clave correcta
        }

        // Validar userId y cargar el inventario
        if (userId == -1) {
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            Log.e("Inverntario_usuario", "Error: Usuario no identificado.");
        } else {
            Log.d("Inverntario_usuario", "userId recuperado: " + userId);
            cargarInventarioUsuario(userId); // Cargar inventario si userId es válido
        }

        return view;
    }
    private void cargarInventarioUsuario(int userId) {
        String url = "http://98.82.247.63/NutriChefAi/get_inventario_usuario.php?id_usuario=" + userId;

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        Log.d("Inverntario_usuario", "URL de inventario: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("estado") == 1) {
                            JSONArray ingredientes = response.getJSONArray("ingredientes");

                            ingredientesList.clear();
                            for (int i = 0; i < ingredientes.length(); i++) {
                                JSONObject obj = ingredientes.getJSONObject(i);

                                ingredientesList.add(new InventarioIngrediente(
                                        obj.optInt("id_ingrediente", -1),
                                        obj.optString("nombre", "Desconocido"),
                                        obj.optString("descripcion", "Sin descripción"),
                                        obj.optString("image_nombre", "default_image"),
                                        obj.optString("cantidad", "0")
                                ));
                            }

                            if (adapter == null) {
                                adapter = new InventarioAdapter(ingredientesList, requireContext());
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(ingrediente -> {
                                    mostrarCardInventario(ingrediente); // Llama a mostrarCardInventario
                                });
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(requireContext(), "No hay ingredientes en el inventario.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("Inverntario_usuario", "Error al procesar JSON", e);
                    }
                },
                error -> Log.e("Inverntario_usuario", "Error al cargar datos", error)
        );

        queue.add(request);
    }


    private void mostrarCardInventario(InventarioIngrediente ingrediente) {
        CardView cardViewInventario = requireView().findViewById(R.id.cardview_inventario);
        FloatingActionButton fabCancelar = requireView().findViewById(R.id.ic_cancelar);

        if (cardViewInventario == null) {
            Log.e("mostrarCardInventario", "cardview_inventario no encontrado en el layout");
            return;
        }
        // Mostrar la tarjeta y el botón flotante
        cardViewInventario.setVisibility(View.VISIBLE);
        fabCancelar.setVisibility(View.VISIBLE);

        cardViewInventario.setVisibility(View.VISIBLE);

        // Inicializar las vistas
        ImageView foodImage = cardViewInventario.findViewById(R.id.image_food_inventario);
        TextView foodName = cardViewInventario.findViewById(R.id.text_food_name_inventario);
        TextView foodQuantity = cardViewInventario.findViewById(R.id.text_food_quantity);
        Button addButton = cardViewInventario.findViewById(R.id.button_add_quantity);
        Button subtractButton = cardViewInventario.findViewById(R.id.button_subtract_quantity);
        ImageButton deleteButton = cardViewInventario.findViewById(R.id.button_delete);
        ImageButton acceptButton = cardViewInventario.findViewById(R.id.btn_add);

        // Limpia y convierte la cantidad a un número entero
        final int[] cantidadTemporal;
        try {
            cantidadTemporal = new int[]{Integer.parseInt(ingrediente.getCantidad().trim())};
        } catch (NumberFormatException e) {
            Log.e("mostrarCardInventario", "Cantidad no válida: " + ingrediente.getCantidad());
            Toast.makeText(requireContext(), "Error: cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar datos iniciales
        foodName.setText(ingrediente.getNombre());
        foodQuantity.setText("Cantidad: " + cantidadTemporal[0]);

        // Configurar imagen
        int imageResourceId = requireContext().getResources().getIdentifier(
                ingrediente.getImageName(), "drawable", requireContext().getPackageName());
        foodImage.setImageResource(imageResourceId != 0 ? imageResourceId : R.drawable.default_image);

        // Botón para añadir cantidad
        addButton.setOnClickListener(v -> {
            cantidadTemporal[0]++;
            foodQuantity.setText("Cantidad: " + cantidadTemporal[0]);
        });

        // Botón para restar cantidad
        subtractButton.setOnClickListener(v -> {
            if (cantidadTemporal[0] > 0) {
                cantidadTemporal[0]--;
                foodQuantity.setText("Cantidad: " + cantidadTemporal[0]);
            } else {
                Toast.makeText(requireContext(), "La cantidad no puede ser negativa.", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para eliminar ingrediente
        deleteButton.setOnClickListener(v -> {
            eliminarIngredienteEnServidor(ingrediente.getId());
            cardViewInventario.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE);// Ocultar la tarjeta después de eliminar
        });

        // Botón para aceptar cambios
        acceptButton.setOnClickListener(v -> {
            // Enviar la cantidad temporal al servidor
            actualizarCantidadEnServidor(ingrediente.getId(), cantidadTemporal[0]);

            // Actualizar la cantidad en el objeto local
            ingrediente.setCantidad(String.valueOf(cantidadTemporal[0]));

            // Ocultar la tarjeta
            cardViewInventario.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE);
        });

        fabCancelar.setOnClickListener(v -> {
            cardViewInventario.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE); // Ocultar el botón flotante
        });
        // Aplicar animación de FAB
        Animation slideUpAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_slide_up);
        fabCancelar.startAnimation(slideUpAnim);
        Log.d("mostrarCardInventario", "Ingrediente mostrado: " + ingrediente.getNombre());
    }


    private void actualizarCantidadEnServidor(int idIngrediente, int nuevaCantidad) {
        String url = "http://98.82.247.63/NutriChefAi/actualizar_cantidad.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getInt("estado") == 1) {
                            Toast.makeText(requireContext(), "Cantidad actualizada correctamente", Toast.LENGTH_SHORT).show();

                            // Actualiza la cantidad visualmente en el adaptador
                            if (adapter != null) {
                                adapter.updateCantidad(idIngrediente, String.valueOf(nuevaCantidad));
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error al actualizar cantidad", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("ActualizarCantidad", "Error al procesar la respuesta: " + e.getMessage());
                    }
                },
                error -> Log.e("ActualizarCantidad", "Error de red: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId)); // ID del usuario
                params.put("id_ingrediente", String.valueOf(idIngrediente)); // ID del ingrediente
                params.put("cantidad", String.valueOf(nuevaCantidad)); // Nueva cantidad
                return params;
            }
        };

        queue.add(request);
    }
    private void eliminarIngredienteEnServidor(int idIngrediente) {
        String url = "http://98.82.247.63/NutriChefAi/eliminar_ingrediente.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getInt("estado") == 1) {
                            Toast.makeText(requireContext(), "Ingrediente eliminado correctamente", Toast.LENGTH_SHORT).show();
                            eliminarIngredienteLocal(idIngrediente); // Eliminar localmente
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar ingrediente", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("EliminarIngrediente", "Error al procesar la respuesta: " + e.getMessage());
                    }
                },
                error -> Log.e("EliminarIngrediente", "Error de red: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId)); // ID del usuario
                params.put("id_ingrediente", String.valueOf(idIngrediente)); // ID del ingrediente
                return params;
            }
        };

        queue.add(request);
    }
    private void eliminarIngredienteLocal(int idIngrediente) {
        for (int i = 0; i < ingredientesList.size(); i++) {
            if (ingredientesList.get(i).getId() == idIngrediente) {
                ingredientesList.remove(i); // Eliminar de la lista
                adapter.notifyItemRemoved(i); // Notificar al adaptador
                break;
            }
        }
    }
}