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

        // Establecer el nombre del grupo
        holder.textFoodName.setText(grupo.getNombreGrupo());

        // Construir el nombre completo del recurso (con extensión .webp)
        String imageName = grupo.getImagenGrupo(); // Ejemplo: img1
        int imageResourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        // Verificar si el recurso existe
        if (imageResourceId != 0) {
            Log.d("ImageResource", "Loading resource: " + imageName + " with ID: " + imageResourceId);
            Glide.with(context)
                    .load(imageResourceId)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Evitar caché en pruebas
                    .skipMemoryCache(true) // Evitar caché en memoria
                    .into(holder.imageFood);
        } else {
            Log.e("ImageResource", "Image not found for: " + imageName + ". Using default image.");
            holder.imageFood.setImageResource(R.drawable.default_image); // Asegúrate de tener una imagen predeterminada
        }

        // Establecer el evento de clic en el CardView
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