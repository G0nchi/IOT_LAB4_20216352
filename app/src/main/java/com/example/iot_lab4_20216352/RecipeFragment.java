package com.example.iot_lab4_20216352;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.iot_lab4_20216352.api.ApiClient;
import com.example.iot_lab4_20216352.model.MealDetail;
import com.example.iot_lab4_20216352.model.MealDetailResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class RecipeFragment extends Fragment {

    private TextInputEditText etMealId;
    private ProgressBar progressBar;
    private ScrollView scrollContent;
    private ImageView ivMealThumb;
    private TextView tvMealName, tvCategory, tvArea, tvIngredients, tvInstructions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMealId = view.findViewById(R.id.etMealId);
        progressBar = view.findViewById(R.id.progressBar);
        scrollContent = view.findViewById(R.id.scrollContent);
        ivMealThumb = view.findViewById(R.id.ivMealThumb);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvArea = view.findViewById(R.id.tvArea);
        tvIngredients = view.findViewById(R.id.tvIngredients);
        tvInstructions = view.findViewById(R.id.tvInstructions);

        view.findViewById(R.id.btnSearch).setOnClickListener(v -> searchRecipe());
        etMealId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchRecipe();
                return true;
            }
            return false;
        });

        // Auto-load if navigated from MealsFragment
        Bundle args = getArguments();
        if (args != null && args.containsKey("mealId")) {
            String mealId = args.getString("mealId");
            etMealId.setText(mealId);
            fetchRecipe(mealId);
        }
    }

    private void searchRecipe() {
        String mealId = "";
        if (etMealId.getText() != null) {
            mealId = etMealId.getText().toString().trim();
        }
        if (mealId.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.search_meal_id), Toast.LENGTH_SHORT).show();
            return;
        }
        fetchRecipe(mealId);
    }

    private void fetchRecipe(String mealId) {
        progressBar.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.GONE);

        ApiClient.getService().getMealById(mealId).enqueue(new Callback<MealDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealDetailResponse> call,
                                   @NonNull Response<MealDetailResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    MealDetail meal = response.body().getFirstMeal();
                    if (meal != null) {
                        displayRecipe(meal);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.no_results), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealDetailResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRecipe(MealDetail meal) {
        tvMealName.setText(meal.getStrMeal());
        tvCategory.setText(meal.getStrCategory());
        tvArea.setText(meal.getStrArea());
        tvIngredients.setText(meal.getFormattedIngredients());
        tvInstructions.setText(meal.getStrInstructions());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(ivMealThumb);

        scrollContent.setVisibility(View.VISIBLE);
        scrollContent.scrollTo(0, 0);
    }
}
