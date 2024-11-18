package com.example.nutrichefai.adapters;

import android.content.Context;
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
import com.example.nutrichefai.bd.InventarioIngrediente;

import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.InventarioViewHolder> {
    private List<InventarioIngrediente> ingredientesList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(InventarioIngrediente ingrediente);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InventarioAdapter(List<InventarioIngrediente> ingredientesList, Context context) {
        this.ingredientesList = ingredientesList;
        this.context = context;
    }

    @NonNull
    @Override
    public InventarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_ingrediente, parent, false);
        return new InventarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventarioViewHolder holder, int position) {
        InventarioIngrediente ingrediente = ingredientesList.get(position);

        holder.foodName.setText(ingrediente.getNombre());
        holder.quantity.setText("Cantidad: " + ingrediente.getCantidad());

        int imageResourceId = context.getResources().getIdentifier(
                ingrediente.getImageName(), "drawable", context.getPackageName());
        holder.foodImage.setImageResource(imageResourceId != 0 ? imageResourceId : R.drawable.default_image);

        // Configurar el clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(ingrediente);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientesList != null ? ingredientesList.size() : 0;
    }

    public static class InventarioViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, quantity;
        ImageView foodImage;

        public InventarioViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_ingredient_name);
            quantity = itemView.findViewById(R.id.text_ingredient_quantity);
            foodImage = itemView.findViewById(R.id.image_ingredient);
        }
    }
}