package com.example.nutrichefai.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.Grupo;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;

import java.time.Instant;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder> {
    private List<Grupo> grupos;
    private Context context;
    private Freezer_inv fragment;

    public GrupoAdapter(List<Grupo> grupos, Context context, Freezer_inv fragment) {
        this.grupos = grupos;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new GrupoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
        Grupo grupo = grupos.get(position);

        Log.d("GrupoAdapter", "onBindViewHolder - Posición: " + position +
                ", ID: " + grupo.getIdGrupo() +
                ", Nombre: " + grupo.getNombreGrupo() +
                ", Imagen: " + grupo.getImagenGrupo());

        holder.textFoodName.setText(grupo.getNombreGrupo());

        // Recuperamos el nombre exacto de la imagen asociada
        String resourceName = grupo.getImagenGrupo().trim();
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        Log.d("GrupoAdapter", "Cargando imagen: " + resourceName + ", ResourceId: " + resourceId);

        // Limpia cualquier imagen previa para evitar reciclaje incorrecto
        Glide.with(context).clear(holder.imageFood);

        // Configuramos Glide para cargar la imagen correctamente
        Glide.with(context)
                .load(resourceId != 0 ? resourceId : R.drawable.default_image)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .skipMemoryCache(true)  // Desactiva caché de memoria para depuración
                        .diskCacheStrategy(DiskCacheStrategy.NONE))  // Desactiva caché de disco
                .into(holder.imageFood);

        // Configuración de clic en el CardView
        holder.cardView.setOnClickListener(v -> fragment.loadTiposAlimentos(grupo.getIdGrupo()));
    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    public static class GrupoViewHolder extends RecyclerView.ViewHolder {

        TextView textFoodName;
        ImageView imageFood;
        CardView cardView;

        public GrupoViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            imageFood = itemView.findViewById(R.id.image_food);
            cardView = itemView.findViewById(R.id.cardViewadd);
        }
    }
}