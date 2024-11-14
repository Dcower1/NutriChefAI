package com.example.nutrichefai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.Food;

import java.util.List;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder> {
    private List<Food> ingredienteList;
    private Context context;
    private boolean isLargeScreen;

    public IngredienteAdapter(List<Food> ingredienteList, Context context, boolean isLargeScreen) {
        this.ingredienteList = ingredienteList;
        this.context = context;
        this.isLargeScreen = isLargeScreen;
    }

    @NonNull
    @Override
    public IngredienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new IngredienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredienteViewHolder holder, int position) {
        Food food = ingredienteList.get(position);

        int imageResId = context.getResources().getIdentifier(food.getImageName(), "drawable", context.getPackageName());
        holder.ingredienteImage.setImageResource(imageResId);
        holder.ingredienteName.setText(food.getName());

        // Ajustar el tamaño de la tarjeta en función del tamaño de pantalla
        if (!isLargeScreen) {
            holder.itemView.getLayoutParams().width = (int) (context.getResources().getDimension(R.dimen.card_width) * 0.8);
            holder.itemView.getLayoutParams().height = (int) (context.getResources().getDimension(R.dimen.card_height) * 0.8);
            holder.itemView.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return ingredienteList.size();
    }

    public static class IngredienteViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredienteImage;
        TextView ingredienteName;

        public IngredienteViewHolder(View itemView) {
            super(itemView);
            ingredienteImage = itemView.findViewById(R.id.image_food);
            ingredienteName = itemView.findViewById(R.id.text_food_name);
        }
    }
}