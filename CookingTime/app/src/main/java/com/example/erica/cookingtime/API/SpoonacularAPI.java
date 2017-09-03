package com.example.erica.cookingtime.API;

import com.example.erica.cookingtime.POJO.SpoonacularExctractedRecipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SpoonacularAPI {

    static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/";

    @Headers({"X-Mashape-Key: hdf1pL9N0qmshJgVa9o1O0IS9imKp1v2P4Xjsn4COdcesI2f62",
            "Accept: application/json"})
    @GET("recipes/extract")
    Call<SpoonacularExctractedRecipe> getRecipe(
            @Query("forceExtraction") boolean forceExtraction,
            @Query("url") String url);
}
