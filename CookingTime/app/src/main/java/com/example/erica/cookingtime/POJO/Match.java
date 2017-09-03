package com.example.erica.cookingtime.POJO;

import java.util.List;

import com.example.erica.cookingtime.Utils.IngredientUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("smallImageUrls")
    @Expose
    private List<String> smallImageUrls = null;
    @SerializedName("totalTimeInSeconds")
    @Expose
    private Integer totalTimeInSeconds;
    @SerializedName("ingredients")
    @Expose
    private List<String> ingredients = null;
    @SerializedName("recipeName")
    @Expose
    private String recipeName;
    private YummlySingleRecipe recipe;
    private SpoonacularExctractedRecipe detailedRecipe;

    public SpoonacularExctractedRecipe getDetailedRecipe() {
        return detailedRecipe;
    }

    public void setDetailedRecipe(SpoonacularExctractedRecipe detailedRecipe) {
        this.detailedRecipe = detailedRecipe;
    }

    public YummlySingleRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(YummlySingleRecipe recipe) {
        this.recipe = recipe;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Integer getRating() {
        if(rating != null){
            return rating;
        }
        else{
            return 0;
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getSmallImageUrls() {
        return smallImageUrls;
    }

    public Integer getTotalTimeInSeconds() {
        if(totalTimeInSeconds != null){
            return totalTimeInSeconds;
        }
        else{
            return 0;
        }

    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getIngredientsNumber(){
        return ingredients.size();
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSmallImageUrls(List<String> smallImageUrls) {
        this.smallImageUrls = smallImageUrls;
    }

    public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}