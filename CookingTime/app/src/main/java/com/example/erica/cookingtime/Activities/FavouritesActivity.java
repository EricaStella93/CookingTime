package com.example.erica.cookingtime.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erica.cookingtime.Adapters.RecipeFrontAdapter;
import com.example.erica.cookingtime.Fragments.FavsRecipeListFragment;
import com.example.erica.cookingtime.Fragments.OnFragmentInteractionListener;
import com.example.erica.cookingtime.Fragments.RecipeListFragment;
import com.example.erica.cookingtime.Fragments.SingleRecipeFragment;
import com.example.erica.cookingtime.Fragments.SupportFavsFragment;
import com.example.erica.cookingtime.Fragments.SupportFragment;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IntentFactory;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.SearchViewStyle;
import com.example.erica.cookingtime.Utils.MenuUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class FavouritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener,
        SupportFavsFragment.ActivityCoordinator,
        SingleRecipeFragment.SingleRecipeListener, RecipeFrontAdapter.OnChangeFavs{

    private final String STATE_QUERY = "state_query";

    private final static String SUPPORT_FRAGMENT = "SUPPORT";

    private SearchView sv;
    private CharSequence initialQuery;

    private SupportFavsFragment supportFragment;
    private FavsRecipeListFragment recipeListFragment;
    private SingleRecipeFragment singleRecipeFragment;

    private boolean mDualePane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_favs);

        setupToolBar();

        //foto sopra al menù laterale
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.favs_menu).setChecked(true);


        View secondFrame = findViewById(R.id.single_recipe_fragment_frame);
        if(secondFrame != null){
            mDualePane = true;
        }else{
            mDualePane = false;
        }

        setUpFragments();

        //setting of the searchview on device rotation
        if(savedInstanceState != null){
            initialQuery = savedInstanceState.getCharSequence(STATE_QUERY);
        }
    }

    private void setUpFragments(){
        FragmentManager fragmentManager = getFragmentManager();
        supportFragment = (SupportFavsFragment) fragmentManager.findFragmentByTag(SUPPORT_FRAGMENT);
        if(supportFragment == null){
            supportFragment = SupportFavsFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(supportFragment, SUPPORT_FRAGMENT);
            fragmentTransaction.commit();
            return;

        }else{
            findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
            findViewById(R.id.recipe_fragment_container).setVisibility(View.VISIBLE);

            Match selRec = supportFragment.getSelectedRecipe();
            //se ho una ricetta aperta
            if(selRec != null){
                setupWithSelRec(selRec);
                return;
            }

            ArrayList<Match> recipes = supportFragment.getRecipeList();
            //se non ho ricette aperte e sono sulla lista di ricette
            if(recipes != null){
                setupWithOnlyRecipes();
            }
        }
    }

    private void setupWithOnlyRecipes(){
        FragmentManager fragmentManager = getFragmentManager();

        findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.recipe_fragment_container).setVisibility(View.VISIBLE);

        //schermo grande
        if(mDualePane){
            int columns = 3;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                columns = 4;
            }
            recipeListFragment = FavsRecipeListFragment.newInstance(columns, supportFragment.getInitialQuery());

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
            fragmentTransaction.commit();
            return;
        }
        //schermo piccolo
        else{
            //orizzontale
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                recipeListFragment = FavsRecipeListFragment.newInstance(2, supportFragment.getInitialQuery());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
                fragmentTransaction.commit();
                return;
            }
            //verticale
            recipeListFragment = FavsRecipeListFragment.newInstance(1, supportFragment.getInitialQuery());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
            fragmentTransaction.commit();
            return;
        }
    }

    private void setupWithSelRec(Match selRec){
        FragmentManager fragmentManager = getFragmentManager();
        findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);

        //schermo grande e ricetta aperta
        if(mDualePane){
            int columns = 1;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                columns = 2;
            }
            findViewById(R.id.single_recipe_fragment_frame).setVisibility(View.VISIBLE);
            recipeListFragment = FavsRecipeListFragment.newInstance(columns, supportFragment.getInitialQuery());
            singleRecipeFragment = SingleRecipeFragment.newInstance(false);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
            fragmentTransaction.commit();

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.single_recipe_fragment_frame, singleRecipeFragment);
            fragmentTransaction.commit();

        }
        //schermo piccolo e ricetta aperta
        else{
            //schermo piccolo orizzontale
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                recipeListFragment = FavsRecipeListFragment.newInstance(2, supportFragment.getInitialQuery());
                singleRecipeFragment = SingleRecipeFragment.newInstance(true);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recipe_fragment_container, singleRecipeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                horizontalRecipeToolbar(supportFragment.getSelectedRecipe());
                return;
            }
            //schermo piccolo verticale
            recipeListFragment = FavsRecipeListFragment.newInstance(1, supportFragment.getInitialQuery());
            singleRecipeFragment = SingleRecipeFragment.newInstance(true);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.recipe_fragment_container, singleRecipeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            changeToRecipeToolbar(selRec);
        }
        return;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();

            } else {
                super.onBackPressed();
            }*/
            super.onBackPressed();
        }
    }

    //menu in alto
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(findViewById(R.id.toolbar).getVisibility() == View.VISIBLE){
            /*getMenuInflater().inflate(R.menu.menu_favs, menu);
            configureSearchView(menu);*/
        }else{
            getMenuInflater().inflate(R.menu.share_recipe, menu);
            if(findViewById(R.id.toolbar_horizontal).getVisibility() == View.VISIBLE){
                menu.findItem(R.id.action_share_recipe).setIcon(getResources().getDrawable(R.drawable.ic_share_grey));
            }
        }
        return true;
    }

    //menu barra in alto
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_shopping_list) {
            Intent intent = IntentFactory.intentFactory(IntentFactory.SHOPPING_LIST, this);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_share_recipe){
            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, supportFragment.getSelectedRecipe().getDetailedRecipe().sourceUrl);
            startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_using)));
        }
        return super.onOptionsItemSelected(item);
    }

    //menu laterale
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return MenuUtils.menuNavigation(item, this);
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        if(sv != null){
            if(!sv.isIconified()) {
                if(!sv.getQuery().toString().trim().equals("")){
                    state.putCharSequence(STATE_QUERY, sv.getQuery());
                }
            }
        }
    }

    @Override
    public void onFragmentRecipeSelected(Match recipe) {
        supportFragment.setSelectedRecipe(recipe);
    }

    @Override
    public ArrayList<String> receiveFavs() {
        if(supportFragment != null){
            return supportFragment.getFavList();
        }
        else{
            return null;
        }

    }

    @Override
    public ArrayList<Match> receiveMatches() {
        if(supportFragment != null){
            return supportFragment.getRecipeList();
        }
        else{
            return null;
        }
    }

    @Override
    public void saveInitialQuery(String initialQuery) {
        supportFragment.setInitialQuery(initialQuery);
    }

    @Override
    public void sendRecipes(ArrayList<Match> recipes) {

        findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.recipe_fragment_container).setVisibility(View.VISIBLE);
        int columns = 1;
        if(mDualePane){
            columns = 4;
        }else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                columns = 2;
            }
        }
        recipeListFragment = FavsRecipeListFragment.newInstance(columns, supportFragment.getInitialQuery());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recipe_fragment_container, recipeListFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sendFavs(ArrayList<String> favs) {

    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, "Error: no internet connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecipeDetailReceived() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //schermo grande
        if(mDualePane){
            findViewById(R.id.single_recipe_fragment_frame).setVisibility(View.VISIBLE);
            singleRecipeFragment = SingleRecipeFragment.newInstance(false);
            int columns = 1;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                columns = 2;
            }
            recipeListFragment.changeGridColumns(columns, this);
            fragmentTransaction.replace(R.id.single_recipe_fragment_frame, singleRecipeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return;
        }else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                singleRecipeFragment = SingleRecipeFragment.newInstance(true);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recipe_fragment_container, singleRecipeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                horizontalRecipeToolbar(supportFragment.getSelectedRecipe());
                return;
            }
            singleRecipeFragment = SingleRecipeFragment.newInstance(true);
            fragmentTransaction.replace(R.id.recipe_fragment_container, singleRecipeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            changeToRecipeToolbar(supportFragment.getSelectedRecipe());
        }
    }

    @Override
    public Match receiveRecipe() {
        return supportFragment.getSelectedRecipe();
    }

    @Override
    public void onExitSingleRecipe() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //schermo grande
        if(mDualePane){
            int columns = 3;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                columns = 4;
            }
            recipeListFragment.changeGridColumns(columns, this);
            fragmentTransaction.remove(singleRecipeFragment);
            fragmentTransaction.commit();
            findViewById(R.id.single_recipe_fragment_frame).setVisibility(View.GONE);
            supportFragment.removeSelectedRecipe();
        }else{
            fragmentManager.popBackStack();
            backFromRecipeToolbar();
            supportFragment.removeSelectedRecipe();
        }
    }

    private void setupToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //aggiunta per cambiare colore alla label dell'activity
        toolbar.setTitleTextColor(getResources().getColor(R.color.orange));

        setSupportActionBar(toolbar);

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
    }

    private void horizontalRecipeToolbar(Match recipe){
        findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.GONE);

        findViewById(R.id.toolbar_horizontal).setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_horizontal);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_arrow));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));

        //aggiunta per cambiare colore alla label dell'activity
        toolbar.setTitleTextColor(getResources().getColor(R.color.orange));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    backFromRecipeToolbar();
                    supportFragment.removeSelectedRecipe();
                }
            }
        });
        setTitle(recipe.getRecipeName());
    }

    private void changeToRecipeToolbar(Match recipe){

        findViewById(R.id.toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_horizontal).setVisibility(View.GONE);

        findViewById(R.id.collapsing_toolbar).setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_collaps);
        setSupportActionBar(toolbar);
        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(recipe.getRecipeName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_arrow_white));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    backFromRecipeToolbar();
                    supportFragment.removeSelectedRecipe();
                }
            }
        });

        Picasso.with(this)
                .load(recipe.getRecipe().getImage())
                .into((ImageView) findViewById(R.id.recipe_image));

    }

    private void backFromRecipeToolbar(){

        findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_horizontal).setVisibility(View.GONE);

        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        setupToolBar();

    }

    @Override
    public void addFav(String id) {
        supportFragment.addFav(id);
    }

    @Override
    public void removeFav(String id) {
        supportFragment.removeFav(id);
    }
}
