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

public class SupportFavsFragment extends Fragment {

    private ArrayList<Match> recipeList;
    private Match selectedRecipe;
    private ArrayList<String> favList;
    private RetrieveFavsFromDBTask task;
    private ActivityCoordinator mListener;
    private Call<SpoonacularExctractedRecipe> APISpoonCall;
    private String initialQuery = "";

    public SupportFavsFragment(){}

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

    public static SupportFavsFragment newInstance(){
        return new SupportFavsFragment();
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

    public String getInitialQuery() {
        return initialQuery;
    }

    public void setInitialQuery(String initialQuery) {
        this.initialQuery = initialQuery;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            FavouritesDataSource dataSource = new FavouritesDataSource(contexts[0]);

            try{
                dataSource.openReadOnly();

            }catch (SQLException e){
                Log.e("DBERROR", "ERROR OPENING FAV TABLE");
            }

            ArrayList<Match> favs = dataSource.retrieveCompleteFavs();

            ArrayList<String> stringFavs = dataSource.retrieveFavsIds();
            dataSource.close();

            recipeList = favs;
            favList = stringFavs;

            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            mListener.sendFavs(favList);
            mListener.sendRecipes(recipeList);
        }
    }

}
