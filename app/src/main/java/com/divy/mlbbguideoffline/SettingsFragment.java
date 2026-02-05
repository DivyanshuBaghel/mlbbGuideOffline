package com.divy.mlbbguideoffline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment {

    private TextView tvCurrentTheme;
    private TextView tvCurrentColorScheme;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        tvCurrentTheme = view.findViewById(R.id.tv_current_theme);
        tvCurrentColorScheme = view.findViewById(R.id.tv_current_color_scheme);
        LinearLayout layoutTheme = view.findViewById(R.id.layout_theme);
        LinearLayout layoutColorScheme = view.findViewById(R.id.layout_color_scheme);

        updateThemeText();
        updateColorSchemeText();

        layoutTheme.setOnClickListener(v -> showThemeDialog());
        layoutColorScheme.setOnClickListener(v -> showColorSchemeDialog());
    }

    private void showThemeDialog() {
        String[] themes = { "System Default", "Light", "Dark" };
        int checkedItem = sharedPreferences.getInt("theme_mode", 0); // 0 corresponds to System Default

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select Theme")
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    sharedPreferences.edit().putInt("theme_mode", which).apply();
                    applyTheme(which);
                    updateThemeText();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showColorSchemeDialog() {
        String[] schemes = {
                "Purple (Baseline)", "Blue", "Green", "Indigo", "Teal", "Yellow", "Orange", "Deep Orange", "Pink"
        };
        int checkedItem = sharedPreferences.getInt("color_scheme", 0);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select Color Scheme")
                .setSingleChoiceItems(schemes, checkedItem, (dialog, which) -> {
                    sharedPreferences.edit().putInt("color_scheme", which).apply();
                    updateColorSchemeText();
                    dialog.dismiss();
                    requireActivity().recreate(); // Restart to apply theme
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateThemeText() {
        int themeMode = sharedPreferences.getInt("theme_mode", 0);
        String themeName;
        switch (themeMode) {
            case 1:
                themeName = "Light";
                break;
            case 2:
                themeName = "Dark";
                break;
            default:
                themeName = "System Default";
                break;
        }
        tvCurrentTheme.setText(themeName);
    }

    private void updateColorSchemeText() {
        int scheme = sharedPreferences.getInt("color_scheme", 0);
        String schemeName;
        switch (scheme) {
            case 1:
                schemeName = "Blue";
                break;
            case 2:
                schemeName = "Green";
                break;
            case 3:
                schemeName = "Indigo";
                break;
            case 4:
                schemeName = "Teal";
                break;
            case 5:
                schemeName = "Yellow";
                break;
            case 6:
                schemeName = "Orange";
                break;
            case 7:
                schemeName = "Deep Orange";
                break;
            case 8:
                schemeName = "Pink";
                break;
            default:
                schemeName = "Purple (Baseline)";
                break;
        }
        tvCurrentColorScheme.setText(schemeName);
    }

    private void applyTheme(int themeMode) {
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
    }
}
