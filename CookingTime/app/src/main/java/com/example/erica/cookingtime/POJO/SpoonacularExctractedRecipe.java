package com.example.erica.cookingtime.POJO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpoonacularExctractedRecipe {

        @SerializedName("servings")
        @Expose
        public Integer servings;
        @SerializedName("sourceUrl")
        @Expose
        public String sourceUrl;
        @SerializedName("spoonacularSourceUrl")
        @Expose
        public String spoonacularSourceUrl;
        @SerializedName("extendedIngredients")
        @Expose
        public List<ExtendedIngredient> extendedIngredients = null;
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("readyInMinutes")
        @Expose
        public Integer readyInMinutes;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("imageUrls")
        @Expose
        public List<String> imageUrls = null;
        @SerializedName("text")
        @Expose
        public String text;
        @SerializedName("instructions")
        @Expose
        public String instructions;

    }

