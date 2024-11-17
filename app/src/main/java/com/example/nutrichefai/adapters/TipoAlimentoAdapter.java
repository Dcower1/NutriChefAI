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

        int imageResourceId = context.getResources().getIdentifier(
                tipo.getImageName(), "drawable", context.getPackageName());

        if (imageResourceId != 0) {
            Glide.with(context).load(imageResourceId).into(holder.imageFood);
        } else {
            holder.imageFood.setImageResource(R.drawable.default_image);
        }

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