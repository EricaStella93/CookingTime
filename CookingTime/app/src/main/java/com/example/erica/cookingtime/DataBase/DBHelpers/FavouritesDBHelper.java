package com.example.erica.cookingtime.DataBase.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class FavouritesDBHelper{

    public static final String FAV_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.FavouritesTable.TABLE_NAME + "( "
            + DBContracts.FavouritesTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.FavouritesTable.COLUMN_RECIPE_ID + " text not null, "
            + DBContracts.FavouritesTable.COLUMN_RECIPE_NAME + " text not null, "
            + DBContracts.FavouritesTable.COLUMN_RATING + " integer not null, "
            + DBContracts.FavouritesTable.COLUMN_TOTAL_TIME_IN_SECONDS + " integer not null, "
            + DBContracts.FavouritesTable.COLUMN_SERVINGS + " integer not null, "
            + DBContracts.FavouritesTable.COLUMN_SOURCE_RECIPE_URL + " text not null);";

    public static final String INGREDIENTS_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.IngredientsTable.TABLE_NAME + "( "
            + DBContracts.IngredientsTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.IngredientsTable.COLUMN_RECIPE_ID + " text not null, "
            + DBContracts.IngredientsTable.COLUMN_INGREDIENT + " text not null);";

    public static final String INGREDIENT_LINES_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.IngredientLinesTable.TABLE_NAME + "( "
            + DBContracts.IngredientLinesTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.IngredientLinesTable.COLUMN_RECIPE_ID + " text not null, "
            + DBContracts.IngredientLinesTable.COLUMN_INGREDIENT + " text not null);";

    public static final String IMAGES_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.ImagesTable.TABLE_NAME + "( "
            + DBContracts.ImagesTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.ImagesTable.COLUMN_RECIPE_ID + " text not null, "
            + DBContracts.ImagesTable.COLUMN_LARGE_IMAGE + " text not null);";
}
