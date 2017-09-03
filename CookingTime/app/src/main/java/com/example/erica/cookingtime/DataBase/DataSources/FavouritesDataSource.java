package com.example.erica.cookingtime.DataBase.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erica.cookingtime.DataBase.DBContracts;
import com.example.erica.cookingtime.DataBase.DBHelpers.BaseDBHelper;
import com.example.erica.cookingtime.DataBase.DBHelpers.FavouritesDBHelper;
import com.example.erica.cookingtime.POJO.Image;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.POJO.Source;
import com.example.erica.cookingtime.POJO.YummlySingleRecipe;

import java.util.ArrayList;

public class FavouritesDataSource {

    private SQLiteDatabase db;
    private BaseDBHelper dbHelper;

    public FavouritesDataSource(Context context){
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

    public ArrayList<String> retrieveFavsIds(){
        ArrayList<String> favs = new ArrayList<>();

        String[] projection = {DBContracts.FavouritesTable.COLUMN_RECIPE_ID};

        Cursor cursor = db.query(DBContracts.FavouritesTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            favs.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return favs;
    }

    public ArrayList<Match> retrieveCompleteFavs(){
        ArrayList<Match> matches = new ArrayList<>();

        String[] projection = {DBContracts.FavouritesTable.COLUMN_RECIPE_ID,
                DBContracts.FavouritesTable.COLUMN_RECIPE_NAME,
                DBContracts.FavouritesTable.COLUMN_RATING,
                DBContracts.FavouritesTable.COLUMN_TOTAL_TIME_IN_SECONDS,
                DBContracts.FavouritesTable.COLUMN_SERVINGS,
                DBContracts.FavouritesTable.COLUMN_SOURCE_RECIPE_URL};

        Cursor cursor = db.query(DBContracts.FavouritesTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            matches.add(addMatch(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return matches;
    }

    private Match addMatch(Cursor cursor){
        Match match = matchFromCursor(cursor);
        match.setIngredients(new ArrayList<String>());

        String[] projection = {DBContracts.IngredientsTable.COLUMN_INGREDIENT};
        Cursor ingredientsCursor = db.query(DBContracts.IngredientsTable.TABLE_NAME, projection,
                DBContracts.IngredientsTable.COLUMN_RECIPE_ID+" = '"+match.getId()+"'", null, null, null, null);
        ingredientsCursor.moveToFirst();

        while(!ingredientsCursor.isAfterLast()){
            match.getIngredients().add(ingredientsCursor.getString(0));
            ingredientsCursor.moveToNext();
        }
        ingredientsCursor.close();

        match.getRecipe().setIngredientLines(new ArrayList<String>());
        String[] proj = {DBContracts.IngredientLinesTable.COLUMN_INGREDIENT};
        Cursor ingrLinesCursor = db.query(DBContracts.IngredientLinesTable.TABLE_NAME, proj,
                DBContracts.IngredientLinesTable.COLUMN_RECIPE_ID+" = '"+match.getId()+"'", null, null, null, null);
        ingrLinesCursor.moveToFirst();

        while (!ingrLinesCursor.isAfterLast()){
            match.getRecipe().getIngredientLines().add(ingrLinesCursor.getString(0));
            ingrLinesCursor.moveToNext();
        }
        ingrLinesCursor.close();

        match.getRecipe().setImages(new ArrayList<Image>());
        String[] p = {DBContracts.ImagesTable.COLUMN_LARGE_IMAGE};
        Cursor imageCursor = db.query(DBContracts.ImagesTable.TABLE_NAME, p,
                DBContracts.ImagesTable.COLUMN_RECIPE_ID+" = '"+match.getId()+"'", null, null, null, null);
        imageCursor.moveToFirst();

        while (!imageCursor.isAfterLast()){
            Image image = new Image();
            image.setHostedLargeUrl(imageCursor.getString(0));
            match.getRecipe().getImages().add(image);
            imageCursor.moveToNext();
        }
        imageCursor.close();

        return match;
    }

    private Match matchFromCursor(Cursor cursor){
        Match match = new Match();
        match.setId(cursor.getString(0));
        match.setRecipeName(cursor.getString(1));
        match.setRating(cursor.getInt(2));
        match.setTotalTimeInSeconds(cursor.getInt(3));

        YummlySingleRecipe singleRecipe = new YummlySingleRecipe();
        singleRecipe.setId(cursor.getString(0));
        singleRecipe.setName(cursor.getString(1));
        singleRecipe.setNumberOfServings(cursor.getInt(4));

        Source source = new Source();
        source.setSourceRecipeUrl(cursor.getString(5));

        singleRecipe.setSource(source);
        match.setRecipe(singleRecipe);

        return match;
    }

    public void insertInFavs(Match recipe){
        YummlySingleRecipe singleRecipe = recipe.getRecipe();
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.FavouritesTable.COLUMN_RECIPE_ID, recipe.getId());
        cv.put(DBContracts.FavouritesTable.COLUMN_RECIPE_NAME, recipe.getRecipeName());
        cv.put(DBContracts.FavouritesTable.COLUMN_RATING, recipe.getRating());
        cv.put(DBContracts.FavouritesTable.COLUMN_TOTAL_TIME_IN_SECONDS, recipe.getTotalTimeInSeconds());
        cv.put(DBContracts.FavouritesTable.COLUMN_SERVINGS, singleRecipe.getNumberOfServings());
        cv.put(DBContracts.FavouritesTable.COLUMN_SOURCE_RECIPE_URL, singleRecipe.getSource().getSourceRecipeUrl());
        db.insert(DBContracts.FavouritesTable.TABLE_NAME, null, cv);

        for(String ing : recipe.getIngredients()){
            cv = new ContentValues();
            cv.put(DBContracts.IngredientsTable.COLUMN_RECIPE_ID, recipe.getId());
            cv.put(DBContracts.IngredientsTable.COLUMN_INGREDIENT, ing);
            db.insert(DBContracts.IngredientsTable.TABLE_NAME, null, cv);

        }

        for(String ing : singleRecipe.getIngredientLines()){
            cv = new ContentValues();
            cv.put(DBContracts.IngredientLinesTable.COLUMN_RECIPE_ID, recipe.getId());
            cv.put(DBContracts.IngredientLinesTable.COLUMN_INGREDIENT, ing);
            db.insert(DBContracts.IngredientLinesTable.TABLE_NAME, null, cv);
        }

        cv = new ContentValues();
        cv.put(DBContracts.ImagesTable.COLUMN_RECIPE_ID, recipe.getId());
        cv.put(DBContracts.ImagesTable.COLUMN_LARGE_IMAGE, singleRecipe.getImage());
        db.insert(DBContracts.ImagesTable.TABLE_NAME, null, cv);
    }

    public void deleteFromFavs(String recipe_id){
        db.delete(DBContracts.FavouritesTable.TABLE_NAME,
                DBContracts.FavouritesTable.COLUMN_RECIPE_ID + " = '"+ recipe_id+"'", null);

        db.delete(DBContracts.IngredientsTable.TABLE_NAME,
                DBContracts.IngredientsTable.COLUMN_RECIPE_ID + " = '"+ recipe_id+"'", null);

        db.delete(DBContracts.IngredientLinesTable.TABLE_NAME,
                DBContracts.IngredientLinesTable.COLUMN_RECIPE_ID + " = '"+ recipe_id+"'", null);

        db.delete(DBContracts.ImagesTable.TABLE_NAME,
                DBContracts.ImagesTable.COLUMN_RECIPE_ID + " = '"+ recipe_id+"'", null);
    }
}
