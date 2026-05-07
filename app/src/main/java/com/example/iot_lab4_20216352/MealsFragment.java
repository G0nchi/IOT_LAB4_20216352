package com.example.iot_lab4_20216352;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab4_20216352.adapter.MealAdapter;
import com.example.iot_lab4_20216352.api.ApiClient;
import com.example.iot_lab4_20216352.model.Meal;
import com.example.iot_lab4_20216352.model.MealDetail;
import com.example.iot_lab4_20216352.model.MealDetailResponse;
import com.example.iot_lab4_20216352.model.MealResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class MealsFragment extends Fragment implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 4.0f;
    private static final long SHAKE_COOLDOWN_MS = 2000;

    private RecyclerView recyclerMeals;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private LinearLayout layoutSearch;
    private TextView tvCategoryLabel;
    private TextInputEditText etIngredient;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime = 0;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        recyclerMeals = view.findViewById(R.id.recyclerMeals);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        tvCategoryLabel = view.findViewById(R.id.tvCategoryLabel);
        etIngredient = view.findViewById(R.id.etIngredient);

        recyclerMeals.setLayoutManager(new LinearLayoutManager(getContext()));

        sensorManager = (SensorManager) requireActivity().getSystemService(android.content.Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Bundle args = getArguments();
        if (args != null && args.containsKey("categoryName")) {
            String categoryName = args.getString("categoryName");
            layoutSearch.setVisibility(View.GONE);
            tvCategoryLabel.setVisibility(View.VISIBLE);
            tvCategoryLabel.setText(getString(R.string.categories) + ": " + categoryName);
            fetchByCategory(categoryName);
        } else {
            layoutSearch.setVisibility(View.VISIBLE);
            tvCategoryLabel.setVisibility(View.GONE);
        }

        view.findViewById(R.id.btnBuscar).setOnClickListener(v -> searchByIngredient());
        etIngredient.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchByIngredient();
                return true;
            }
            return false;
        });
    }

    private void searchByIngredient() {
        String ingredient = "";
        if (etIngredient.getText() != null) {
            ingredient = etIngredient.getText().toString().trim();
        }
        if (ingredient.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese un ingrediente", Toast.LENGTH_SHORT).show();
            return;
        }
        fetchByIngredient(ingredient);
    }

    private void fetchByCategory(String category) {
        showLoading();
        ApiClient.getService().getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call,
                                   @NonNull Response<MealResponse> response) {
                handleMealResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                handleFailure();
            }
        });
    }

    private void fetchByIngredient(String ingredient) {
        showLoading();
        ApiClient.getService().getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call,
                                   @NonNull Response<MealResponse> response) {
                handleMealResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                handleFailure();
            }
        });
    }

    private void handleMealResponse(Response<MealResponse> response) {
        progressBar.setVisibility(View.GONE);
        if (response.isSuccessful() && response.body() != null) {
            List<Meal> meals = response.body().getMeals();
            if (meals != null && !meals.isEmpty()) {
                MealAdapter adapter = new MealAdapter(meals, meal -> {
                    Bundle args = new Bundle();
                    args.putString("mealId", meal.getIdMeal());
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_categories, false)
                            .setLaunchSingleTop(true)
                            .build();
                    Navigation.findNavController(rootView)
                            .navigate(R.id.nav_recipe, args, navOptions);
                });
                recyclerMeals.setAdapter(adapter);
                recyclerMeals.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            } else {
                showEmpty();
            }
        } else {
            showEmpty();
        }
    }

    private void handleFailure() {
        progressBar.setVisibility(View.GONE);
        showEmpty();
        Toast.makeText(getContext(), getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerMeals.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    private void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
        recyclerMeals.setVisibility(View.GONE);
    }

    // --- Accelerometer (Sensor Q2) ---

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double magnitude = Math.sqrt(x * x + y * y + z * z);
        double netAcceleration = Math.abs(magnitude - SensorManager.GRAVITY_EARTH);
        if (netAcceleration > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (now - lastShakeTime > SHAKE_COOLDOWN_MS) {
                lastShakeTime = now;
                fetchRandomMeal();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { /* no-op */ }

    private void fetchRandomMeal() {
        ApiClient.getService().getRandomMeal().enqueue(new Callback<MealDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealDetailResponse> call,
                                   @NonNull Response<MealDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealDetail meal = response.body().getFirstMeal();
                    if (meal != null && isAdded()) {
                        showSurpriseDialog(meal);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealDetailResponse> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSurpriseDialog(MealDetail meal) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.surprise_meal))
                .setMessage(meal.getStrMeal() + "\n\n"
                        + getString(R.string.category_label) + ": " + meal.getStrCategory() + "\n"
                        + getString(R.string.area) + ": " + meal.getStrArea())
                .setPositiveButton(getString(R.string.ver_receta), (dialog, which) -> {
                    Bundle args = new Bundle();
                    args.putString("mealId", meal.getIdMeal());
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_categories, false)
                            .setLaunchSingleTop(true)
                            .build();
                    Navigation.findNavController(rootView)
                            .navigate(R.id.nav_recipe, args, navOptions);
                })
                .setNegativeButton(getString(R.string.cerrar), null)
                .show();
    }
}
