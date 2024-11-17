package com.example.nutrichefai.adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
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

        holder.textFoodName.setText(ingrediente.getNombre());

        int imageResourceId = context.getResources().getIdentifier(
                ingrediente.getImageName(), "drawable", context.getPackageName());

        if (imageResourceId != 0) {
            Glide.with(context).load(imageResourceId).into(holder.imageFood);
        } else {
            holder.imageFood.setImageResource(R.drawable.default_image);
        }

        // Configurar inicio de arrastre
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