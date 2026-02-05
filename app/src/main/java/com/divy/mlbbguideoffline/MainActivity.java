package com.divy.mlbbguideoffline;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        // Apply Color Scheme
        int colorScheme = sharedPreferences.getInt("color_scheme", 0);
        switch (colorScheme) {
            case 1:
                setTheme(R.style.Theme_MlbbGuideOffline_Blue);
                break;
            case 2:
                setTheme(R.style.Theme_MlbbGuideOffline_Green);
                break;
            case 3:
                setTheme(R.style.Theme_MlbbGuideOffline_Indigo);
                break;
            case 4:
                setTheme(R.style.Theme_MlbbGuideOffline_Teal);
                break;
            case 5:
                setTheme(R.style.Theme_MlbbGuideOffline_Yellow);
                break;
            case 6:
                setTheme(R.style.Theme_MlbbGuideOffline_Orange);
                break;
            case 7:
                setTheme(R.style.Theme_MlbbGuideOffline_DeepOrange);
                break;
            case 8:
                setTheme(R.style.Theme_MlbbGuideOffline_Pink);
                break;
            default:
                // Default is Purple, no need to set explicitly if it's the manifest theme
                // But if we want to be sure:
                // setTheme(R.style.Theme_MlbbGuideOffline);
                break;
        }

        int themeMode = sharedPreferences.getInt("theme_mode", 0);
        int mode;
        switch (themeMode) {
            case 1:
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case 2:
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(mode);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(
                R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            androidx.fragment.app.Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_heroes) {
                selectedFragment = new HeroesFragment();
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Set default selection
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}