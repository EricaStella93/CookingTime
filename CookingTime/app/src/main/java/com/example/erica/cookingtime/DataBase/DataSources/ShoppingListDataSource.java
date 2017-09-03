package com.example.erica.cookingtime.DataBase.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erica.cookingtime.DataBase.DBContracts;
import com.example.erica.cookingtime.DataBase.DBHelpers.BaseDBHelper;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class ShoppingListDataSource {

    private SQLiteDatabase db;
    private BaseDBHelper dbHelper;

    public ShoppingListDataSource(Context context){
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

    public ArrayList<ShoppingListIngredient> retrieveAllShoppingListIngredients(int gotIt){

        ArrayList<ShoppingListIngredient> ingredients = new ArrayList<>();
        String[] projection = {DBContracts.ShoppingListTable.COLUMN_NAME, DBContracts.ShoppingListTable.COLUMN_CATEGORY,
                DBContracts.ShoppingListTable.COLUMN_QUANTITY, DBContracts.ShoppingListTable.COLUMN_UDM,
                DBContracts.ShoppingListTable.COLUMN_RECIPE, DBContracts.ShoppingListTable.COLUMN_GOT_IT,
                DBContracts.ShoppingListTable.COLUMN_ID};

        Cursor cursor = db.query(DBContracts.ShoppingListTable.TABLE_NAME, projection,
                DBContracts.ShoppingListTable.COLUMN_GOT_IT + " = "+ gotIt, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            ingredients.add(ingredientFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return ingredients;
    }

    private ShoppingListIngredient ingredientFromCursor(Cursor cursor){
        return new ShoppingListIngredient(cursor.getLong(6), cursor.getString(0),
                cursor.getString(1), cursor.getFloat(2), cursor.getString(3),
                cursor.getString(4), IngredientUtils.fromIntToBool(cursor.getInt(5)));
    }


    public void deleteAllShoppingList(){
        db.delete(DBContracts.ShoppingListTable.TABLE_NAME, null, null);
    }

    public void deleteGotItList(){
        db.delete(DBContracts.ShoppingListTable.TABLE_NAME,
                DBContracts.ShoppingListTable.COLUMN_GOT_IT + " = 1", null);
    }

    public void deleteSingleIngredient(long id){
        db.delete(DBContracts.ShoppingListTable.TABLE_NAME,
                DBContracts.ShoppingListTable.COLUMN_ID + " = " + id, null);
    }

    public long insertIng(String name, String aisle, float quantity, String udm, String recipe){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.ShoppingListTable.COLUMN_NAME, name);
        cv.put(DBContracts.ShoppingListTable.COLUMN_CATEGORY, aisle);
        cv.put(DBContracts.ShoppingListTable.COLUMN_QUANTITY, quantity);
        cv.put(DBContracts.ShoppingListTable.COLUMN_UDM, udm);
        cv.put(DBContracts.ShoppingListTable.COLUMN_RECIPE, recipe);
        cv.put(DBContracts.ShoppingListTable.COLUMN_GOT_IT, 0);
        return db.insert(DBContracts.ShoppingListTable.TABLE_NAME, null, cv);
    }

    public void updateId(long id, int newState){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.ShoppingListTable.COLUMN_GOT_IT, newState);
        db.update(DBContracts.ShoppingListTable.TABLE_NAME, cv,
                DBContracts.ShoppingListTable.COLUMN_ID + " = " + id, null);
    }

    public void updateId(long id, float quantity, String udm){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.ShoppingListTable.COLUMN_QUANTITY, quantity);
        cv.put(DBContracts.ShoppingListTable.COLUMN_UDM, udm);
        db.update(DBContracts.ShoppingListTable.TABLE_NAME, cv,
                DBContracts.ShoppingListTable.COLUMN_ID + " = " + id, null);
    }
}
