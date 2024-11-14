package com.example.nutrichefai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrichefai.R;
import com.example.nutrichefai.bd.Food;

import java.util.Collections;
import java.util.List;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder> {
    private List<Food> ingredienteList;
    private Context context;
    private boolean isLargeScreen;
    private final ItemTouchHelper itemTouchHelper;

    // Constructor actualizado para recibir ItemTouchHelper
    public IngredienteAdapter(List<Food> ingredienteList, Context context, boolean isLargeScreen, ItemTouchHelper itemTouchHelper) {
        this.ingredienteList = ingredienteList;
        this.context = context;
        this.isLargeScreen = isLargeScreen;
        this.itemTouchHelper = itemTouchHelper;
    }

    // Callback para el arrastre https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper
    public ItemTouchHelper.Callback getItemTouchHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                // Actualizar la lista y notificar el cambio
                Collections.swap(ingredienteList, fromPosition, toPosition);
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // No hacemos nada en el swipe
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                    viewHolder.itemView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start();
                    viewHolder.itemView.setAlpha(0.9f);
                } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
                    viewHolder.itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();
                    viewHolder.itemView.setAlpha(1.0f);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1.0f);
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
        };
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

        if (!isLargeScreen) {
            holder.itemView.getLayoutParams().width = (int) (context.getResources().getDimension(R.dimen.card_width) * 0.8);
            holder.itemView.getLayoutParams().height = (int) (context.getResources().getDimension(R.dimen.card_height) * 0.8);
            holder.itemView.requestLayout();
        }

        // Detectar el movimiento de arrastre
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private float initialY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getY();
                        if (initialY - currentY > 20) {
                            itemTouchHelper.startDrag(holder);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        initialY = 0;
                        break;
                }
                return false;
            }
        });
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