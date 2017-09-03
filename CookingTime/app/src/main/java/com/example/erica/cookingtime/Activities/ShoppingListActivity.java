package com.example.erica.cookingtime.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.erica.cookingtime.Adapters.ShoppingListAdapter;
import com.example.erica.cookingtime.Adapters.ShoppingListSectionAdapter;
import com.example.erica.cookingtime.Adapters.SquareShopListAdapter;
import com.example.erica.cookingtime.DataBase.DBModifiers.DeleteShoppingList;
import com.example.erica.cookingtime.DataBase.DataSources.ShoppingListDataSource;
import com.example.erica.cookingtime.Dialogs.AddShoppingListIngredientDialog;
import com.example.erica.cookingtime.Dialogs.ClearAllShoppingListDialog;
import com.example.erica.cookingtime.Dialogs.ClearGotItShopListDialog;
import com.example.erica.cookingtime.Dialogs.EditShopListIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListSection;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.MenuUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClearAllShoppingListDialog.ClearAllDialogListener,
        AddShoppingListIngredientDialog.AddShopListIngListener,
        ShoppingListAdapter.OnMoveIngredients,
        ClearGotItShopListDialog.ClearGotItDialogListener,
        EditShopListIngredient.OnEditIngredient {

    private boolean sortedByAisle = true;

    private ArrayList<ShoppingListIngredient> notGotList = new ArrayList<>();
    private ArrayList<ShoppingListIngredient> gotItList = new ArrayList<>();
    private RetrieveIngredientsFromDBTask task;

    private LinearLayout gotItRow;
    private RecyclerView gotItRecView;
    private ShoppingListAdapter gotItAdapter;
    private RecyclerView.LayoutManager gotItLayoutManager;

    private Map<String, ArrayList<ShoppingListIngredient>> aisleList;

    private Map<String, ArrayList<ShoppingListIngredient>> recipeList;

    private RecyclerView aisleParent;

    private boolean mDualPane;

    private static final String SORTING = "sorting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //aggiunte per far funzionare il pulsante cambiato del menù laterale
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

        RadioButton recipeButton = (RadioButton) findViewById(R.id.shopping_list_recipe);
        RadioButton aisleButton = (RadioButton) findViewById(R.id.shopping_list_aisle);

        //recipeButton.setButtonTintList(getResources().getColorStateList(R.color.radio_button_tint));
        //aisleButton.setButtonTintList(getResources().getColorStateList(R.color.radio_button_tint));

        ((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.shopping_list_aisle:
                        ((RadioButton) findViewById(R.id.shopping_list_aisle)).setChecked(true);
                        sortedByAisle = true;
                        sortByAisle();
                        ((RadioButton) findViewById(R.id.shopping_list_recipe)).setChecked(false);
                        break;
                    case R.id.shopping_list_recipe:
                        ((RadioButton) findViewById(R.id.shopping_list_recipe)).setChecked(true);
                        sortedByAisle = false;
                        sortByRecipe();
                        ((RadioButton) findViewById(R.id.shopping_list_aisle)).setChecked(false);
                        break;
                }
            }
        });

        aisleParent = (RecyclerView) findViewById(R.id.aisle_parent);
        mDualPane = findViewById(R.id.second_view_stub) != null;
        //faccio partire gli ingredienti ordinati per aisle
        if(savedInstanceState != null){
            sortedByAisle = savedInstanceState.getBoolean(SORTING);
        }else{
            sortedByAisle = true;
        }
        if(sortedByAisle){
            aisleButton.setChecked(true);
        }else{
            recipeButton.setChecked(true);
        }
        setupGotIt();

        if(task == null){
            task = new RetrieveIngredientsFromDBTask();
            task.execute(this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putBoolean(SORTING, sortedByAisle);
    }

    private void setupGotIt(){

        //set up rec view for got it ings
        gotItRecView = (RecyclerView) findViewById(R.id.shopping_list_got_it_rec_view);
        gotItRow = (LinearLayout) findViewById(R.id.got_it_row);
        findViewById(R.id.clear_got_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ClearGotItShopListDialog dialog = new ClearGotItShopListDialog();
                dialog.show(getSupportFragmentManager(), "clear_got_it");
            }
        });
        gotItRow.setVisibility(View.GONE);
        //schermo grande
        if(mDualPane){
            gotItLayoutManager = new LinearLayoutManager(this);
            gotItAdapter = new ShoppingListAdapter(gotItList, this, gotItRow, this);
            gotItRecView.setLayoutManager(gotItLayoutManager);
            gotItRecView.setAdapter(gotItAdapter);
            return;
        }
        //schermo orizzontale
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gotItLayoutManager = new LinearLayoutManager(this);
            gotItAdapter = new ShoppingListAdapter(gotItList, this, gotItRow, this);
            gotItRecView.setLayoutManager(gotItLayoutManager);
            gotItRecView.setAdapter(gotItAdapter);
            return;
        }
        //schermo piccolo portrait
        gotItLayoutManager = new LinearLayoutManager(this);
        gotItRecView.setLayoutManager(gotItLayoutManager);
        gotItAdapter = new ShoppingListAdapter(gotItList, this, gotItRow, this);
        gotItRecView.setAdapter(gotItAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.shopping_list_clear_all) {
            showClearAllDialog();
            return true;
        }
        if (id == R.id.shopping_list_email_list) {
            emailShoppingList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return MenuUtils.menuNavigation(item, this);
    }

    public void openAddIngredientDialog(View v){
        final AddShoppingListIngredientDialog dialog = new AddShoppingListIngredientDialog();
        dialog.show(getSupportFragmentManager(), "add_ing");
    }

    private void showClearAllDialog(){
        final ClearAllShoppingListDialog dialog = new ClearAllShoppingListDialog();
        dialog.show(getSupportFragmentManager(), "clear_all");
    }

    private void emailShoppingList(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        String text = generateEmailText();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Shopping List");
        startActivity(intent);
    }

    private String generateEmailText(){

        Map<String, ArrayList<String>> aList = new HashMap<>();

        for(ShoppingListIngredient ing : notGotList){
            if(aList.containsKey(ing.getAisle())){

                ArrayList<String> stringList = aList.get(ing.getAisle());
                float quantity = ing.getQuantity();
                String quant;
                if (quantity <= 0) {
                    quant = "";
                }else{
                    quant = String.valueOf(quantity);
                }
                String udm = ing.getUdm();
                if(udm == null){
                    udm = "";
                }
                stringList.add(quant + " " + udm + " " + ing.getName());

            }else{
                ArrayList<String> stringList = new ArrayList<>();
                float quantity = ing.getQuantity();
                String quant;
                if (quantity <= 0) {
                    quant = "";
                }else{
                    quant = String.valueOf(quantity);
                }
                String udm = ing.getUdm();
                if(udm == null){
                    udm = "";
                }
                stringList.add(quant + " " + udm + " " + ing.getName());
                aList.put(ing.getAisle(), stringList);
            }
        }

        StringBuilder string = new StringBuilder();
        string.append("Here's my list:\n");

        for(String key : aList.keySet()){
            string.append(key.toUpperCase()+"\n");
            for(String el : aList.get(key)){
                string.append("- "+el+"\n");
            }
        }

        return string.toString();
    }

    @Override
    public void onClearAllChoice(){
        DeleteShoppingList task = new DeleteShoppingList(ConstantsDictionary.ALL, 0);
        task.execute(this);

        aisleParent.removeAllViews();
        gotItAdapter.removeAll();
    }

    private void sortByRecipe(){
        if(notGotList != null && !notGotList.isEmpty()){

            recipeList = new HashMap<>();

            //riordino tutti gli ingredienti che non ho segnato come presi
            for(int i = 0; i < notGotList.size(); i++){

                ShoppingListIngredient ing = notGotList.get(i);

                String recipeName = ing.getRecipe();
                if(recipeName == null || "".equals(recipeName)){
                    recipeName = getResources().getString(R.string.not_in_recipe);
                }else{
                    recipeName = recipeName.toUpperCase();
                }

                //se la ricetta è già presente nella map
                if(recipeList.containsKey(recipeName)){
                    recipeList.get(recipeName).add(ing);

                }
                //se non ho un ingrediente con la stessa ricetta creo un nuovo spazio per la ricetta
                else{
                    ArrayList<ShoppingListIngredient> recipeIngList = new ArrayList<>();
                    recipeIngList.add(ing);
                    recipeList.put(recipeName, recipeIngList);
                }
            }

            ArrayList<ShoppingListSection> sections = new ArrayList<>();
            for(String aisle: recipeList.keySet()){
                sections.add(new ShoppingListSection(aisle, recipeList.get(aisle)));
            }

            setupSectionRecView(sections);

        }
        if(notGotList.isEmpty() && aisleParent.getAdapter() != null){
            ((ShoppingListSectionAdapter)aisleParent.getAdapter()).removeAll();
        }
    }

    private void setupSectionRecView(ArrayList<ShoppingListSection> sections){
        //schermo grande
        if(findViewById(R.id.second_view_stub) != null){
            RecyclerView.LayoutManager layMan = new LinearLayoutManager(this);
            aisleParent.setLayoutManager(layMan);
            ShoppingListSectionAdapter adapter = new ShoppingListSectionAdapter(sections, this, null, ConstantsDictionary.ROW, this);
            aisleParent.setAdapter(adapter);
            return;
        }

        //schermo orizzontale
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            RecyclerView.LayoutManager layMan = new LinearLayoutManager(this);
            aisleParent.setLayoutManager(layMan);
            ShoppingListSectionAdapter adapter = new ShoppingListSectionAdapter(sections, this, null, ConstantsDictionary.ROW, this);
            aisleParent.setAdapter(adapter);
            return;
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        aisleParent.setLayoutManager(layoutManager);
        ShoppingListSectionAdapter adapter = new ShoppingListSectionAdapter(sections, this, null, ConstantsDictionary.ROW, this);
        aisleParent.setAdapter(adapter);
    }

    private void sortByAisle(){
        if(notGotList != null && !notGotList.isEmpty()){

            aisleList = new HashMap<>();

            //prendo tutte le aisle degli ingredienti e li divido per aisle
            for(int i = 0; i < notGotList.size(); i++){
                ShoppingListIngredient ing = notGotList.get(i);
                if(aisleList.containsKey(ing.getAisle())){
                    aisleList.get(ing.getAisle()).add(ing);
                }else{
                    ArrayList<ShoppingListIngredient> ingList = new ArrayList<>();
                    ingList.add(ing);
                    aisleList.put(ing.getAisle(), ingList);
                }
            }

            ArrayList<ShoppingListSection> sections = new ArrayList<>();
            for(String aisle: aisleList.keySet()){
                sections.add(new ShoppingListSection(aisle, aisleList.get(aisle)));
            }

            setupSectionRecView(sections);

        }
        if(notGotList.isEmpty() && aisleParent.getAdapter() != null){
            ((ShoppingListSectionAdapter)aisleParent.getAdapter()).removeAll();
        }
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        ((RadioButton) view).setChecked(checked);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.shopping_list_aisle:
                if (checked){
                    sortedByAisle = true;
                    sortByAisle();
                    //((RadioButton) findViewById(R.id.shopping_list_recipe)).setChecked(false);
                }

                //((RadioButton) view).setChecked(checked);
                break;
            case R.id.shopping_list_recipe:
                if (checked){
                    sortedByAisle = false;
                    sortByRecipe();
                    //((RadioButton) findViewById(R.id.shopping_list_aisle)).setChecked(false);
                }

                //((RadioButton) view).setChecked(checked);
                break;

        }
    }

    @Override
    public void onAddIngredient(String name, float quantity, String udm, String aisle, String recipe) {
        AddShopListIngTask task = new AddShopListIngTask(name, aisle, quantity, udm, recipe);
        task.execute(this);
    }

    @Override
    public void moveIngredient(boolean checked, ShoppingListIngredient ingredient) {
        //passo da non got it a got it
        if(checked){
            notGotList.remove(ingredient);
            if(sortedByAisle){
                sortByAisle();
            }
            else{
                sortByRecipe();
            }
            moveToGotIt(ingredient);
        }
        //passo da got it a non got it
        else{
            gotItList.remove(ingredient);
            moveFromGotIt(ingredient);
        }
    }

    @Override
    public void onClearChoice() {
        gotItAdapter.removeAll();
        gotItList.clear();
        DeleteShoppingList task = new DeleteShoppingList(ConstantsDictionary.GOT_IT, 0);
        task.execute(this);
    }

    @Override
    public void onIngredientEdited(ShoppingListIngredient ing) {
        if(ing.isGotIt()){
            gotItAdapter.notifyDataSetChanged();
        }else{
            resort();
        }
    }

    private class RetrieveIngredientsFromDBTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... contexts){
            ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DBEX", "ECCEZIONE NEL DB");
            }
            ArrayList<ShoppingListIngredient> notGotIngs = dataSource.retrieveAllShoppingListIngredients(0);
            ArrayList<ShoppingListIngredient> gotIngs = dataSource.retrieveAllShoppingListIngredients(1);

            dataSource.close();

            notGotList.addAll(notGotIngs);
            gotItAdapter.addAll(gotIngs);
            return null;
        }

        @Override
        protected void onPostExecute(Void value){
            if(!isCancelled()){
                gotItAdapter.notifyDataSetChanged();
                resort();

            }
        }
    }

    private class AddShopListIngTask extends AsyncTask<Context, Void, Long> {

        private final String name;
        private final String aisle;
        private final float quantity;
        private final String udm;
        private final String recipe;

        public AddShopListIngTask(String name, String aisle, float quantity, String udm, String recipe) {
            this.name = name;
            this.aisle = aisle;
            this.quantity = quantity;
            this.udm = udm;
            this.recipe = recipe;
        }

        @Override
        protected Long doInBackground(Context... contexts) {

            ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);

            try{
                dataSource.openWritable();
            }catch (SQLException e){
                Log.e("DBEX", e.getMessage());
            }

            long id = dataSource.insertIng(name, aisle, quantity, udm, recipe);
            dataSource.close();
            return id;
        }

        @Override
        protected void onPostExecute(Long id){
            if(!isCancelled()){
                ShoppingListIngredient ing = new ShoppingListIngredient(id, this.name, this.aisle, this.quantity, this.udm, this.recipe, false);
                notGotList.add(ing);
                resort();

            }

        }
    }

    private void moveToGotIt(ShoppingListIngredient ingredient){
        gotItAdapter.add(gotItList.size(), ingredient);
        if(notGotList.isEmpty()){
            ((ShoppingListSectionAdapter)aisleParent.getAdapter()).removeAll();
        }
    }


    private void moveFromGotIt(ShoppingListIngredient ingredient){
        notGotList.add(ingredient);
        resort();

    }

    public void resort(){
        if(sortedByAisle){
            sortByAisle();
        }else {
            sortByRecipe();
        }
    }

    @Override
    public void resort(ShoppingListIngredient ing){
        if(!ing.isGotIt()){
            notGotList.remove(ing);
            resort();
        }else{
            gotItList.remove(ing);
        }
    }

}
