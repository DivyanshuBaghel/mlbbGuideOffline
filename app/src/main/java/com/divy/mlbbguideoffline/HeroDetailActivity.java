package com.divy.mlbbguideoffline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.divy.mlbbguideoffline.model.Hero;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.divy.mlbbguideoffline.model.HeroResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HeroDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HERO = "extra_hero";
    private Map<String, String> heroImageMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply Theme and Color Scheme
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("app_prefs",
                android.content.Context.MODE_PRIVATE);
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
                break; // Default is Purple
        }

        int themeMode = sharedPreferences.getInt("theme_mode", 0);
        int mode;
        switch (themeMode) {
            case 1:
                mode = androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case 2:
                mode = androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                mode = androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(mode);

        setContentView(R.layout.activity_hero_detail);

        Hero hero = getIntent().getParcelableExtra(EXTRA_HERO);
        if (hero == null) {
            finish();
            return;
        }

        loadHeroImageMapSync();
        setupToolbar(hero.getHeroName());
        bindHeroData(hero);
    }

    private void loadHeroImageMap() {
        new Thread(() -> {
            try {
                InputStream is = getAssets().open("hero-meta-final.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, StandardCharsets.UTF_8);

                Gson gson = new Gson();
                HeroResponse response = gson.fromJson(json, HeroResponse.class);
                if (response != null && response.getData() != null) {
                    for (Hero h : response.getData()) {
                        if (h.mlid != null && h.portraitUrl != null) {
                            heroImageMap.put(h.mlid, h.portraitUrl);
                        }
                    }
                    // Since populate is called in bindHeroData on main thread, we might need to
                    // refresh or
                    // move bindHeroData call. But for simplicity, we can load map on main thread or
                    // just
                    // populate list items when map is ready.
                    // Given the file size, main thread load is acceptable for now to avoid
                    // complexity of callbacks.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        // Note: Threading might cause race condition if binding happens before map is
        // ready.
        // Changing to synchronous load for simplicity as user requested "offline" guide
        // and file is local.
    }

    // Synchronous version for stability as file is local asset
    private void loadHeroImageMapSync() {
        try {
            InputStream is = getAssets().open("hero-meta-final.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            HeroResponse response = gson.fromJson(json, HeroResponse.class);
            if (response != null && response.getData() != null) {
                for (Hero h : response.getData()) {
                    if (h.mlid != null && h.portraitUrl != null) {
                        heroImageMap.put(h.mlid, h.portraitUrl);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar(String heroName) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(heroName);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        CollapsingToolbarLayout ctl = findViewById(R.id.toolbar_layout);
        ctl.setTitle(heroName);
    }

    private void bindHeroData(Hero hero) {
        // Ensure map is loaded if we used the sync method
        if (heroImageMap.isEmpty())
            loadHeroImageMapSync();

        ImageView ivPortrait = findViewById(R.id.iv_detail_portrait);
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvClass = findViewById(R.id.tv_detail_class);
        ChipGroup cgLaning = findViewById(R.id.chip_group_laning);
        LinearLayout llSkills = findViewById(R.id.ll_skills_container);
        LinearLayout llCounters = findViewById(R.id.ll_counters_container);
        LinearLayout llSynergies = findViewById(R.id.ll_synergies_container);

        // Header
        tvName.setText(hero.getHeroName());
        tvClass.setText(hero.getHeroClass());

        if (hero.getPortraitUrl() != null && !hero.getPortraitUrl().isEmpty()) {
            Glide.with(this)
                    .load(hero.getPortraitUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPortrait);
        }

        // Laning Chips
        if (hero.laning != null) {
            for (String lane : hero.laning) {
                if (!lane.isEmpty()) {
                    addChip(cgLaning, lane);
                }
            }
        }

        // Speciality Chips
        ChipGroup cgSpeciality = findViewById(R.id.chip_group_speciality);
        if (hero.speciality != null) {
            for (String spec : hero.speciality) {
                if (!spec.isEmpty()) {
                    addChip(cgSpeciality, spec);
                }
            }
        }

        // Skills
        if (hero.skills != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Hero.Skill skill : hero.skills) {
                View skillView = inflater.inflate(R.layout.item_skill_detail, llSkills, false);

                TextView name = skillView.findViewById(R.id.tv_skill_name);
                TextView type = skillView.findViewById(R.id.tv_skill_type);
                TextView cd = skillView.findViewById(R.id.tv_skill_cd);
                TextView desc = skillView.findViewById(R.id.tv_skill_desc);
                // ImageView icon = skillView.findViewById(R.id.iv_skill_icon); // Icon logic if
                // URLs exist

                name.setText(skill.skillName);
                if (skill.type != null)
                    type.setText("Type: " + skill.type);
                if (skill.cooldown != null && !skill.cooldown.equals("null"))
                    cd.setText("CD: " + skill.cooldown);
                else
                    cd.setText("");
                desc.setText(skill.description);

                llSkills.addView(skillView);
            }
        }

        // Counters
        if (hero.counters != null) {
            for (Hero.Counter counter : hero.counters) {
                addHeroItem(llCounters, counter.heroId, counter.heroName);
            }
        }

        // Synergies
        if (hero.synergies != null) {
            for (Hero.Synergy synergy : hero.synergies) {
                addHeroItem(llSynergies, synergy.heroId, synergy.heroName);
            }
        }
    }

    private void addChip(ChipGroup group, String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setClickable(false);
        group.addView(chip);
    }

    private void addHeroItem(LinearLayout container, int heroId, String heroName) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_hero_small, container, false);
        ImageView iv = view.findViewById(R.id.iv_small_hero_portrait);
        TextView tv = view.findViewById(R.id.tv_small_hero_name);

        tv.setText(heroName);

        String url = heroImageMap.get(String.valueOf(heroId));
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv);
        } else {
            iv.setImageResource(R.drawable.ic_launcher_foreground);
        }

        container.addView(view);
    }
}
