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
    private List<InventarioIngrediente> ingredientesList;
    private InventarioAdapter adapter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inverntario_usuario, container, false);

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_ingredients);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3)); // Vista en rejilla
        ingredientesList = new ArrayList<>();

        // Recuperar userId desde los argumentos del fragmento
        if (getArguments() != null && getArguments().containsKey("userId")) {
            userId = getArguments().getInt("userId", -1);
        } else {
            Log.e("Inventario_usuario", "No se recibió userId en los argumentos.");
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            return view; // Terminar la ejecución si no hay userId
        }

        // Validar userId y cargar el inventario
        if (userId == -1) {
            Log.e("Inventario_usuario", "ID de usuario inválido.");
            Toast.makeText(requireContext(), "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Inventario_usuario", "ID de usuario recibido: " + userId);
            cargarInventarioUsuario(userId); // Cargar inventario
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Inventario_usuario", "Fragmento destruido.");
        // Limpieza adicional si es necesario
        recyclerView.setAdapter(null);
        ingredientesList.clear();
    }

    private void cargarInventarioUsuario(int userId) {
        String url = "http://98.82.247.63/NutriChefAi/get_inventario_usuario.php?id_usuario=" + userId;

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        Log.d("Inventario_usuario", "URL de inventario: " + url);

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

                            // Configurar adaptador
                            if (adapter == null) {
                                adapter = new InventarioAdapter(ingredientesList, requireContext());
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(ingrediente -> {
                                    mostrarCardInventario(ingrediente);
                                });
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(requireContext(), "No hay ingredientes en el inventario.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("Inventario_usuario", "Error al procesar JSON", e);
                    }
                },
                error -> Log.e("Inventario_usuario", "Error al cargar datos", error)
        );

        queue.add(request);
    }

    private void mostrarCardInventario(InventarioIngrediente ingrediente) {
        CardView cardView = requireView().findViewById(R.id.cardview_inventario);
        FloatingActionButton fabCancelar = requireView().findViewById(R.id.ic_cancelar);

        if (cardView == null) {
            Log.e("mostrarCardInventario", "CardView no encontrado en el layout.");
            return;
        }

        // Configuración inicial
        cardView.setVisibility(View.VISIBLE);
        fabCancelar.setVisibility(View.VISIBLE);

        ImageView foodImage = cardView.findViewById(R.id.image_food_inventario);
        TextView foodName = cardView.findViewById(R.id.text_food_name_inventario);
        TextView foodQuantity = cardView.findViewById(R.id.text_food_quantity);
        Button addButton = cardView.findViewById(R.id.button_add_quantity);
        Button subtractButton = cardView.findViewById(R.id.button_subtract_quantity);
        ImageButton deleteButton = cardView.findViewById(R.id.button_delete);
        ImageButton acceptButton = cardView.findViewById(R.id.btn_add);

        // Cantidad temporal para gestionar incrementos/decrementos
        final int[] cantidadTemporal = {Integer.parseInt(ingrediente.getCantidad())};

        // Configurar vistas
        foodName.setText(ingrediente.getNombre());
        foodQuantity.setText("Cantidad: " + cantidadTemporal[0]);

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
                Toast.makeText(requireContext(), "La cantidad no puede ser menor a 0.", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para aceptar los cambios y actualizar el servidor
        acceptButton.setOnClickListener(v -> {
            actualizarCantidadEnServidor(ingrediente.getId(), cantidadTemporal[0]);
            ingrediente.setCantidad(String.valueOf(cantidadTemporal[0])); // Actualizar el objeto local
            adapter.notifyDataSetChanged(); // Notificar al adaptador
            cardView.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE);
        });

        // Botón para eliminar el ingrediente
        deleteButton.setOnClickListener(v -> {
            eliminarIngredienteEnServidor(ingrediente.getId());
            ingredientesList.remove(ingrediente); // Eliminar de la lista local
            adapter.notifyDataSetChanged(); // Notificar al adaptador
            cardView.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE);
        });

        // Botón para cancelar la operación
        fabCancelar.setOnClickListener(v -> {
            cardView.setVisibility(View.GONE);
            fabCancelar.setVisibility(View.GONE);
        });
    }

    private void actualizarCantidadEnServidor(int idIngrediente, int nuevaCantidad) {
        String url = "http://98.82.247.63/NutriChefAi/actualizar_cantidad.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Inventario_usuario", "Cantidad actualizada en el servidor.");
                },
                error -> Log.e("Inventario_usuario", "Error al actualizar cantidad", error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
                params.put("cantidad", String.valueOf(nuevaCantidad));
                return params;
            }
        };

        queue.add(request);
    }

    private void eliminarIngredienteEnServidor(int idIngrediente) {
        String url = "http://98.82.247.63/NutriChefAi/eliminar_ingrediente.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Log.d("Inventario_usuario", "Ingrediente eliminado."),
                error -> Log.e("Inventario_usuario", "Error al eliminar ingrediente", error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
                return params;
            }
        };

        queue.add(request);
    }
}