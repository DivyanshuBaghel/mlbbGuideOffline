package com.divy.mlbbguideoffline.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Hero {
    @SerializedName("hero_name")
    public String heroName;

    @SerializedName("class")
    public String heroClass;

    @SerializedName("portrait")
    public String portraitUrl;

    @SerializedName("laning")
    public List<String> laning;

    // We can add more fields if needed like skills, stats etc.
    // For now, these are enough for the list view.

    public String getHeroName() {
        return heroName;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getLaning() {
        if (laning != null && !laning.isEmpty()) {
            return String.join(", ", laning);
        }
        return "";
    }
}
