package com.example.iot_lab4_20216352.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MealResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() { return meals; }
}
