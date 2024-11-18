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
        holder.foodQuantity.setText("Cantidad: " + ingrediente.getCantidad());

        int imageResourceId = context.getResources().getIdentifier(
                ingrediente.getImageName(), "drawable", context.getPackageName());
        holder.foodImage.setImageResource(imageResourceId != 0 ? imageResourceId : R.drawable.default_image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(ingrediente);
            }
        });
    }
    public void updateCantidad(int idIngrediente, String nuevaCantidad) {
        for (int i = 0; i < ingredientesList.size(); i++) {
            InventarioIngrediente ingrediente = ingredientesList.get(i);
            if (ingrediente.getId() == idIngrediente) {
                ingrediente.setCantidad(nuevaCantidad); // Actualiza la cantidad
                if (Integer.parseInt(nuevaCantidad) == 0) {
                    // Si la cantidad es 0, elimina el ingrediente de la lista
                    ingredientesList.remove(i);
                    notifyItemRemoved(i); // Notifica que el item ha sido eliminado
                    Log.d("InventarioAdapter", "Ingrediente eliminado debido a cantidad 0: " + ingrediente.getNombre());
                } else {
                    notifyItemChanged(i); // Notifica solo el cambio del elemento
                }
                break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return ingredientesList.size();
    }

    public static class InventarioViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName, foodQuantity;

        public InventarioViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_food_icon);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodQuantity = itemView.findViewById(R.id.text_food_quantity);
        }
    }
}