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
import com.example.iot_lab4_20216352.model.Category;

import java.util.List;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private final List<Category> categories;
    private final OnCategoryClickListener listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getStrCategory());
        holder.tvCategoryDescription.setText(category.getStrCategoryDescription());
        Glide.with(holder.itemView.getContext())
                .load(category.getStrCategoryThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivCategoryThumb);
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() { return categories.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryThumb;
        TextView tvCategoryName, tvCategoryDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryThumb = itemView.findViewById(R.id.ivCategoryThumb);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryDescription = itemView.findViewById(R.id.tvCategoryDescription);
        }
    }
}
