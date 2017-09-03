package com.example.erica.cookingtime.DataBase.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class ShoppingListDBHelper{

    public static final String DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.ShoppingListTable.TABLE_NAME + "( "
            + DBContracts.ShoppingListTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.ShoppingListTable.COLUMN_NAME + " text not null, "
            + DBContracts.ShoppingListTable.COLUMN_QUANTITY + " real not null, "
            + DBContracts.ShoppingListTable.COLUMN_CATEGORY + " text not null, "
            + DBContracts.ShoppingListTable.COLUMN_UDM + " text not null, "
            + DBContracts.ShoppingListTable.COLUMN_GOT_IT + " integer default 0, "
            + DBContracts.ShoppingListTable.COLUMN_RECIPE + " text not null);";

}