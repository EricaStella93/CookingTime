package com.example.erica.cookingtime.DataBase.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erica.cookingtime.DataBase.DBContracts;
import com.example.erica.cookingtime.DataBase.DBHelpers.BaseDBHelper;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;
import java.util.Date;

public class FridgeDataSource {

    private SQLiteDatabase db;
    private BaseDBHelper dbHelper;

    public FridgeDataSource(Context context){
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

    public ArrayList<FridgeIngredient> retrieveAllFridgeIngredients() {

        ArrayList<FridgeIngredient> ingredients = new ArrayList<>();

        String[] projection = {DBContracts.FridgeTable.COLUMN_NAME,
                DBContracts.FridgeTable.COLUMN_CATEGORY, DBContracts.FridgeTable.COLUMN_QUANTITY,
                DBContracts.FridgeTable.COLUMN_UDM, DBContracts.FridgeTable.COLUMN_BEST_BEFORE,
                DBContracts.FridgeTable.COLUMN_ID};

        Cursor cursor = db.query(DBContracts.FridgeTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            ingredients.add(ingredientFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        
        return ingredients;
    }

    private FridgeIngredient ingredientFromCursor(Cursor cursor){
        return new FridgeIngredient(cursor.getString(0),
                cursor.getFloat(2), cursor.getString(1),
                cursor.getString(4), cursor.getString(3), cursor.getLong(5));
    }

    public void deleteAllIngredients(){
        db.delete(DBContracts.FridgeTable.TABLE_NAME, null, null);
    }

    public long insertIngredient(String name, String aisle, float quant, String udm, String date){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.FridgeTable.COLUMN_NAME, name);
        cv.put(DBContracts.FridgeTable.COLUMN_CATEGORY, aisle);
        cv.put(DBContracts.FridgeTable.COLUMN_QUANTITY, quant);
        cv.put(DBContracts.FridgeTable.COLUMN_UDM, udm);
        cv.put(DBContracts.FridgeTable.COLUMN_BEST_BEFORE, date);
        return db.insert(DBContracts.FridgeTable.TABLE_NAME, null, cv);
    }

    public void deleteIngredient(long id){
        db.delete(DBContracts.FridgeTable.TABLE_NAME,
                DBContracts.FridgeTable.COLUMN_ID+" = "+id, null);

    }

    public void updateIngredient(long id, float quant, String udm, String date){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.FridgeTable.COLUMN_QUANTITY, quant);
        cv.put(DBContracts.FridgeTable.COLUMN_UDM, udm);
        cv.put(DBContracts.FridgeTable.COLUMN_BEST_BEFORE, date);
        db.update(DBContracts.FridgeTable.TABLE_NAME, cv,
                DBContracts.FridgeTable.COLUMN_ID+" = "+id, null);
    }
}
