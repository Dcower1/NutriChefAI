package com.example.nutrichefai.bd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;

import java.util.HashMap;
import java.util.Map;

public class RefrigeratorDragListener implements View.OnDragListener {


    private final Context context;
    private final Freezer_inv fragment;
    private final int userId;

    public RefrigeratorDragListener(Context context, Freezer_inv fragment, int userId) {
        this.context = context;
        this.fragment = fragment;
        this.userId = userId;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setAlpha(0.7f); // Cambia la transparencia al arrastrar sobre la vista
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                v.setAlpha(1.0f); // Restaura la transparencia
                return true;

            case DragEvent.ACTION_DROP:
                if (event.getClipData() != null && event.getClipData().getItemCount() > 0) {
                    try {
                        int idIngrediente = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                        Log.d("RefrigeratorDragListener", "Ingrediente soltado con ID: " + idIngrediente);

                        // Llama al método del fragmento para mostrar la tarjeta dinámica
                        fragment.showDynamicCard(idIngrediente);

                        // No asociamos el ingrediente aquí, solo mostramos la tarjeta y dejamos que el usuario lo haga cuando presione "Agregar"

                    } catch (NumberFormatException e) {
                        Log.e("RefrigeratorDragListener", "Error al convertir ID del ingrediente", e);
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.setAlpha(1.0f); // Restaura la transparencia
                return true;

            default:
                return false;
        }
    }
}