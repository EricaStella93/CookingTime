package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.erica.cookingtime.API.SpoonacularAPI;
import com.example.erica.cookingtime.API.YummlyAPI;
import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.DataBase.DataSources.FavouritesDataSource;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.Counter;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.POJO.SpoonacularExctractedRecipe;
import com.example.erica.cookingtime.POJO.YummlySearchResponse;
import com.example.erica.cookingtime.POJO.YummlySingleRecipe;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupportFragment extends Fragment {

    private YummlySearchResponse yummlyResponse;
    private ArrayList<Match> recipeList;
    private Match selectedRecipe;
    private ArrayList<String> favList;
    private RetrieveFavsFromDBTask task;
    private RetrieveDietPrefsFromDBTask dietPrefTask;
    private ActivityCoordinator mListener;
    private Call<YummlySearchResponse> APIcall;
    private Call<SpoonacularExctractedRecipe> APISpoonCall;

    private ArrayList<String> dislString = new ArrayList<>();
    private ArrayList<String> allergyString = new ArrayList<>();
    private ArrayList<String> dietString = new ArrayList<>();

    public SupportFragment(){}

    public static SupportFragment newInstance(){
        return new SupportFragment();
    }

    public ArrayList<String> getFavList(){
        return favList;
    }

    public ArrayList<Match> getRecipeList(){
        return recipeList;
    }

    public Match getSelectedRecipe(){
        return selectedRecipe;
    }

    public void removeSelectedRecipe(){
        selectedRecipe = null;
    }

    public void setSelectedRecipe(final Match recipe){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SpoonacularAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        SpoonacularAPI spoona = retrofit.create(SpoonacularAPI.class);
        APISpoonCall = spoona.getRecipe(false, recipe.getRecipe().getSource().getSourceRecipeUrl());
        APISpoonCall.enqueue(new Callback<SpoonacularExctractedRecipe>() {
            @Override
            public void onResponse(Call<SpoonacularExctractedRecipe> call, Response<SpoonacularExctractedRecipe> response) {

                recipe.setDetailedRecipe(response.body());
                selectedRecipe = recipe;
                mListener.onRecipeDetailReceived();
            }

            @Override
            public void onFailure(Call<SpoonacularExctractedRecipe> call, Throwable t) {
                Log.e("API FALLITE", t.getMessage());
                t.printStackTrace();
                mListener.showConnectionError();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityCoordinator) {
            mListener = (ActivityCoordinator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof ActivityCoordinator) {
            mListener = (ActivityCoordinator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_list_section, null); // You need to return null
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(task == null){
            task = new RetrieveFavsFromDBTask();
            task.execute(getActivity());
        }

        if(dietPrefTask == null){
            dietPrefTask = new RetrieveDietPrefsFromDBTask();
            dietPrefTask.execute(getActivity());
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addFav(String id){
        if(favList != null){
            favList.add(id);
        }
    }

    public void removeFav(String id){

        if(favList != null){
            favList.remove(id);
        }

    }

    private void callAPI(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(YummlyAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        YummlyAPI yummlyAPI = retrofit.create(YummlyAPI.class);
        APIcall = yummlyAPI.searchForRecipes(YummlyAPI.APP_ID,
                YummlyAPI.APP_KEY, null, true, null, dislString,
                allergyString, dietString, null, null, null, null, null,
                null, ConstantsDictionary.RECIPE_NUM, 0, null);
        APIcall.enqueue(new Callback<YummlySearchResponse>() {
            @Override
            public void onResponse(Call<YummlySearchResponse> call, Response<YummlySearchResponse> response) {
                recipeList = (ArrayList<Match>) response.body().matches;
                CallSingleRecipeAPI task = new CallSingleRecipeAPI();
                task.execute();
            }

            @Override
            public void onFailure(Call<YummlySearchResponse> call, Throwable t) {
                Log.e("API FALLITE", t.getMessage());
                t.printStackTrace();
                mListener.showConnectionError();
            }
        });
    }

    public interface ActivityCoordinator{
        void sendRecipes(ArrayList<Match> recipes);
        void sendFavs(ArrayList<String> favs);
        void showConnectionError();
        void onRecipeDetailReceived();
    }

    private class RetrieveFavsFromDBTask extends AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {
            ArrayList<String> favs = new ArrayList<>();

            FavouritesDataSource dataSource = new FavouritesDataSource(contexts[0]);

            try{
                dataSource.openReadOnly();

            }catch (SQLException e){
                Log.e("DBERROR", "ERROR OPENING FAV TABLE");
            }

            favs = dataSource.retrieveFavsIds();
            dataSource.close();
            favList = favs;
            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            mListener.sendFavs(favList);
        }
    }


    private class RetrieveDietPrefsFromDBTask extends AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {

            DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);

            try{
                dataSource.openReadOnly();

            }catch (SQLException e){
                Log.e("DBERROR", "ERROR OPENING DIET PREF TABLE");
            }

            ArrayList<Allergy> allergy = dataSource.retrieveChosenAllergies();
            ArrayList<Diet> diets = dataSource.retrieveChosenDiets();
            ArrayList<DislikedIng> dislIng = dataSource.retrieveDislikedIng();


            for(DislikedIng ing : dislIng){
                dislString.add(ing.getName());
            }

            for(Allergy allerg : allergy){
                allergyString.add(allerg.getCode());
            }

            for(Diet diet : diets){
                dietString.add(diet.getCode());
            }

            dataSource.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void value){

            if(APIcall == null ) {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    callAPI();
                } else {
                    mListener.showConnectionError();
                }
            }
        }
    }

    private class CallSingleRecipeAPI extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... contexts) {

            final Counter counter = new Counter(recipeList.size());

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(YummlyAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
            YummlyAPI yummlyAPI = retrofit.create(YummlyAPI.class);

            for(final Match match : recipeList){

                String id = match.getId();
                Call<YummlySingleRecipe> recipe = yummlyAPI.getRecipe(match.getId(), YummlyAPI.APP_ID, YummlyAPI.APP_KEY);
                recipe.enqueue(new Callback<YummlySingleRecipe>() {
                    @Override
                    public void onResponse(Call<YummlySingleRecipe> call, Response<YummlySingleRecipe> response) {
                        match.setRecipe(response.body());
                        counter.decrement();
                    }

                    @Override
                    public void onFailure(Call<YummlySingleRecipe> call, Throwable t) {
                        mListener.showConnectionError();
                    }
                });
            }

            while(counter.getCounter() > 0){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            mListener.sendRecipes(recipeList);
        }
    }
}
