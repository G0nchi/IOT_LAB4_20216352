package com.example.iot_lab4_20216352.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoryResponse {
    @SerializedName("categories")
    private List<Category> categories;

    public List<Category> getCategories() { return categories; }
}
