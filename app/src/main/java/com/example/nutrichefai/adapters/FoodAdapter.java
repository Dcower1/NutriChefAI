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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private List<Food> ingredientsList;
    private Context context;

    public FoodAdapter(List<Food> ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Food food = ingredientsList.get(position);


        String imageName = food.getImageName();
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.foodImage.setImageResource(imageResId);


        holder.foodName.setText(food.getName());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    // ViewHolder para el carrusel
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_food);
            foodName = itemView.findViewById(R.id.text_food_name);
        }
    }
}