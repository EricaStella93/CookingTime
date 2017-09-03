package com.example.erica.cookingtime.DataBase.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class FridgeDBHelper{

    public static final String DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.FridgeTable.TABLE_NAME + "( "
            + DBContracts.FridgeTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.FridgeTable.COLUMN_NAME + " text not null, "
            + DBContracts.FridgeTable.COLUMN_QUANTITY + " real not null, "
            + DBContracts.FridgeTable.COLUMN_CATEGORY + " text not null, "
            + DBContracts.FridgeTable.COLUMN_UDM + " text not null, "
            + DBContracts.FridgeTable.COLUMN_BEST_BEFORE + " text not null);";

}
