package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erica.cookingtime.API.SpoonacularAPI;
import com.example.erica.cookingtime.API.YummlyAPI;
import com.example.erica.cookingtime.DataBase.DBModifiers.DeleteIncludedIngr;
import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyFilters;
import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.DataBase.DataSources.FavouritesDataSource;
import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.DataBase.DataSources.FridgeDataSource;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.Counter;
import com.example.erica.cookingtime.POJO.Course;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.IncludedIngr;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.POJO.SpoonacularExctractedRecipe;
import com.example.erica.cookingtime.POJO.YummlySearchResponse;
import com.example.erica.cookingtime.POJO.YummlySingleRecipe;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class FilterSupportFragment extends Fragment{

    private FiltersCoordinator mListener;

    //queste per il frammento dei filter
    private ArrayList<String> dietsName;
    private ArrayList<String> allergiesName;
    //questa sia per filtri che per api
    private ArrayList<String> dislStringCode;
    //queste per le api
    private ArrayList<String> allergyStringCode;
    private ArrayList<String> dietStringCode;

    private ArrayList<Cuisine> cuisines;
    private ArrayList<Course> courses;
    private ArrayList<IncludedIngr> includedIngrs;

    private ArrayList<FridgeIngredient> fridgeIngredients;
    private ArrayList<FridgeIngredient> chosenIngredients = new ArrayList<>();
    private boolean fridgeOpen;

    private Call<YummlySearchResponse> APIcall;
    private Call<SpoonacularExctractedRecipe> APISpoonCall;

    private RetrieveDietPrefsFromDBTask task;
    private RetrieveFavsFromDBTask favTask;
    private RetrieveCuisinesFromDB cuisTask;
    private RetrieveCoursesFromDB courseTask;
    private RetrieveIncludedIngrFromDB inclIngrTask;
    private SearchRecipes searchTask;
    private RetrieveFridgeContent fridgeTask;

    private ArrayList<String> favList;
    private ArrayList<Match> recipeList;
    private Match selectedRecipe;

    public static final String DIET_PREF_OPEN = "diet_pref_open";
    public static final String PREP_TIME_OPEN = "prep_time_open";
    public static final String CUISINE_OPEN = "cuisine_open";
    public static final String COURSE_OPEN = "course_open";
    public static final String INCL_INGR_OPEN = "incl_ingr_open";
    public static final String FRIDGE_OPEN = "fridge_open";

    private String[] allFilters = {DIET_PREF_OPEN, PREP_TIME_OPEN, CUISINE_OPEN,
                                    COURSE_OPEN, INCL_INGR_OPEN, FRIDGE_OPEN};

    private Map<String, Boolean> filtersMap = new HashMap<>();
    private boolean filterPageOpen = false;

    private final static String SEARCH_QUERY = "search_query";
    private String searchQuery;

    public interface FiltersCoordinator{
        void passDislIngs(ArrayList<String> ings);
        void passDiets(ArrayList<String> diets);
        void passAllergies(ArrayList<String> allergies);
        void passCuisines(ArrayList<Cuisine> cuisines);
        void passCourses(ArrayList<Course> courses);
        void passIncludedIngrs(ArrayList<IncludedIngr> ingredients);
        void sendFavs(ArrayList<String> favs);
        void showConnectionError();
        void sendRecipes(ArrayList<Match> recipes);
        void onRecipeDetailReceived();
    }

    public FilterSupportFragment(){

    }

    public static FilterSupportFragment newInstance(String searchQuery){
        FilterSupportFragment fragment = new FilterSupportFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FiltersCoordinator) {
            mListener = (FiltersCoordinator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        for(String filter : allFilters){
            filtersMap.put(filter, false);
        }
        if (getArguments() != null) {
            searchQuery = getArguments().getString(SEARCH_QUERY);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(searchQuery != null){
            searchTask = new SearchRecipes();
            searchTask.execute(getActivity());
        }

        if(fridgeTask == null){
            fridgeTask = new RetrieveFridgeContent();
            fridgeTask.execute(getActivity());
        }

        if(task == null){
            task = new RetrieveDietPrefsFromDBTask();
            task.execute(getActivity());
        }

        if(favTask == null){
            favTask = new RetrieveFavsFromDBTask();
            favTask.execute(getActivity());
        }

        if(cuisTask == null){
            cuisTask = new RetrieveCuisinesFromDB();
            cuisTask.execute(getActivity());
        }

        if(courseTask == null){
            courseTask = new RetrieveCoursesFromDB();
            courseTask.execute(getActivity());
        }

        if(inclIngrTask == null){
            inclIngrTask = new RetrieveIncludedIngrFromDB();
            inclIngrTask.execute(getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null; // You need to return null
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setFridgeOpen(boolean fridgeOpen){
        this.fridgeOpen = fridgeOpen;
    }

    public void setChosenIngredients(ArrayList<FridgeIngredient> chosenIngredients) {
        this.chosenIngredients = chosenIngredients;
    }

    public ArrayList<String> getFavList(){
        return favList;
    }

    public Match getSelectedRecipe(){
        return selectedRecipe;
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

    public void removeSelectedRecipe(){
        selectedRecipe = null;
    }

    public ArrayList<Match> getRecipeList(){
        return recipeList;
    }

    public boolean isFilterPageOpen(){
        return filterPageOpen;
    }

    public void openFilterPage(){
        filterPageOpen = true;
    }

    public void closeFilterPage(){
        filterPageOpen = false;
        for(String filter : allFilters){
            filtersMap.put(filter,false);
        }
        fridgeOpen = false;
    }

    private void openFilter(String openFilter){
        filtersMap.put(openFilter, true);
    }

    public void closeFilter(String filter){
        filtersMap.put(filter, false);
    }

    public boolean isFilterOpen(){
        for(String filter : allFilters){
            if(filtersMap.get(filter)){
                return true;
            }
        }
        return false;
    }

    public Fragment filterToOpen(String openFilter){

        openFilter(openFilter);

        switch (openFilter){
            case DIET_PREF_OPEN:
                return DietPrefFilterFragment.newInstance(dislStringCode, dietsName, allergiesName);
            case PREP_TIME_OPEN:
                return PrepTimeFilterFragment.newInstance();
            case CUISINE_OPEN:
                return CuisineFilterFragment.newInstance(cuisines);
            case COURSE_OPEN:
                return CourseFilterFragment.newInstance(courses);
            case FRIDGE_OPEN:
                return FridgeContentFragment.newInstance(fridgeIngredients);
            case INCL_INGR_OPEN:
                return InclIngrFilterFragment.newInstance(includedIngrs, fridgeOpen, fridgeIngredients, chosenIngredients);
            default:
                return null;
        }
    }

    public Fragment filterToOpen(){

        if(filtersMap.get(FRIDGE_OPEN)){
            return FridgeContentFragment.newInstance(fridgeIngredients);
        }

        String openFilter = null;
        for(String filter : allFilters){
            if(filtersMap.get(filter) && !filter.equals(FRIDGE_OPEN)){
                openFilter = filter;
                break;
            }
        }
        if(openFilter == null){
            return null;
        }

        switch (openFilter){
            case DIET_PREF_OPEN:
                return DietPrefFilterFragment.newInstance(dislStringCode, dietsName, allergiesName);
            case PREP_TIME_OPEN:
                return PrepTimeFilterFragment.newInstance();
            case CUISINE_OPEN:
                return CuisineFilterFragment.newInstance(cuisines);
            case COURSE_OPEN:
                return CourseFilterFragment.newInstance(courses);
            case INCL_INGR_OPEN:
                if(!fridgeOpen){
                    chosenIngredients = new ArrayList<>();
                }
                return InclIngrFilterFragment.newInstance(includedIngrs, fridgeOpen, fridgeIngredients, chosenIngredients);
            default:
                return null;
        }
    }

    private void callAPI(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(YummlyAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        YummlyAPI yummlyAPI = retrofit.create(YummlyAPI.class);
        APIcall = yummlyAPI.searchForRecipes(YummlyAPI.APP_ID,
                YummlyAPI.APP_KEY, null, true, null, dislStringCode,
                allergyStringCode, dietStringCode, null, null, null, null, null,
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

    public void resetAllCuisines(){
        ModifyFilters task = new ModifyFilters(ConstantsDictionary.CUISINE,
                ConstantsDictionary.ALL, 0, 0);
        task.execute(getActivity());

        if(cuisines != null){
            for(int i = 0; i < cuisines.size(); i++){
                cuisines.get(i).setChosen(false);
            }
        }

    }

    public void resetAllCourses(){
        ModifyFilters task = new ModifyFilters(ConstantsDictionary.COURSE,
                ConstantsDictionary.ALL, 0, 0);
        task.execute(getActivity());

        if(courses != null){
            for(int i = 0; i < courses.size(); i++){
                courses.get(i).setChosen(false);
            }
        }

    }

    public void resetPrepTime(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ConstantsDictionary.SHARED_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ConstantsDictionary.SHARED_DIET_PREF_SWITCH, false);
        editor.apply();
    }

    public void resetIncludedIng(){
        includedIngrs.clear();
        DeleteIncludedIngr task = new DeleteIncludedIngr(ConstantsDictionary.ALL, 0);
        task.execute(getActivity());
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

            //aspetto che siano arrivati i dettagli di tutte le ricette
            while(counter.getCounter() > 0){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            mListener.sendRecipes(recipeList);
        }
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

            ArrayList<Allergy> allergy = dataSource.retrieveAllergies();
            ArrayList<Diet> diets = dataSource.retrieveDiets();
            ArrayList<DislikedIng> dislIng = dataSource.retrieveDislikedIng();

            dislStringCode = new ArrayList<>();
            allergyStringCode  = new ArrayList<>();
            dietStringCode = new ArrayList<>();

            allergiesName = new ArrayList<>();
            dietsName = new ArrayList<>();

            for(DislikedIng ing : dislIng){
                dislStringCode.add(ing.getName());
            }

            for(Allergy allerg : allergy){
                if(allerg.isChosen()){
                    allergyStringCode.add(allerg.getCode());
                    allergiesName.add(allerg.getName());
                }
            }

            for(Diet diet : diets){
                if(diet.isChosen()){
                    dietStringCode.add(diet.getCode());
                    dietsName.add(diet.getName());
                }

            }

            dataSource.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void value){

            mListener.passAllergies(allergiesName);
            mListener.passDiets(dietsName);
            mListener.passDislIngs(dislStringCode);

            if(searchQuery == null){
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
    }

    private class RetrieveIncludedIngrFromDB extends AsyncTask<Context, Void, ArrayList<IncludedIngr>>{

        @Override
        protected ArrayList<IncludedIngr> doInBackground(Context... contexts) {

            FilterDataSource dataSource = new FilterDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DB exc", e.getMessage());
            }
            ArrayList<IncludedIngr> inclIngrs = dataSource.retrieveAllIncludedIngreds();
            dataSource.close();

            return inclIngrs;
        }

        @Override
        protected void onPostExecute(ArrayList<IncludedIngr> value){
            includedIngrs = value;
            mListener.passIncludedIngrs(includedIngrs);
        }
    }

    private class RetrieveCuisinesFromDB extends AsyncTask<Context, Void, ArrayList<Cuisine>>{

        @Override
        protected ArrayList<Cuisine> doInBackground(Context... contexts) {

            FilterDataSource dataSource = new FilterDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DB exc", e.getMessage());
            }
            ArrayList<Cuisine> cuisines = dataSource.retrieveAllCuisines();
            dataSource.close();

            return cuisines;
        }

        @Override
        protected void onPostExecute(ArrayList<Cuisine> value){
            cuisines = value;
            mListener.passCuisines(cuisines);
        }
    }

    private class RetrieveCoursesFromDB extends AsyncTask<Context, Void, ArrayList<Course>>{

        @Override
        protected ArrayList<Course> doInBackground(Context... contexts) {

            FilterDataSource dataSource = new FilterDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DB exc", e.getMessage());
            }
            ArrayList<Course> courses = dataSource.retrieveAllCourses();
            dataSource.close();

            return courses;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> value){
            courses = value;
            mListener.passCourses(courses);
        }
    }

    private class SearchRecipes extends AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {
            SharedPreferences sharedPreferences = contexts[0].getSharedPreferences(ConstantsDictionary.SHARED_PREF_FILE, Context.MODE_PRIVATE);

            //Dietary preferences
            ArrayList<String> dislikedIngredientsNames = null;
            ArrayList<String> dietCodes = null;
            ArrayList<String> allergyCodes = null;
            if(sharedPreferences.getBoolean(ConstantsDictionary.SHARED_DIET_PREF_SWITCH, false)){
                DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);
                try{
                    dataSource.openReadOnly();
                }catch(SQLException e){
                    Log.e("Db ex", e.getMessage());
                }
                ArrayList<DislikedIng> dIng = dataSource.retrieveDislikedIng();
                ArrayList<Allergy> all = dataSource.retrieveChosenAllergies();
                ArrayList<Diet> d = dataSource.retrieveChosenDiets();

                dataSource.close();

                dislikedIngredientsNames = new ArrayList<>();
                dietCodes = new ArrayList<>();
                allergyCodes = new ArrayList<>();

                for(DislikedIng ing: dIng){
                    dislikedIngredientsNames.add(ing.getName());
                }
                for(Allergy al : all){
                    allergyCodes.add(al.getCode());
                }
                for(Diet di : d){
                    dietCodes.add(di.getCode());
                }
            }
            //prep time
            Integer maxTime = null;
            if(sharedPreferences.getBoolean(ConstantsDictionary.SHARED_PREP_TIME_SWITCH, false)){
                int progress = sharedPreferences.getInt(ConstantsDictionary.SHARED_PREP_TIME_PROGRESS, 0);
                maxTime = fromProgressToTime(progress);
            }
            //cuisines
            ArrayList<String> chosenCuisines = null;
            FilterDataSource dataSource = new FilterDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("db ex", e.getStackTrace().toString());
            }
            ArrayList<Cuisine> cuisines = dataSource.retrieveChosenCuisine();
            if(!cuisines.isEmpty()){
                chosenCuisines = new ArrayList<>();
                for(Cuisine c : cuisines){
                    chosenCuisines.add(c.getCode());
                }
            }

            //courses
            ArrayList<String> chosenCourses = null;
            ArrayList<Course> courses = dataSource.retrieveChosenCourses();
            if(!courses.isEmpty()){
                chosenCourses = new ArrayList<>();
                for(Course c : courses){
                    chosenCourses.add(c.getCode());
                }
            }

            //included ingredients
            ArrayList<String> allowedIngreds = null;
            ArrayList<IncludedIngr> ingr = dataSource.retrieveAllIncludedIngreds();
            if(!ingr.isEmpty()){
                allowedIngreds = new ArrayList<>();
                for(IncludedIngr i : ingr){
                    allowedIngreds.add(i.getName());
                }
            }
            dataSource.close();

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(YummlyAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            YummlyAPI yummlyAPI = retrofit.create(YummlyAPI.class);
            APIcall = yummlyAPI.searchForRecipes(YummlyAPI.APP_ID,
                    YummlyAPI.APP_KEY, searchQuery, true,
                    allowedIngreds, dislikedIngredientsNames, allergyCodes, dietCodes,
                    chosenCuisines, null, chosenCourses, null, null,
                    null, ConstantsDictionary.RECIPE_NUM, 0, maxTime);
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
            return null;
        }
    }

    private class RetrieveFridgeContent extends AsyncTask<Context, Void, ArrayList<FridgeIngredient>>{

        @Override
        protected ArrayList<FridgeIngredient> doInBackground(Context... contexts) {
            FridgeDataSource dataSource = new FridgeDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("db ex", "failed to open data source");
            }

            ArrayList<FridgeIngredient> ingredients = dataSource.retrieveAllFridgeIngredients();
            dataSource.close();
            return ingredients;
        }

        @Override
        protected void onPostExecute(ArrayList<FridgeIngredient> ings){
            fridgeIngredients = ings;
        }
    }

    private Integer fromProgressToTime(int progress){
        switch (progress){
            case 0:
                return 5*60;
            case 1:
                return 10*60;
            case 2:
                return 15*60;
            case 3:
                return 20*60;
            case 4:
                return 25*60;
            case 5:
                return 30*60;
            case 6:
                return 45*60;
            case 7:
                return 60*60;
            case 8:
                return 90*60;
            case 9:
                return 120*60;
            case 10:
                return 240*60;
            case 11:
                return 480*60;
            case 12:
                return 12*60*60;
            case 13:
                return null;
            default:
                return null;
        }
    }
}
