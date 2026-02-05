package com.divy.mlbbguideoffline;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.divy.mlbbguideoffline.adapter.HeroAdapter;
import com.divy.mlbbguideoffline.model.Hero;
import com.divy.mlbbguideoffline.model.HeroResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HeroesFragment extends Fragment {

    private RecyclerView rvHeroes;
    private HeroAdapter heroAdapter;
    private List<Hero> heroList = new ArrayList<>();
    private List<Hero> originalHeroList = new ArrayList<>();
    private com.google.android.material.chip.ChipGroup chipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_heroes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHeroes = view.findViewById(R.id.rv_heroes);
        chipGroup = view.findViewById(R.id.chip_group_categories);

        loadHeroesFromAssets();

        heroAdapter = new HeroAdapter(requireContext(), heroList);
        rvHeroes.setAdapter(heroAdapter);

        setupChips();
    }

    private void setupChips() {
        String[] categories = { "All", "Tank", "Fighter", "Assassin", "Mage", "Marksman", "Support" };

        for (String category : categories) {
            com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);

            // Set "All" as default checked
            if (category.equals("All")) {
                chip.setChecked(true);
            }

            chipGroup.addView(chip);
        }

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                // If nothing selected (unselected current), re-select All or just show All
                filterHeroes("All");
                return;
            }
            // Single selection, so get the first one
            int id = checkedIds.get(0);
            com.google.android.material.chip.Chip chip = group.findViewById(id);
            if (chip != null) {
                filterHeroes(chip.getText().toString());
            }
        });
    }

    private void filterHeroes(String category) {
        if (category.equals("All")) {
            heroAdapter.updateList(new ArrayList<>(originalHeroList));
            return;
        }

        List<Hero> filteredList = new ArrayList<>();
        for (Hero hero : originalHeroList) {
            // Null check just in case. Case insensitive check might be safer but JSON
            // implies proper casing.
            if (hero.getHeroClass() != null && hero.getHeroClass().contains(category)) {
                filteredList.add(hero);
            }
        }
        heroAdapter.updateList(filteredList);
    }

    private void loadHeroesFromAssets() {
        try {
            InputStream is = requireContext().getAssets().open("hero-meta-final.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            HeroResponse response = gson.fromJson(json, HeroResponse.class);
            if (response != null && response.getData() != null) {
                heroList.clear();
                heroList.addAll(response.getData());
                originalHeroList.addAll(response.getData());
            }

        } catch (IOException e) {
            Log.e("HeroesFragment", "Error loading JSON", e);
        }
    }
}
