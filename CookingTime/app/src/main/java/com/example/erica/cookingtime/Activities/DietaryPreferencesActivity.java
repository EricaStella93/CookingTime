package com.example.erica.cookingtime.Activities;

import android.content.Context;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.erica.cookingtime.Adapters.AllergyAdapter;
import com.example.erica.cookingtime.Adapters.DietAdapter;
import com.example.erica.cookingtime.Adapters.DislIngAdapter;
import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.Dialogs.AddDislikedIngredientDialog;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.MenuUtils;

import java.util.ArrayList;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class DietaryPreferencesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddDislikedIngredientDialog.AddDislIngredientDialogListener {

    private ArrayList<DislikedIng> dislIngList = new ArrayList<>();
    private RecyclerView dislIngRecView;
    private DislIngAdapter dislIngAdapter;
    private RecyclerView.LayoutManager dislIngLayoutManager;
    private RetrieveDislikedIngredientsFromDBTask dislIngTask;

    private ArrayList<Allergy> allergList = new ArrayList<>();
    private RecyclerView allergRecView;
    private AllergyAdapter allergyAdapter;
    private RecyclerView.LayoutManager allergLayoutManager;
    private RetrieveAllergiesFromDBTask allergTask;

    private ArrayList<Diet> dietList = new ArrayList<>();
    private RecyclerView dietRecView;
    private DietAdapter dietAdapter;
    private RecyclerView.LayoutManager dietLayoutManager;
    private RetrieveDietsFromDBTask dietTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_diet_pref);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //aggiunta per cambiare colore alla label dell'activity
            toolbar.setTitleTextColor(getResources().getColor(R.color.orange));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //aggiunte per far funzionare il pulsante con l'immagine cambiata del menù laterale
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //foto sopra al menù laterale
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.diet_pref_menu).setChecked(true);

        setDietRecyclerViews();
        setAllergyRecyclerViews();
        setDislIngRecyclerViews();

        if(dislIngList.isEmpty() && dislIngTask == null){
            this.dislIngTask = new RetrieveDislikedIngredientsFromDBTask();
            dislIngTask.execute(this);
        }

        if(allergList.isEmpty() && allergTask == null){
            this.allergTask = new RetrieveAllergiesFromDBTask();
            allergTask.execute(this);
        }

        if(dietList.isEmpty() && dietTask == null){
            this.dietTask = new RetrieveDietsFromDBTask();
            dietTask.execute(this);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //menu laterale
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return MenuUtils.menuNavigation(item, this);
    }

    public void openAddDislIngDialog(View v){
        AddDislikedIngredientDialog dialog = new AddDislikedIngredientDialog();
        dialog.show(getSupportFragmentManager(), "add_disl_ing");
    }

    private void setDislIngRecyclerViews(){
        dislIngRecView = (RecyclerView) findViewById(R.id.disl_ing_recycler_view);
        dislIngLayoutManager = new LinearLayoutManager(this);
        dislIngRecView.setLayoutManager(dislIngLayoutManager);
        dislIngAdapter = new DislIngAdapter(dislIngList);
        dislIngRecView.setAdapter(dislIngAdapter);
    }

    private void setAllergyRecyclerViews(){
        allergRecView = (RecyclerView) findViewById(R.id.allerg_recycler_view);
        allergRecView.setNestedScrollingEnabled(false);
        allergLayoutManager = new LinearLayoutManager(this);
        allergRecView.setLayoutManager(allergLayoutManager);
        allergyAdapter = new AllergyAdapter(allergList);
        allergRecView.setAdapter(allergyAdapter);
    }

    private void setDietRecyclerViews(){
        dietRecView = (RecyclerView) findViewById(R.id.diets_recycler_view);
        dietLayoutManager = new LinearLayoutManager(this);
        dietRecView.setLayoutManager(dietLayoutManager);
        dietAdapter = new DietAdapter(dietList);
        dietRecView.setAdapter(dietAdapter);
    }

    @Override
    public void onAddIngredient(DislikedIng ing) {
        //controllo che non sia già stato inserito
        for(DislikedIng dislIng : dislIngList){
            if(dislIng.getName().toLowerCase().equals(ing.getName().toLowerCase())){
                return;
            }
        }

        AddDislIng task = new AddDislIng(-1, ing.getName());
        task.execute(this);
    }


    private class RetrieveDislikedIngredientsFromDBTask extends AsyncTask<Context, DislikedIng, Void> {

        @Override
        protected Void doInBackground(Context... contexts){
            DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DBEX", e.getMessage());
            }
            ArrayList<DislikedIng> ingredients = dataSource.retrieveDislikedIng();
            dataSource.close();

            for(int i=0; i<ingredients.size(); i++){
                publishProgress(ingredients.get(i));
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(DislikedIng... ing){
            dislIngAdapter.add(dislIngList.size(), ing[0]);
        }

    }

    private class RetrieveAllergiesFromDBTask extends AsyncTask<Context, Allergy, Void> {

        @Override
        protected Void doInBackground(Context... contexts){
            DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DBEX", "ECCEZIONE NEL DB");
            }
            ArrayList<Allergy> allergies = dataSource.retrieveAllergies();
            dataSource.close();

            for(int i = 0; i < allergies.size(); i++){
                publishProgress(allergies.get(i));
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Allergy... value){
            allergyAdapter.add(allergList.size(), value[0]);
        }

    }

    class RetrieveDietsFromDBTask extends AsyncTask<Context, Diet, Void> {

        @Override
        protected Void doInBackground(Context... contexts){
            DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DBEX", "ECCEZIONE NEL DB");
            }
            ArrayList<Diet> diets = dataSource.retrieveDiets();
            dataSource.close();

            for(int i = 0; i < diets.size(); i++){
                publishProgress(diets.get(i));
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Diet... value){
            dietAdapter.add(dietList.size(), value[0]);
        }

    }

    public class AddDislIng extends AsyncTask<Context, Void, Void>{

        private final long id;
        private final String name;
        private long lastInsertedId;

        public AddDislIng(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Context... contexts) {

            DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);

            try{
                dataSource.openWritable();
            }catch (SQLException e){
                Log.e("DBEX", e.getMessage());
            }

            lastInsertedId = dataSource.addDislIng(name);

            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            dislIngAdapter.add(dislIngList.size(), new DislikedIng(lastInsertedId, name));
        }
    }
}
