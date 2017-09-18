package com.example.erica.cookingtime.API;

import com.example.erica.cookingtime.POJO.SpoonacularExctractedRecipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SpoonacularAPI {

    static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/";

    @Headers({"X-Mashape-Key: va227dnbk2mshwaLUGdoa58LWUD8p1UbWo7jsn68u1owEnG4ub",
            "Accept: application/json"})
    @GET("recipes/extract")
    Call<SpoonacularExctractedRecipe> getRecipe(
            @Query("forceExtraction") boolean forceExtraction,
            @Query("url") String url);
}
