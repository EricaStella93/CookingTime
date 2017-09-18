package com.example.erica.cookingtime.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.erica.cookingtime.Adapters.FridgeAdapter;
import com.example.erica.cookingtime.Adapters.FridgeSectionAdapter;
import com.example.erica.cookingtime.Adapters.ShoppingListSectionAdapter;
import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyFridge;
import com.example.erica.cookingtime.DataBase.DataSources.FridgeDataSource;
import com.example.erica.cookingtime.Dialogs.AddFridgeIngredientDialog;
import com.example.erica.cookingtime.Dialogs.ClearAllFridgeDialog;
import com.example.erica.cookingtime.Notifications.NotificationReceiver;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.FridgeSection;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.POJO.ShoppingListSection;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;
import com.example.erica.cookingtime.Utils.MenuUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class FridgeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClearAllFridgeDialog.ClearAllDialogListener,
        AddFridgeIngredientDialog.AddIngredientDialogListener,
        FridgeAdapter.OnEditIngredients
{

    private ArrayList<FridgeIngredient> ingredientList = new ArrayList<>();
    private RetrieveIngredientsFromDBTask task;

    private RecyclerView listParent;
    private ProgressBar progressBar;

    private Map<String, ArrayList<FridgeIngredient>> aisleList;

    private Map<Date, ArrayList<FridgeIngredient>> dateList;

    private static final String SORTING = "sorting";


    private boolean mDualPane;
    private boolean sortedByAisle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        if(ingredientList.isEmpty() && task == null){
            this.task = new RetrieveIngredientsFromDBTask();
            task.execute(this);
        }

        setContentView(R.layout.activity_fridge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        //foto sopra al menù laterale
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.fridge_menu).setChecked(true);

        //faccio partire gli ingredienti ordinati per aisle
        if(savedInstanceState != null){
            sortedByAisle = savedInstanceState.getBoolean(SORTING);
        }else{
            sortedByAisle = true;
        }
        if(sortedByAisle){
            RadioButton aisleButton = (RadioButton) findViewById(R.id.fridge_aisle);
            aisleButton.setChecked(true);
        }else{
            RadioButton dateButton = (RadioButton) findViewById(R.id.fridge_date);
            dateButton.setChecked(true);
        }

        listParent = (RecyclerView) findViewById(R.id.list_parent);
        progressBar = (ProgressBar) findViewById(R.id.fridge_loading_progress_bar);

        if(findViewById(R.id.second_view_stub) != null){
            mDualPane = true;
        }else{
            mDualPane = false;
        }

        horizontalToolbar(toolbar);
    }

    private void horizontalToolbar(Toolbar toolbar){
        if(!mDualPane && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            toolbar.setTitleTextColor(getResources().getColor(R.color.orange));
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

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putBoolean(SORTING, sortedByAisle);
    }

    //menu in alto
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fridge, menu);
        if(!mDualPane && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ((Toolbar) findViewById(R.id.toolbar)).setOverflowIcon(getResources().getDrawable(R.drawable.ic_overflow_dark));
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
        if (id == R.id.fridge_email_list) {
            emailFridgeList();
            return true;
        }
        if(id == R.id.fridge_clear_all){
            showClearAllDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //menu laterale
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return MenuUtils.menuNavigation(item, this);
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.fridge_aisle:
                if (checked)
                    sortByAisle();
                break;
            case R.id.fridge_date:
                if (checked)
                    sortByDate();
                break;

        }
    }

    private void sortByAisle(){
        sortedByAisle = true;
        if(ingredientList != null || ingredientList.isEmpty()){

            aisleList = new HashMap<>();

            //prendo tutte le aisle degli ingredienti e li divido per aisle
            for(int i = 0; i < ingredientList.size(); i++){
                FridgeIngredient ing = ingredientList.get(i);
                if(aisleList.containsKey(ing.getCategory())){
                    aisleList.get(ing.getCategory()).add(ing);
                }else{
                    ArrayList<FridgeIngredient> ingList = new ArrayList<>();
                    ingList.add(ing);
                    aisleList.put(ing.getCategory(), ingList);
                }
            }

            ArrayList<FridgeSection> sections = new ArrayList<>();
            for(String aisle: aisleList.keySet()){
                sections.add(new FridgeSection(aisle, aisleList.get(aisle)));
            }

            setupSectionRecView(sections);
        }

        if(ingredientList.isEmpty() && listParent.getAdapter() != null){
            ((FridgeSectionAdapter)listParent.getAdapter()).removeAll();
        }
    }

    private void setupSectionRecView(ArrayList<FridgeSection> sections){
        LinearLayoutManager layMan = new LinearLayoutManager(this);
        listParent.setLayoutManager(layMan);
        FridgeSectionAdapter adapter = new FridgeSectionAdapter(sections, ConstantsDictionary.ROW, this);
        listParent.setAdapter(adapter);
    }

    private void sortByDate(){
        sortedByAisle = false;
        if(ingredientList != null || ingredientList.isEmpty()){
            dateList = new HashMap<>();

            for(int i = 0; i < ingredientList.size(); i++){
                FridgeIngredient ing = ingredientList.get(i);
                if(dateList.containsKey(ing.getBestBefore())){
                    dateList.get(ing.getBestBefore()).add(ing);
                }else{
                    ArrayList<FridgeIngredient> ingList = new ArrayList<>();
                    ingList.add(ing);
                    dateList.put(ing.getBestBefore(), ingList);
                }
            }

            ArrayList<FridgeSection> sections = new ArrayList<>();
            for(Date date: dateList.keySet()){
                sections.add(new FridgeSection(
                        IngredientUtils.fromDateToString(date), dateList.get(date)));
            }

            setupSectionRecView(sections);

        }

        if(ingredientList.isEmpty() && listParent.getAdapter() != null){
            ((FridgeSectionAdapter)listParent.getAdapter()).removeAll();
        }
    }

    private void emailFridgeList(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        String text = generateEmailText();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Fridge");
        startActivity(intent);
    }

    private String generateEmailText(){

        Map<String, ArrayList<String>> aList = new HashMap<>();

        for(FridgeIngredient ing : ingredientList){
            if(aList.containsKey(ing.getCategory())){
                ArrayList<String> stringList = aList.get(ing.getCategory());
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
                aList.put(ing.getCategory(), stringList);
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

    private void showClearAllDialog(){
        final ClearAllFridgeDialog dialog = new ClearAllFridgeDialog();
        dialog.show(getSupportFragmentManager(), "clear_all");
    }

    private void clearAllFridge(){
        ingredientList.clear();
        if(listParent.getAdapter()!= null){
            ((FridgeSectionAdapter) listParent.getAdapter()).removeAll();
        }
        ModifyFridge task = new ModifyFridge(ConstantsDictionary.ALL, 0);
        task.execute(this);
    }

    public void openAddIngredientDialog(View view){
        final AddFridgeIngredientDialog dialog = new AddFridgeIngredientDialog();
        dialog.show(getSupportFragmentManager(), "add_ingredient");
    }

    @Override
    public void resort(FridgeIngredient ing) {
        ingredientList.remove(ing);
        if(sortedByAisle){
            sortByAisle();
        }else {
            sortByDate();
        }
    }

    @Override
    public void onIngredientEdited(FridgeIngredient ing) {
        if(sortedByAisle){
            sortByAisle();
        }else {
            sortByDate();
        }
    }

    class RetrieveIngredientsFromDBTask extends AsyncTask<Context, Void, ArrayList<FridgeIngredient>> {

        @Override
        protected ArrayList<FridgeIngredient> doInBackground(Context... contexts){
            FridgeDataSource dataSource = new FridgeDataSource(contexts[0]);
            try{
                dataSource.openReadOnly();
            }catch (SQLException e){
                Log.e("DBEX", "ECCEZIONE NEL DB");
            }
            ArrayList<FridgeIngredient> ingredients = dataSource.retrieveAllFridgeIngredients();
            dataSource.close();

            return ingredients;
        }

        @Override
        protected void onPostExecute(ArrayList<FridgeIngredient> ingredients){
            if(!isCancelled()){
                ingredientList = ingredients;
                progressBar.setVisibility(View.GONE);
                if(sortedByAisle){
                    sortByAisle();
                }
                else{
                    sortByDate();
                }
            }
        }
    }

    @Override
    public void onClearAllChoice(){
        clearAllFridge();
    }

    @Override
    public void onAddIngredient(String name, float quant, String aisle, String date, String udm){
        AddFridgeIngredientToDB task = new AddFridgeIngredientToDB(name, quant, aisle, udm, date);
        task.execute(this);

        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        Date bestBefore = IngredientUtils.fromStringToDate(date);
        Date notifDate = new Date(bestBefore.getTime() - (2*DAY_IN_MS));

        Date now = new Date();
        if(notifDate.before(now)){
            //se mancano meno di due giorni dalla scadenza mando la notifica in 5 minuti
            notifDate = new Date(now.getTime() + 1000 * 60 * 5);
        }

        Intent myIntent = new Intent(this, NotificationReceiver.class);
        myIntent.putExtra(ConstantsDictionary.INGREDIENT_NAME, name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, notifDate.getTime(), pendingIntent);
    }

    private class AddFridgeIngredientToDB extends AsyncTask<Context, Void, Long>{

        private final String name;
        private final float quant;
        private final String aisle;
        private final String udm;
        private final String date;

        public AddFridgeIngredientToDB(String name, float quant, String aisle, String udm, String date) {
            this.name = name;
            this.quant = quant;
            this.aisle = aisle;
            this.udm = udm;
            this.date = date;
        }

        @Override
        protected Long doInBackground(Context... contexts) {
            FridgeDataSource dataSource = new FridgeDataSource(contexts[0]);
            try{
                dataSource.openWritable();
            }catch (SQLException e){
                Log.e(" DB EX", e.getMessage());
            }
            long id = dataSource.insertIngredient(name, aisle, quant, udm, date);
            dataSource.close();
            return id;
        }

        @Override
        protected void onPostExecute(Long id){
            if(!isCancelled()){
                FridgeIngredient ing = new FridgeIngredient(name, quant, aisle, date, udm, id);
                ingredientList.add(ing);
                if(sortedByAisle){
                    sortByAisle();
                }
                else{
                    sortByDate();
                }
            }
        }
    }
}
