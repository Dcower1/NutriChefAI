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

public IngredienteAdapter(List<Food> ingredienteList, Context context) {
    this.ingredienteList = ingredienteList;
    this.context = context;
}

@NonNull
@Override
public IngredienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
    return new IngredienteViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull IngredienteViewHolder holder, int position) {
    Food food = ingredienteList.get(position);

    int imageResId = context.getResources().getIdentifier(food.getImageName(), "drawable", context.getPackageName());
    holder.ingredienteImage.setImageResource(imageResId);
    holder.ingredienteName.setText(food.getName());
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