package com.example.erica.cookingtime.DataBase.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class DietPrefDBHelper{

    public static final String DIET_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.DietsTable.TABLE_NAME + "( "
            + DBContracts.DietsTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.DietsTable.COLUMN_NAME + " text not null, "
            + DBContracts.DietsTable.COLUMN_YUMMLY_CODE + " text not null, "
            + DBContracts.DietsTable.COLUMN_CHOSEN + " integer default 0);";

    public static final String ALLERG_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.AllergiesTable.TABLE_NAME + "( "
            + DBContracts.AllergiesTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.AllergiesTable.COLUMN_NAME + " text not null, "
            + DBContracts.AllergiesTable.COLUMN_YUMMLY_CODE + " text not null, "
            + DBContracts.AllergiesTable.COLUMN_CHOSEN + " integer default 0);";

    public static final String DISL_ING_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.DislikedIngredientsTable.TABLE_NAME + "( "
            + DBContracts.DislikedIngredientsTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.DislikedIngredientsTable.COLUMN_NAME + " text not null);";

    public static final String ADD_DIET = "INSERT INTO "
            + DBContracts.DietsTable.TABLE_NAME + " ( "
            + DBContracts.DietsTable.COLUMN_NAME + ", "
            + DBContracts.DietsTable.COLUMN_YUMMLY_CODE + ", "
            + DBContracts.DietsTable.COLUMN_CHOSEN + ") VALUES "
            + "('Vegan', '386^Vegan', 0), "
            + "('Vegetarian', '387^Lacto-ovo vegetarian', 0), "
            + "('Lacto vegetarian', '388^Lacto vegetarian', 0), "
            + "('Ovo vegetarian', '389^Ovo vegetarian', 0), "
            + "('Pescetarian', '390^Pescetarian', 0), "
            + "('Paleo', '403^Paleo', 0);";

    public static final String ADD_ALLERG = "INSERT INTO "
            + DBContracts.AllergiesTable.TABLE_NAME + " ( "
            + DBContracts.AllergiesTable.COLUMN_NAME + ", "
            + DBContracts.AllergiesTable.COLUMN_YUMMLY_CODE + ", "
            + DBContracts.AllergiesTable.COLUMN_CHOSEN + ") VALUES "
            + "('Gluten-Free', '393^Gluten-Free', 0), "
            + "('Dairy-Free', '392^Wheat-Free', 0), "
            + "('Peanut-Free', '394^Peanut-Free', 0), "
            + "('Seafood-Free', '398^Seafood-Free', 0), "
            + "('Sesame-Free', '399^Sesame-Free', 0), "
            + "('Soy-Free', '400^Soy-Free', 0), "
            + "('Egg-Free', '397^Egg-Free', 0), "
            + "('Sulfite-Free', '401^Sulfite-Free', 0), "
            + "('Tree Nut-Free', '395^Tree Nut-Free', 0), "
            + "('Wheat-Free', '400^Soy-Free', 0);";

}
