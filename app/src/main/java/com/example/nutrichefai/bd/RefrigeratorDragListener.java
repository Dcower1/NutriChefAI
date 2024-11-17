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
import com.example.nutrichefai.fragments.freezer.Freezer_inv;

import java.util.HashMap;
import java.util.Map;

public class RefrigeratorDragListener implements View.OnDragListener {

    private final Context context;
    private final int idUsuario;
    private final Freezer_inv fragment;

    public RefrigeratorDragListener(Context context, int idUsuario, Freezer_inv fragment) {
        this.context = context;
        this.idUsuario = idUsuario;
        this.fragment = fragment;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setAlpha(0.7f); // Cambiar apariencia al entrar
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                v.setAlpha(1.0f); // Restaurar apariencia
                return true;

            case DragEvent.ACTION_DROP:
                v.setAlpha(1.0f);

                String idIngrediente = event.getClipData().getItemAt(0).getText().toString();
                fragment.showDynamicCard(Integer.parseInt(idIngrediente)); // Mostrar la tarjeta dinámica
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.setAlpha(1.0f);
                return true;

            default:
                return false;
        }
    }


}