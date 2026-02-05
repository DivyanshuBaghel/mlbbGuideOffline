package com.divy.mlbbguideoffline.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Hero implements Parcelable {
    @SerializedName("hero_name")
    public String heroName;

    @SerializedName("mlid")
    public String mlid;

    @SerializedName("class")
    public String heroClass;

    @SerializedName("portrait")
    public String portraitUrl;

    @SerializedName("laning")
    public List<String> laning;

    @SerializedName("release_year")
    public String releaseYear;

    @SerializedName("speciality")
    public List<String> speciality;

    @SerializedName("skills")
    public List<Skill> skills;

    @SerializedName("counters")
    public List<Counter> counters;

    @SerializedName("synergies")
    public List<Synergy> synergies;

    public Hero() {
    }

    protected Hero(Parcel in) {
        heroName = in.readString();
        mlid = in.readString();
        heroClass = in.readString();
        portraitUrl = in.readString();
        laning = in.createStringArrayList();
        releaseYear = in.readString();
        speciality = in.createStringArrayList();
        skills = in.createTypedArrayList(Skill.CREATOR);
        counters = in.createTypedArrayList(Counter.CREATOR);
        synergies = in.createTypedArrayList(Synergy.CREATOR);
    }

    public static final Creator<Hero> CREATOR = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int size) {
            return new Hero[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(heroName);
        dest.writeString(mlid);
        dest.writeString(heroClass);
        dest.writeString(portraitUrl);
        dest.writeStringList(laning);
        dest.writeString(releaseYear);
        dest.writeStringList(speciality);
        dest.writeTypedList(skills);
        dest.writeTypedList(counters);
        dest.writeTypedList(synergies);
    }

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

    // Nested Classes

    public static class Skill implements Parcelable {
        @SerializedName("skill_name")
        public String skillName;
        @SerializedName("skill_icon")
        public String skillIcon;
        @SerializedName("type")
        public String type;
        @SerializedName("cooldown")
        public String cooldown;
        @SerializedName("manacost")
        public String manaCost;
        @SerializedName("description")
        public String description;

        public Skill() {
        }

        protected Skill(Parcel in) {
            skillName = in.readString();
            skillIcon = in.readString();
            type = in.readString();
            cooldown = in.readString();
            manaCost = in.readString();
            description = in.readString();
        }

        public static final Creator<Skill> CREATOR = new Creator<Skill>() {
            @Override
            public Skill createFromParcel(Parcel in) {
                return new Skill(in);
            }

            @Override
            public Skill[] newArray(int size) {
                return new Skill[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(skillName);
            dest.writeString(skillIcon);
            dest.writeString(type);
            dest.writeString(cooldown);
            dest.writeString(manaCost);
            dest.writeString(description);
        }
    }

    public static class Counter implements Parcelable {
        @SerializedName("heroid")
        public int heroId;
        @SerializedName("heroname")
        public String heroName;

        public Counter() {
        }

        protected Counter(Parcel in) {
            heroId = in.readInt();
            heroName = in.readString();
        }

        public static final Creator<Counter> CREATOR = new Creator<Counter>() {
            @Override
            public Counter createFromParcel(Parcel in) {
                return new Counter(in);
            }

            @Override
            public Counter[] newArray(int size) {
                return new Counter[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(heroId);
            dest.writeString(heroName);
        }
    }

    public static class Synergy implements Parcelable {
        @SerializedName("heroid")
        public int heroId;
        @SerializedName("heroname")
        public String heroName;

        public Synergy() {
        }

        protected Synergy(Parcel in) {
            heroId = in.readInt();
            heroName = in.readString();
        }

        public static final Creator<Synergy> CREATOR = new Creator<Synergy>() {
            @Override
            public Synergy createFromParcel(Parcel in) {
                return new Synergy(in);
            }

            @Override
            public Synergy[] newArray(int size) {
                return new Synergy[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(heroId);
            dest.writeString(heroName);
        }
    }
}
