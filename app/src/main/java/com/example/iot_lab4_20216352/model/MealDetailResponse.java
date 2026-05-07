package com.example.iot_lab4_20216352.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MealDetailResponse {
    @SerializedName("meals")
    private List<MealDetail> meals;

    public List<MealDetail> getMeals() { return meals; }

    public MealDetail getFirstMeal() {
        if (meals != null && !meals.isEmpty()) return meals.get(0);
        return null;
    }
}
