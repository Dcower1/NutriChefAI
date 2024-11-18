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
import com.bumptech.glide.request.RequestOptions;
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

        // Verifica y registra los datos para depuración
        Log.d("TipoAlimentoAdapter", "onBindViewHolder - Posición: " + position +
                ", ID: " + tipo.getId() +
                ", Nombre: " + tipo.getName() +
                ", Imagen: " + tipo.getImageName());

        holder.textFoodName.setText(tipo.getName());

        // Recupera el nombre exacto de la imagen asociada
        String resourceName = tipo.getImageName().trim();
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        Log.d("TipoAlimentoAdapter", "Cargando imagen: " + resourceName + ", ResourceId: " + resourceId);

        // Limpia cualquier imagen previa para evitar problemas de reciclaje
        Glide.with(context).clear(holder.imageFood);

        // Configura Glide para cargar la imagen correctamente
        Glide.with(context)
                .load(resourceId != 0 ? resourceId : R.drawable.default_image)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_image)  // Imagen por defecto mientras carga
                        .error(R.drawable.default_image)        // Imagen por defecto si hay error
                        .skipMemoryCache(true)                  // Desactiva caché de memoria para depuración
                        .diskCacheStrategy(DiskCacheStrategy.NONE))  // Desactiva caché de disco
                .into(holder.imageFood);

        // Configura el evento de clic para cargar ingredientes
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