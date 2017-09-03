package com.example.erica.cookingtime.DataBase.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erica.cookingtime.DataBase.DBContracts;
import com.example.erica.cookingtime.DataBase.DBHelpers.BaseDBHelper;
import com.example.erica.cookingtime.DataBase.DBHelpers.DietPrefDBHelper;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class DietPrefDataSource {

    private SQLiteDatabase db;
    private BaseDBHelper dbHelper;

    public DietPrefDataSource(Context context){
        this.dbHelper = new BaseDBHelper(context);
    }

    public void openReadOnly() throws SQLException {
        db = dbHelper.getReadableDatabase();
    }

    public void openWritable() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<Allergy> retrieveAllergies(){
        ArrayList<Allergy> allergies = new ArrayList<>();

        String[] projection = {DBContracts.AllergiesTable.COLUMN_ID,
                DBContracts.AllergiesTable.COLUMN_NAME,
                DBContracts.AllergiesTable.COLUMN_YUMMLY_CODE,
                DBContracts.AllergiesTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.AllergiesTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            allergies.add(allergyFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return allergies;
    }

    public ArrayList<Allergy> retrieveChosenAllergies(){
        ArrayList<Allergy> allergies = new ArrayList<>();

        String[] projection = {DBContracts.AllergiesTable.COLUMN_ID,
                DBContracts.AllergiesTable.COLUMN_NAME,
                DBContracts.AllergiesTable.COLUMN_YUMMLY_CODE,
                DBContracts.AllergiesTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.AllergiesTable.TABLE_NAME, projection, DBContracts.AllergiesTable.COLUMN_CHOSEN+" = 1", null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            allergies.add(allergyFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return allergies;
    }

    private Allergy allergyFromCursor(Cursor cursor){
        return new Allergy(cursor.getLong(0),
                cursor.getString(1), cursor.getString(2),
                IngredientUtils.fromIntToBool(cursor.getInt(3)));
    }

    public ArrayList<Diet> retrieveDiets(){
        ArrayList<Diet> diets = new ArrayList<>();

        String[] projection = {DBContracts.DietsTable.COLUMN_ID,
                DBContracts.DietsTable.COLUMN_NAME,
                DBContracts.DietsTable.COLUMN_YUMMLY_CODE,
                DBContracts.DietsTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.DietsTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            diets.add(dietFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return diets;
    }

    public ArrayList<Diet> retrieveChosenDiets(){
        ArrayList<Diet> diets = new ArrayList<>();

        String[] projection = {DBContracts.DietsTable.COLUMN_ID,
                DBContracts.DietsTable.COLUMN_NAME,
                DBContracts.DietsTable.COLUMN_YUMMLY_CODE,
                DBContracts.DietsTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.DietsTable.TABLE_NAME, projection, DBContracts.DietsTable.COLUMN_CHOSEN+" = 1", null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            diets.add(dietFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return diets;
    }

    private Diet dietFromCursor(Cursor cursor){
        return new Diet(cursor.getLong(0),
                cursor.getString(1), cursor.getString(2),
                IngredientUtils.fromIntToBool(cursor.getInt(3)));
    }

    public ArrayList<DislikedIng> retrieveDislikedIng(){

        ArrayList<DislikedIng> ings = new ArrayList<>();
        String[] projection = {DBContracts.DislikedIngredientsTable.COLUMN_ID,
                                DBContracts.DislikedIngredientsTable.COLUMN_NAME};

        Cursor cursor = db.query(DBContracts.DislikedIngredientsTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            ings.add(dislIngFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return ings;
    }

    private DislikedIng dislIngFromCursor(Cursor cursor){
        return new DislikedIng(cursor.getLong(0), cursor.getString(1));
    }

    public void updateAllergy(long id, int newState){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.AllergiesTable.COLUMN_CHOSEN, newState);
        db.update(DBContracts.AllergiesTable.TABLE_NAME, cv,
                DBContracts.AllergiesTable.COLUMN_ID + " = " + id, null);
    }

    public void updateDiet(long id, int newState){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.DietsTable.COLUMN_CHOSEN, newState);
        db.update(DBContracts.DietsTable.TABLE_NAME, cv,
                DBContracts.DietsTable.COLUMN_ID + " = " + id, null);
    }

    public long addDislIng(String name){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.DislikedIngredientsTable.COLUMN_NAME, name);
        return db.insert(DBContracts.DislikedIngredientsTable.TABLE_NAME,
                null, cv);
    }

    public void removeDislIng(long id){
        db.delete(DBContracts.DislikedIngredientsTable.TABLE_NAME, DBContracts.DislikedIngredientsTable.COLUMN_ID + " = " + id, null);
    }
}
