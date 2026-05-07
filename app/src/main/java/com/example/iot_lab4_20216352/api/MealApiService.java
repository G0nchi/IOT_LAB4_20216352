package com.example.iot_lab4_20216352.api;

import com.example.iot_lab4_20216352.model.CategoryResponse;
import com.example.iot_lab4_20216352.model.MealDetailResponse;
import com.example.iot_lab4_20216352.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {

    // GET 1 - Todas las categorías
    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    // GET 2A - Filtrar platos por categoría
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    // GET 2B - Filtrar platos por ingrediente
    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    // GET 3 - Detalle de un plato por ID
    @GET("lookup.php")
    Call<MealDetailResponse> getMealById(@Query("i") String mealId);

    // GET 4 - Plato aleatorio
    @GET("random.php")
    Call<MealDetailResponse> getRandomMeal();
}
