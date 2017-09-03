package com.example.erica.cookingtime.DataBase.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class BaseDBHelper extends SQLiteOpenHelper {

    public BaseDBHelper(Context context){
        //1 è il db version
        super(context, DBContracts.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Diet pref
        db.execSQL(DietPrefDBHelper.DIET_DATABASE_CREATE);
        db.execSQL(DietPrefDBHelper.ALLERG_DATABASE_CREATE);
        db.execSQL(DietPrefDBHelper.DISL_ING_DATABASE_CREATE);
        db.execSQL(DietPrefDBHelper.ADD_ALLERG);
        db.execSQL(DietPrefDBHelper.ADD_DIET);

        //Favs
        db.execSQL(FavouritesDBHelper.FAV_DATABASE_CREATE);
        db.execSQL(FavouritesDBHelper.INGREDIENTS_DATABASE_CREATE);
        db.execSQL(FavouritesDBHelper.INGREDIENT_LINES_DATABASE_CREATE);
        db.execSQL(FavouritesDBHelper.IMAGES_DATABASE_CREATE);

        //Filters
        db.execSQL(FilterDBhelper.CUISINE_DATABASE_CREATE);
        db.execSQL(FilterDBhelper.COURSES_DATABASE_CREATE);
        db.execSQL(FilterDBhelper.INCLUDED_INGRED_DATABASE_CREATE);
        db.execSQL(FilterDBhelper.ADD_CUISINE);
        db.execSQL(FilterDBhelper.ADD_COURSES);

        //Fridge
        db.execSQL(FridgeDBHelper.DATABASE_CREATE);

        //Shopping list
        db.execSQL(ShoppingListDBHelper.DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
