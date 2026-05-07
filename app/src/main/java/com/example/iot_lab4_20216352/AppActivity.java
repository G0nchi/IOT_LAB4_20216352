package com.example.iot_lab4_20216352;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// LLM: clase generada con asistencia de IA adaptada a los patrones usados en clase
public class AppActivity extends AppCompatActivity {

    private boolean isUpdatingBottomNav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Sync bottom nav selection when destination changes programmatically
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            isUpdatingBottomNav = true;
            int id = destination.getId();
            if (id == R.id.nav_categories) bottomNav.setSelectedItemId(R.id.nav_categories);
            else if (id == R.id.nav_meals) bottomNav.setSelectedItemId(R.id.nav_meals);
            else if (id == R.id.nav_recipe) bottomNav.setSelectedItemId(R.id.nav_recipe);
            isUpdatingBottomNav = false;
        });

        bottomNav.setOnItemSelectedListener(item -> {
            if (isUpdatingBottomNav) return true;
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_categories, false)
                    .setLaunchSingleTop(true)
                    .build();
            int id = item.getItemId();
            if (id == R.id.nav_categories) {
                navController.navigate(R.id.nav_categories, null, navOptions);
            } else if (id == R.id.nav_meals) {
                navController.navigate(R.id.nav_meals, null, navOptions);
            } else if (id == R.id.nav_recipe) {
                navController.navigate(R.id.nav_recipe, null, navOptions);
            }
            return true;
        });

        // Back press always returns to MainActivity regardless of fragment stack
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }
}
