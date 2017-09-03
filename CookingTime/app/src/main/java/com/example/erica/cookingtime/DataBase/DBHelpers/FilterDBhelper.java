package com.example.erica.cookingtime.DataBase.DBHelpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erica.cookingtime.DataBase.DBContracts;

public class FilterDBhelper{

    public static final String CUISINE_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.CuisineTable.TABLE_NAME + "( "
            + DBContracts.CuisineTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.CuisineTable.COLUMN_NAME + " text not null, "
            + DBContracts.CuisineTable.COLUMN_YUMMLY_CODE + " text not null, "
            + DBContracts.CuisineTable.COLUMN_CHOSEN + " integer default 0);";

    public static final String COURSES_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.CoursesTable.TABLE_NAME + "( "
            + DBContracts.CoursesTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.CoursesTable.COLUMN_NAME + " text not null, "
            + DBContracts.CoursesTable.COLUMN_YUMMLY_CODE + " text not null, "
            + DBContracts.CoursesTable.COLUMN_CHOSEN + " integer default 0);";

    public static final String INCLUDED_INGRED_DATABASE_CREATE = "create table IF NOT EXISTS "
            + DBContracts.IncludedIngrTable.TABLE_NAME + "( "
            + DBContracts.IncludedIngrTable.COLUMN_ID + " integer primary key autoincrement, "
            + DBContracts.IncludedIngrTable.COLUMN_NAME + " text not null);";

    public static final String ADD_CUISINE = "INSERT INTO "
            + DBContracts.CuisineTable.TABLE_NAME + " ( "
            + DBContracts.CuisineTable.COLUMN_NAME + ", "
            + DBContracts.CuisineTable.COLUMN_YUMMLY_CODE + ", "
            + DBContracts.CuisineTable.COLUMN_CHOSEN + ") VALUES "
            + "('American', 'cuisine^cuisine-american', 0), "
            + "('Kid-Friendly', 'cuisine^cuisine-kid-friendly', 0), "
            + "('Italian', 'cuisine^cuisine-italian', 0), "
            + "('Asian', 'cuisine^cuisine-asian', 0), "
            + "('Mexican', 'cuisine^cuisine-mexican', 0), "
            + "('French', 'cuisine^cuisine-french', 0), "
            + "('Southwestern', 'cuisine^cuisine-southwestern', 0), "
            + "('Barbecue', 'cuisine^cuisine-barbecue-bbq', 0), "
            + "('Indian', 'cuisine^cuisine-indian', 0), "
            + "('Chinese', 'cuisine^cuisine-chinese', 0), "
            + "('Cajun & Creole', 'cuisine^cuisine-cajun', 0), "
            + "('Mediterranean', 'cuisine^cuisine-mediterranean', 0), "
            + "('Greek', 'cuisine^cuisine-greek', 0), "
            + "('English', 'cuisine^cuisine-english', 0), "
            + "('Spanish', 'cuisine^cuisine-spanish', 0), "
            + "('Thai', 'cuisine^cuisine-thai', 0), "
            + "('German', 'cuisine^cuisine-german', 0), "
            + "('Moroccan', 'cuisine^cuisine-moroccan', 0), "
            + "('Irish', 'cuisine^cuisine-irish', 0), "
            + "('Japanese', 'cuisine^cuisine-japanese', 0), "
            + "('Cuban', 'cuisine^cuisine-cuban', 0), "
            + "('Hawaiian', 'cuisine^cuisine-hawaiian', 0), "
            + "('Swedish', 'cuisine^cuisine-swedish', 0), "
            + "('Portuguese', 'cuisine^cuisine-portuguese', 0), "
            + "('Hungarian', 'cuisine^cuisine-hungarian', 0), "
            + "('Southern & Soul Food', 'cuisine^cuisine-southern', 0);";

    public static final String ADD_COURSES = "INSERT INTO "
            + DBContracts.CoursesTable.TABLE_NAME + " ( "
            + DBContracts.CoursesTable.COLUMN_NAME + ", "
            + DBContracts.CoursesTable.COLUMN_YUMMLY_CODE + ", "
            + DBContracts.CoursesTable.COLUMN_CHOSEN + ") VALUES "
            + "('Main Dishes', 'course^course-Main Dishes', 0), "
            + "('Desserts', 'course^course-Desserts', 0), "
            + "('Side Dishes', 'course^course-Side Dishes', 0), "
            + "('Appetizers', 'course^course-Appetizers', 0), "
            + "('Salads', 'course^course-Salads', 0), "
            + "('Breakfast and Brunch', 'course^course-Breakfast and Brunch', 0), "
            + "('Breads', 'course^course-Breads', 0), "
            + "('Soups', 'course^course-Soups', 0), "
            + "('Beverages', 'course^course-Beverages', 0), "
            + "('Condiments and Sauces', 'course^course-Condiments and Sauces', 0), "
            + "('Cocktails', 'course^course-Cocktails', 0), "
            + "('Snacks', 'course^course-Snacks', 0), "
            + "('Lunch', 'course^course-Lunch', 0);";


}
