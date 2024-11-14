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
import com.example.nutrichefai.bd.Grupo;

import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder> {
    private List<Grupo> grupoList;
    private Context context;
    private boolean isLargeScreen;
    private OnGrupoClickListener onGrupoClickListener;

    public interface OnGrupoClickListener {
        void onGrupoClick(int grupoId);
    }

    public GrupoAdapter(List<Grupo> grupoList, Context context, boolean isLargeScreen, OnGrupoClickListener listener) {
        this.grupoList = grupoList;
        this.context = context;
        this.isLargeScreen = isLargeScreen;
        this.onGrupoClickListener = listener;
    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new GrupoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
        Grupo grupo = grupoList.get(position);

        int imageResId = context.getResources().getIdentifier(grupo.getImageName(), "drawable", context.getPackageName());
        holder.grupoImage.setImageResource(imageResId);
        holder.grupoName.setText(grupo.getName());

        holder.itemView.setOnClickListener(v -> onGrupoClickListener.onGrupoClick(grupo.getId()));

        // Ajustar el tamaño de la tarjeta en función del tamaño de pantalla
        if (!isLargeScreen) {
            holder.itemView.getLayoutParams().width = (int) (context.getResources().getDimension(R.dimen.card_width) * 0.8);
            holder.itemView.getLayoutParams().height = (int) (context.getResources().getDimension(R.dimen.card_height) * 0.8);
            holder.itemView.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return grupoList.size();
    }

    public static class GrupoViewHolder extends RecyclerView.ViewHolder {
        ImageView grupoImage;
        TextView grupoName;

        public GrupoViewHolder(View itemView) {
            super(itemView);
            grupoImage = itemView.findViewById(R.id.image_food);
            grupoName = itemView.findViewById(R.id.text_food_name);
        }
    }
}