package com.example.nutrichefai.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.TipoAlimento;
import com.example.nutrichefai.fragments.freezer.Freezer_inv;

import java.util.List;

public class TipoAlimentoAdapter extends RecyclerView.Adapter<TipoAlimentoAdapter.TipoAlimentoViewHolder> {
    private List<TipoAlimento> tipoAlimentos;
    private Context context;
    private Freezer_inv fragment;

    public TipoAlimentoAdapter(List<TipoAlimento> tipoAlimentos, Context context, Freezer_inv fragment) {
        this.tipoAlimentos = tipoAlimentos;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TipoAlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new TipoAlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipoAlimentoViewHolder holder, int position) {
        TipoAlimento tipo = tipoAlimentos.get(position);

        holder.textFoodName.setText(tipo.getName());

        // Construir el identificador de la imagen
        String imageName = tipo.getImageName(); // Nombre esperado en la base de datos (ej. "img1")
        int imageResourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        // Validar el recurso antes de cargar
        if (imageResourceId != 0) {
            Log.d("ImageResource", "Cargando recurso: " + imageName + " con ID: " + imageResourceId);
            Glide.with(context)
                    .load(imageResourceId)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Evitar caché en pruebas
                    .skipMemoryCache(true) // Evitar caché en memoria
                    .into(holder.imageFood);
        } else {
            Log.e("ImageResource", "Imagen no encontrada para: " + imageName + ". Usando imagen predeterminada.");
            holder.imageFood.setImageResource(R.drawable.default_image); // Imagen predeterminada
        }

        // Evento de clic en el elemento
        holder.itemView.setOnClickListener(v -> fragment.loadIngredientes(tipo.getId()));
    }


    @Override
    public int getItemCount() {
        return tipoAlimentos.size();
    }

    public static class TipoAlimentoViewHolder extends RecyclerView.ViewHolder {

        TextView textFoodName;
        ImageView imageFood;

        public TipoAlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            imageFood = itemView.findViewById(R.id.image_food);
        }
    }
}