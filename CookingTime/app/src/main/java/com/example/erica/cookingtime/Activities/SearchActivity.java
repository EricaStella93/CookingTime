package com.example.erica.cookingtime.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.Dialogs.AddIncludedIngrDialog;
import com.example.erica.cookingtime.Dialogs.ResetCoursesDialog;
import com.example.erica.cookingtime.Dialogs.ResetCuisinesDialog;
import com.example.erica.cookingtime.Dialogs.ResetFiltersDialog;
import com.example.erica.cookingtime.Fragments.CourseFilterFragment;
import com.example.erica.cookingtime.Fragments.CuisineFilterFragment;
import com.example.erica.cookingtime.Fragments.DietPrefFilterFragment;
import com.example.erica.cookingtime.Fragments.FilterSupportFragment;
import com.example.erica.cookingtime.Fragments.FiltersFragment;
import com.example.erica.cookingtime.Fragments.FridgeContentFragment;
import com.example.erica.cookingtime.Fragments.InclIngrFilterFragment;
import com.example.erica.cookingtime.Fragments.OnCloseFilterFragment;
import com.example.erica.cookingtime.Fragments.OnFragmentInteractionListener;
import com.example.erica.cookingtime.Fragments.RecipeListFragment;
import com.example.erica.cookingtime.Fragments.SingleRecipeFragment;
import com.example.erica.cookingtime.POJO.Course;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.IncludedIngr;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.MenuUtils;
import com.example.erica.cookingtime.Utils.SearchViewStyle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FiltersFragment.OnFiltersChange,
        FilterSupportFragment.FiltersCoordinator, SingleRecipeFragment.SingleRecipeListener,
        OnFragmentInteractionListener,
        OnCloseFilterFragment,
        ResetCuisinesDialog.ResetCuisinesListener,
        ResetCoursesDialog.ResetCoursesListener,
        AddIncludedIngrDialog.AddInclIngredientDialogListener,
        ResetFiltersDialog.ResetFilterListener,
        InclIngrFilterFragment.OnIncludedIngredients {

    private boolean mDualPane;

    private final String STATE_QUERY = "state_query";
    private SearchView sv;
    private CharSequence initialQuery;
    private MenuItem filters;

    private FilterSupportFragment supportFragment;
    private RecipeListFragment recipeListFragment;
    private SingleRecipeFragment singleRecipeFragment;

    private FiltersFragment filtersFragment;
    private Fragment filter;
    private FridgeContentFragment fridgeFragment;

    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupToolbar();

        //foto sopra al menù laterale
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.search_menu).setChecked(true);

        View secondFrame = findViewById(R.id.second_fragment_frame);
        if(secondFrame != null){
            mDualPane = true;
        }else{
            mDualPane = false;
        }
        if(getIntent().getStringExtra(ConstantsDictionary.SEARCH_QUERY) != null){
            searchQuery = getIntent().getStringExtra(ConstantsDictionary.SEARCH_QUERY);
            setTitle(searchQuery);
        }else{
            searchQuery = null;
        }

        setupFragments();

        if(savedInstanceState != null){
            initialQuery = savedInstanceState.getCharSequence(STATE_QUERY);
        }
    }

    private void setupFragments(){
        FragmentManager fragmentManager = getFragmentManager();
        supportFragment = (FilterSupportFragment) fragmentManager.findFragmentByTag(ConstantsDictionary.SUPPORT_FRAGMENT);
        if(supportFragment == null){
            setupSupport(fragmentManager);
            return;
        }else{
            //se ho una ricetta aperta
            Match selRec = supportFragment.getSelectedRecipe();
            if(selRec != null){
                findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
                setupWithOpenRighePanel(fragmentManager);
                setupOpenRecipe(fragmentManager, selRec);
                return;
            }
            //se ho i filtri aperti
            if(supportFragment.isFilterPageOpen()){
                findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
                setupWithOpenRighePanel(fragmentManager);
                setupFilters(fragmentManager);
                return;
            }
            //se ho solo la lista ricette aperta
            if(supportFragment.getRecipeList() != null){
                findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);
                setupWithOnlyRecipeList(fragmentManager);
            }
        }

    }

    private void setupFilters(FragmentManager fragmentManager){
        if(mDualPane){
            findViewById(R.id.second_fragment_frame).setVisibility(View.VISIBLE);
            filtersFragment = FiltersFragment.newInstance(false);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.second_fragment_frame, filtersFragment);
            fragmentTransaction.commit();

            if(supportFragment.isFilterOpen()){
                filter = supportFragment.filterToOpen();
                /*if(filter instanceof FridgeContentFragment){
                    fridgeFragment = (FridgeContentFragment) filter;
                    filter = supportFragment.filterToOpen(FilterSupportFragment.INCL_INGR_OPEN);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.second_fragment_frame, filter);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.second_fragment_frame, fridgeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }*/
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.second_fragment_frame, filter);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else{
                return;
            }

        }else{
            filtersFragment = FiltersFragment.newInstance(true);

            switchToFiltersToolbar();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.first_fragment_frame, filtersFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if(supportFragment.isFilterOpen()){
                filter = supportFragment.filterToOpen();
                /*if(filter instanceof FridgeContentFragment){
                    fridgeFragment = (FridgeContentFragment) filter;
                    filter = supportFragment.filterToOpen(FilterSupportFragment.INCL_INGR_OPEN);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.first_fragment_frame, filter);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.first_fragment_frame, fridgeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }*/
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.first_fragment_frame, filter);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else{
                return;
            }
        }

    }

    private void setupWithOpenRighePanel(FragmentManager fragmentManager){
        if(mDualPane){
            setupRecipeList(2, fragmentManager);
            return;
        }else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setupRecipeList(2, fragmentManager);
                return;
            }else{
                setupRecipeList(1, fragmentManager);
                return;
            }
        }
    }

    private void setupWithOnlyRecipeList(FragmentManager fragmentManager){
        if(mDualPane){
            setupRecipeList(4, fragmentManager);
            return;
        }else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setupRecipeList(2, fragmentManager);
                return;
            }else{
                setupRecipeList(1, fragmentManager);
                return;
            }
        }
    }

    private void setupOpenRecipe(FragmentManager fragmentManager, Match selRec){
        if(mDualPane){
            findViewById(R.id.second_fragment_frame).setVisibility(View.VISIBLE);
            singleRecipeFragment = SingleRecipeFragment.newInstance(false);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.second_fragment_frame, recipeListFragment);
            fragmentTransaction.commit();
        }else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                singleRecipeFragment = SingleRecipeFragment.newInstance(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.first_fragment_frame, singleRecipeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                horizontalRecipeToolbar(selRec);

            }else{
                singleRecipeFragment = SingleRecipeFragment.newInstance(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.first_fragment_frame, singleRecipeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                changeToRecipeToolbar(selRec);
            }
        }
    }

    private void setupRecipeList(int columns, FragmentManager fragmentManager){
        findViewById(R.id.first_fragment_frame).setVisibility(View.VISIBLE);
        recipeListFragment = RecipeListFragment.newInstance(columns);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.first_fragment_frame, recipeListFragment);
        fragmentTransaction.commit();
    }

    private void setupSupport(FragmentManager fragmentManager){
        supportFragment = FilterSupportFragment.newInstance(searchQuery);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(supportFragment, ConstantsDictionary.SUPPORT_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void setupToolbar(){
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

    private void openFilters(){
        FragmentManager fragmentManager = getFragmentManager();
        supportFragment.openFilterPage();
        if(mDualPane){
            if(recipeListFragment!= null){
                recipeListFragment.changeGridColumns(2, this);
            }
            findViewById(R.id.second_fragment_frame).setVisibility(View.VISIBLE);
            filtersFragment = FiltersFragment.newInstance(false);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.second_fragment_frame, filtersFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else{

            filtersFragment = FiltersFragment.newInstance(true);
            switchToFiltersToolbar();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.first_fragment_frame, filtersFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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

    //menu in alto
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(findViewById(R.id.toolbar).getVisibility() == View.VISIBLE){
            getMenuInflater().inflate(R.menu.search, menu);
            filters = menu.findItem(R.id.action_filters);
            configureSearchView(menu);
            return true;
        }
        if(findViewById(R.id.toolbar_filters).getVisibility() == View.VISIBLE) {
            getMenuInflater().inflate(R.menu.filter_menu, menu);
            return true;
        }
        if(findViewById(R.id.toolbar_horizontal).getVisibility() == View.VISIBLE){
            getMenuInflater().inflate(R.menu.share_recipe, menu);
            menu.findItem(R.id.action_share_recipe).setIcon(getResources().getDrawable(R.drawable.ic_share_grey));
            return true;

        }
        if(findViewById(R.id.toolbar_collaps).getVisibility() == View.VISIBLE) {
            getMenuInflater().inflate(R.menu.share_recipe, menu);
            return true;
        }
            return true;
    }

    private void configureSearchView(final Menu menu){
        final MenuItem search = menu.findItem(R.id.search);

        //per togliere l'overflow menu quando si apre la search view
        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                filters.setVisible(false);
                //to give focus to the search view when it is expanded
                sv.setIconified(false);
                sv.requestFocusFromTouch();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                filters.setVisible(true);
                //to hide the keyboard when back button is pressed
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                return true;
            }
        });

        sv = (SearchView) search.getActionView();

        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);

        //style cursor and other things of the search view
        SearchViewStyle.on(sv)
                .setCursorColor(getResources().getColor(R.color.light_green))
                .setTextColor(getResources().getColor(R.color.medium_grey))
                .setCloseBtnImageResource(R.drawable.ic_close);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                Intent intent = new Intent(sv.getContext(), SearchActivity.class);
                intent.putExtra(ConstantsDictionary.SEARCH_QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                //TODO autocomplete
                return true;
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose(){
                return true;
            }
        });
        sv.setSubmitButtonEnabled(false);
        sv.setIconifiedByDefault(true);

        if(initialQuery != null){
            sv.setIconified(false);
            search.expandActionView();
            sv.setQuery(initialQuery,true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        if(sv != null){
            if(!sv.isIconified()) {
                state.putCharSequence(STATE_QUERY, sv.getQuery());
            }
        }
    }

    //menu barra in alto
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_filters) {
            openFilters();
            return true;
        }
        if (id == R.id.action_close_filters) {
            onFiltersExit();
            return true;
        }
        if(id == R.id.share_recipe){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, supportFragment.getSelectedRecipe().getDetailedRecipe().sourceUrl);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
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
    public void onFiltersExit() {
        FragmentManager fragmentManager = getFragmentManager();

        //schermo grande
        if(mDualPane){
            findViewById(R.id.second_fragment_frame).setVisibility(View.GONE);
            if(supportFragment.isFilterOpen()){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(filter);
                fragmentTransaction.commit();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(filtersFragment);
            fragmentTransaction.commit();
            if(recipeListFragment == null){
                setupWithOnlyRecipeList(fragmentManager);
            }else{
                recipeListFragment.changeGridColumns(4, this);
            }
        }else{
            if(supportFragment.isFilterOpen()){
                fragmentManager.popBackStack();
            }
            fragmentManager.popBackStack();
            resetNormalToolbar();
            if(recipeListFragment == null){
                setupWithOnlyRecipeList(fragmentManager);
            }
        }

        supportFragment.closeFilterPage();


    }

    @Override
    public void onFiltersOpen(String filterToOpen) {
        filter = supportFragment.filterToOpen(filterToOpen);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(mDualPane){
            fragmentTransaction.replace(R.id.second_fragment_frame, filter);
        }else{
            fragmentTransaction.replace(R.id.first_fragment_frame, filter);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void passDislIngs(ArrayList<String> ings) {
        if(filter instanceof DietPrefFilterFragment){
            ((DietPrefFilterFragment) filter).setDislIngs(ings);
        }
    }

    @Override
    public void passDiets(ArrayList<String> diets) {
        if(filter instanceof DietPrefFilterFragment){
            ((DietPrefFilterFragment) filter).setDiets(diets);
        }
    }

    @Override
    public void passAllergies(ArrayList<String> allergies) {
        if(filter instanceof DietPrefFilterFragment){
            ((DietPrefFilterFragment) filter).setAllergies(allergies);
        }
    }

    @Override
    public void passCuisines(ArrayList<Cuisine> cuisines) {
        if(filter instanceof CuisineFilterFragment){
            ((CuisineFilterFragment) filter).setCuisines(cuisines);
        }
    }

    @Override
    public void passCourses(ArrayList<Course> courses) {
        if(filter instanceof CourseFilterFragment){
            ((CourseFilterFragment) filter).setCourses(courses);
        }
    }

    @Override
    public void passIncludedIngrs(ArrayList<IncludedIngr> ingredients) {
        if(filter instanceof InclIngrFilterFragment){
            ((InclIngrFilterFragment) filter).setIngredients(ingredients);
        }
    }

    @Override
    public void sendFavs(ArrayList<String> favs) {
        if(recipeListFragment != null){
            recipeListFragment.setFavList(favs);
        }
    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, "Error: no internet connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void sendRecipes(ArrayList<Match> recipes) {
        if(supportFragment.isFilterPageOpen()){
            return;
        }
        findViewById(R.id.recipe_list_progress_bar).setVisibility(View.GONE);

        FragmentManager fragmentManager = getFragmentManager();
        if(supportFragment.isFilterPageOpen()){
            setupWithOpenRighePanel(fragmentManager);
        }else{
            setupWithOnlyRecipeList(fragmentManager);
        }
    }

    @Override
    public void onRecipeDetailReceived() {
        setupOpenRecipe(getFragmentManager(), supportFragment.getSelectedRecipe());
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
        if(mDualPane){
            findViewById(R.id.second_fragment_frame).setVisibility(View.GONE);
            fragmentTransaction.remove(singleRecipeFragment);
            fragmentTransaction.commit();
            recipeListFragment.changeGridColumns(4, this);
            supportFragment.removeSelectedRecipe();
        }else{
            fragmentManager.popBackStack();
            resetNormalToolbar();
            supportFragment.removeSelectedRecipe();
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
        //non serve qui
    }

    private void resetNormalToolbar(){
        findViewById(R.id.toolbar_filters).setVisibility(View.GONE);
        findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_horizontal).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        setupToolbar();

    }

    private void switchToFiltersToolbar(){
        findViewById(R.id.toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_horizontal).setVisibility(View.GONE);
        findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_filters);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        setTitle(R.string.filters);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_funnel));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //serve solo a mostrare l'immagine dei filtri
            }
        });
    }

    private void changeToRecipeToolbar(Match recipe){

        findViewById(R.id.toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_filters).setVisibility(View.GONE);
        findViewById(R.id.toolbar_horizontal).setVisibility(View.GONE);

        findViewById(R.id.collapsing_toolbar).setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_collaps);
        setSupportActionBar(toolbar);

        toolbar.setTitle(recipe.getRecipeName());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_arrow_white));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    supportFragment.removeSelectedRecipe();
                }
                resetNormalToolbar();*/
                onExitSingleRecipe();
            }
        });

        Picasso.with(this)
                .load(recipe.getRecipe().getImage())
                .into((ImageView) findViewById(R.id.recipe_image));

    }

    private void horizontalRecipeToolbar(Match recipe){
        findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        findViewById(R.id.toolbar_filters).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.GONE);

        findViewById(R.id.toolbar_horizontal).setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_horizontal);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_arrow));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.orange));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    resetNormalToolbar();
                    supportFragment.removeSelectedRecipe();
                }*/
                onExitSingleRecipe();
            }
        });
        setTitle(recipe.getRecipeName());

    }

    @Override
    public void onCloseFilterFragment(String filterFragment) {
        supportFragment.closeFilter(filterFragment);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onResetAllCuisines() {
        if(filter instanceof CuisineFilterFragment){
            ((CuisineFilterFragment) filter).resetAllCuisines();
        }
        supportFragment.resetAllCuisines();
    }

    @Override
    public void onResetAllCourses() {
        if(filter instanceof CourseFilterFragment){
            ((CourseFilterFragment) filter).resetAllCourses();
        }
        supportFragment.resetAllCourses();
    }

    @Override
    public void onAddIngredient(String name) {
        AddIncludedIngrTask task = new AddIncludedIngrTask(name);
        task.execute(this);
    }

    @Override
    public void onResetFilters() {
        supportFragment.resetAllCuisines();
        supportFragment.resetAllCourses();
        supportFragment.resetPrepTime();
        supportFragment.resetIncludedIng();
    }

    @Override
    public void saveIngredients(ArrayList<FridgeIngredient> chosenIngredients) {
        supportFragment.setChosenIngredients(chosenIngredients);
    }

    @Override
    public void saveFridgeOpen(boolean fridgeOpen) {
        supportFragment.setFridgeOpen(fridgeOpen);
    }

    private class AddIncludedIngrTask extends AsyncTask<Context, Void, Long>{

        private final String name;

        public AddIncludedIngrTask(String name) {
            this.name = name;
        }

        @Override
        protected Long doInBackground(Context... contexts) {
            FilterDataSource dataSource = new FilterDataSource(contexts[0]);

            try{
                dataSource.openWritable();
            }catch (SQLException e){
                Log.e("DBEX", e.getMessage());
            }

            long id = dataSource.insertIncludedIngr(name);
            dataSource.close();
            return id;
        }

        @Override
        protected void onPostExecute(Long id){
            ((InclIngrFilterFragment) filter).addIngr(new IncludedIngr(id, name));
        }
    }
}