package com.example.nutrichefai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.TipoAlimento;

import java.util.List;

public class TipoAlimentoAdapter extends RecyclerView.Adapter<TipoAlimentoAdapter.TipoAlimentoViewHolder> {
private List<TipoAlimento> tipoAlimentoList;
private Context context;
private OnTipoAlimentoClickListener onTipoAlimentoClickListener;

public interface OnTipoAlimentoClickListener {
    void onTipoAlimentoClick(int tipoAlimentoId);
}

public TipoAlimentoAdapter(List<TipoAlimento> tipoAlimentoList, Context context, OnTipoAlimentoClickListener listener) {
    this.tipoAlimentoList = tipoAlimentoList;
    this.context = context;
    this.onTipoAlimentoClickListener = listener;
}

@Override
public TipoAlimentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
    return new TipoAlimentoViewHolder(view);
}

@Override
public void onBindViewHolder(TipoAlimentoViewHolder holder, int position) {
    TipoAlimento tipoAlimento = tipoAlimentoList.get(position);

    int imageResId = context.getResources().getIdentifier(tipoAlimento.getImageName(), "drawable", context.getPackageName());
    holder.tipoImage.setImageResource(imageResId);
    holder.tipoName.setText(tipoAlimento.getName());

    holder.itemView.setOnClickListener(v -> onTipoAlimentoClickListener.onTipoAlimentoClick(tipoAlimento.getId()));
}

@Override
public int getItemCount() {
    return tipoAlimentoList.size();
}

public static class TipoAlimentoViewHolder extends RecyclerView.ViewHolder {
    ImageView tipoImage;
    TextView tipoName;

    public TipoAlimentoViewHolder(View itemView) {
        super(itemView);
        tipoImage = itemView.findViewById(R.id.image_food);
        tipoName = itemView.findViewById(R.id.text_food_name);
    }
}
}