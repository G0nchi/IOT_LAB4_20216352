package com.example.iot_lab4_20216352.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iot_lab4_20216352.R;
import com.example.iot_lab4_20216352.model.Meal;

import java.util.List;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    private final List<Meal> meals;
    private final OnMealClickListener listener;

    public MealAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvMealName.setText(meal.getStrMeal());
        holder.tvMealId.setText("ID: " + meal.getIdMeal());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivMealThumb);
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() { return meals.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealThumb;
        TextView tvMealName, tvMealId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealThumb = itemView.findViewById(R.id.ivMealThumb);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealId = itemView.findViewById(R.id.tvMealId);
        }
    }
}
