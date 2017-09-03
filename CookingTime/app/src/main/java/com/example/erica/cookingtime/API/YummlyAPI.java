package com.example.erica.cookingtime.API;

import com.example.erica.cookingtime.POJO.YummlySearchResponse;
import com.example.erica.cookingtime.POJO.YummlySingleRecipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YummlyAPI {

    public static final String BASE_URL = "http://api.yummly.com/v1/";
    public static final String APP_ID = "4b57eb23";
    public static final String APP_KEY = "059cad41722332b5c437a619b1c6bc3e";

    @GET("api/recipes")
    Call<YummlySearchResponse> searchForRecipes(
            @Query("_app_id") String appId,
            @Query("_app_key") String appKey,
            @Query("q") String query,
            @Query("requirePictures") boolean required,
            @Query("allowedIngredient[]")ArrayList<String> allowedIngredients,
            @Query("excludedIngredient[]") ArrayList<String> excludedIngredients,
            @Query("allowedAllergy[]") ArrayList<String> allowedAllergies,
            @Query("allowedDiet[]") ArrayList<String> allowedDiets,
            @Query("allowedCuisine[]") ArrayList<String> allowedCuisine,
            @Query("excludedCuisine[]") ArrayList<String> excludedCuisine,
            @Query("allowedCourse[]") ArrayList<String> allowedCourse,
            @Query("excludedCourse[]") ArrayList<String> excludedCourse,
            @Query("allowedHoliday[]") ArrayList<String> allowedHoliday,
            @Query("excludedHoliday[]") ArrayList<String> excludedHoliday,
            @Query("maxResult") int maxResult,
            @Query("start") int start,
            @Query("maxTotalTimeInSeconds") Integer maxtotalTimeInSeconds);

    @GET("api/recipe/{recipe-id}")
    Call<YummlySingleRecipe> getRecipe(
            @Path("recipe-id") String id,
            @Query("_app_id") String appId,
            @Query("_app_key") String appKey);
}
