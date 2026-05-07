package com.example.iot_lab4_20216352;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab4_20216352.adapter.CategoryAdapter;
import com.example.iot_lab4_20216352.api.ApiClient;
import com.example.iot_lab4_20216352.model.Category;
import com.example.iot_lab4_20216352.model.CategoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerCategories;
    private ProgressBar progressBar;
    private TextView tvError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerCategories = view.findViewById(R.id.recyclerCategories);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);

        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCategories(view);
    }

    private void loadCategories(View view) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerCategories.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        ApiClient.getService().getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call,
                                   @NonNull Response<CategoryResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getCategories();
                    if (categories != null && !categories.isEmpty()) {
                        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
                            Bundle args = new Bundle();
                            args.putString("categoryName", category.getStrCategory());
                            NavOptions navOptions = new NavOptions.Builder()
                                    .setPopUpTo(R.id.nav_categories, false)
                                    .setLaunchSingleTop(true)
                                    .build();
                            Navigation.findNavController(view)
                                    .navigate(R.id.nav_meals, args, navOptions);
                        });
                        recyclerCategories.setAdapter(adapter);
                        recyclerCategories.setVisibility(View.VISIBLE);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError();
                Toast.makeText(getContext(), getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showError() {
        tvError.setVisibility(View.VISIBLE);
        recyclerCategories.setVisibility(View.GONE);
    }
}
