package com.example.nutrichefai.bd;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RefrigeratorDragListener implements View.OnDragListener {

    private final Context context;
    private final int idUsuario; // ID del usuario que inició sesión

    public RefrigeratorDragListener(Context context, int idUsuario) {
        this.context = context;
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true; // Indica que el evento fue recibido

            case DragEvent.ACTION_DRAG_ENTERED:
                // Cambiar apariencia del refrigerador al entrar
                v.setAlpha(0.7f);
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                // Restaurar apariencia del refrigerador
                v.setAlpha(1.0f);
                return true;

            case DragEvent.ACTION_DROP:
                // Obtener el ID del ingrediente arrastrado
                int idIngrediente = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                asociarIngredienteConUsuario(idIngrediente);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Restaurar apariencia del refrigerador
                v.setAlpha(1.0f);
                return true;

            default:
                return false;
        }
    }


    private void asociarIngredienteConUsuario(int idIngrediente) {
        String url = "http://98.82.247.63/NutriChefAi/asociar_ingrediente.php";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar respuesta del servidor
                        Toast.makeText(context, "Ingrediente agregado al inventario", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error
                        Toast.makeText(context, "Error al agregar el ingrediente: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros que serán enviados al servidor
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_ingrediente", String.valueOf(idIngrediente));
                return params;
            }
        };

        queue.add(request);
    }
}