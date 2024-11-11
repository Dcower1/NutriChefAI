package com.example.nutrichefai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.Grupo;

import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder> {
    private List<Grupo> grupoList;
    private Context context;
    private OnGrupoClickListener onGrupoClickListener;

    public interface OnGrupoClickListener {
        void onGrupoClick(int grupoId);

    }

    public GrupoAdapter(List<Grupo> grupoList, Context context, OnGrupoClickListener onGrupoClickListener) {
        this.grupoList = grupoList;
        this.context = context;

        this.onGrupoClickListener = onGrupoClickListener;

    }

    @Override
    public GrupoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new GrupoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GrupoViewHolder holder, int position) {
        Grupo grupo = grupoList.get(position);

        int imageResId = context.getResources().getIdentifier(grupo.getImageName(), "drawable", context.getPackageName());
        holder.grupoImage.setImageResource(imageResId);
        holder.grupoName.setText(grupo.getName());

        holder.itemView.setOnClickListener(v -> onGrupoClickListener.onGrupoClick(grupo.getId()));
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