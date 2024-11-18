package com.example.nutrichefai.adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.Ingrediente;

import java.util.List;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder> {
    private List<Ingrediente> ingredientes;
    private Context context;

    public IngredienteAdapter(List<Ingrediente> ingredientes, Context context) {
        this.ingredientes = ingredientes;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new IngredienteViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull IngredienteViewHolder holder, int position) {
        Ingrediente ingrediente = ingredientes.get(position);

        // Logs para verificar datos
        Log.d("IngredienteAdapter", "onBindViewHolder - Posición: " + position +
                ", ID: " + ingrediente.getId() +
                ", Nombre: " + ingrediente.getNombre() +
                ", Imagen: " + ingrediente.getImageName());

        holder.textFoodName.setText(ingrediente.getNombre());

        // Recupera el nombre exacto de la imagen asociada
        String imageName = ingrediente.getImageName().trim();
        int imageResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        Log.d("IngredienteAdapter", "Cargando imagen: " + imageName + ", ResourceId: " + imageResourceId);

        // Limpia cualquier imagen previa antes de cargar la nueva
        Glide.with(context).clear(holder.imageFood);

        // Configura Glide para cargar correctamente la imagen
        Glide.with(context)
                .load(imageResourceId != 0 ? imageResourceId : R.drawable.default_image)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_image)  // Imagen por defecto mientras carga
                        .error(R.drawable.default_image)        // Imagen por defecto si hay error
                        .skipMemoryCache(true)                  // Desactiva caché de memoria para depuración
                        .diskCacheStrategy(DiskCacheStrategy.NONE))  // Desactiva caché de disco
                .into(holder.imageFood);

        // Configurar evento de arrastre
        holder.cardView.setOnLongClickListener(v -> {
            // Crear datos del arrastre
            ClipData data = ClipData.newPlainText("id_ingrediente", String.valueOf(ingrediente.getId()));

            // Crear la sombra personalizada
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(holder.cardView);

            // Iniciar el evento de arrastre
            v.startDragAndDrop(data, shadowBuilder, null, 0);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public static class IngredienteViewHolder extends RecyclerView.ViewHolder {

        TextView textFoodName;
        ImageView imageFood;
        CardView cardView;

        public IngredienteViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            imageFood = itemView.findViewById(R.id.image_food);
            cardView = itemView.findViewById(R.id.cardViewadd);
        }
    }
}