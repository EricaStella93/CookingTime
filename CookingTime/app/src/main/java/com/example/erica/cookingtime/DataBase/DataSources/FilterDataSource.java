package com.example.erica.cookingtime.DataBase.DataSources;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erica.cookingtime.DataBase.DBContracts;
import com.example.erica.cookingtime.DataBase.DBHelpers.BaseDBHelper;
import com.example.erica.cookingtime.DataBase.DBHelpers.FilterDBhelper;
import com.example.erica.cookingtime.POJO.Course;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.POJO.IncludedIngr;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FilterDataSource {

    private SQLiteDatabase db;
    private BaseDBHelper dbHelper;

    public FilterDataSource(Context context){
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

    public ArrayList<Cuisine> retrieveAllCuisines(){

        ArrayList<Cuisine> cuisines = new ArrayList<>();

        String[] projection = {DBContracts.CuisineTable.COLUMN_ID,
                DBContracts.CuisineTable.COLUMN_NAME,
                DBContracts.CuisineTable.COLUMN_YUMMLY_CODE,
                DBContracts.CuisineTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.CuisineTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            cuisines.add(cuisineFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return cuisines;
    }

    public ArrayList<Cuisine> retrieveChosenCuisine(){
        ArrayList<Cuisine> cuisines = new ArrayList<>();

        String[] projection = {DBContracts.CuisineTable.COLUMN_ID,
                DBContracts.CuisineTable.COLUMN_NAME,
                DBContracts.CuisineTable.COLUMN_YUMMLY_CODE,
                DBContracts.CuisineTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.CuisineTable.TABLE_NAME, projection,
                DBContracts.CuisineTable.COLUMN_CHOSEN+" = 1", null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            cuisines.add(cuisineFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return cuisines;
    }

    private Cuisine cuisineFromCursor(Cursor cursor){
        return new Cuisine(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2),
                IngredientUtils.fromIntToBool(cursor.getInt(3)));
    }

    public ArrayList<Course> retrieveAllCourses(){

        ArrayList<Course> courses = new ArrayList<>();

        String[] projection = {DBContracts.CoursesTable.COLUMN_ID,
                DBContracts.CoursesTable.COLUMN_NAME,
                DBContracts.CoursesTable.COLUMN_YUMMLY_CODE,
                DBContracts.CoursesTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.CoursesTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            courses.add(courseFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return courses;
    }

    private Course courseFromCursor(Cursor cursor){
        return new Course(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2),
                IngredientUtils.fromIntToBool(cursor.getInt(3)));
    }

    public ArrayList<Course> retrieveChosenCourses(){

        ArrayList<Course> courses = new ArrayList<>();

        String[] projection = {DBContracts.CoursesTable.COLUMN_ID,
                DBContracts.CoursesTable.COLUMN_NAME,
                DBContracts.CoursesTable.COLUMN_YUMMLY_CODE,
                DBContracts.CoursesTable.COLUMN_CHOSEN};

        Cursor cursor = db.query(DBContracts.CoursesTable.TABLE_NAME, projection,
                DBContracts.CoursesTable.COLUMN_CHOSEN+" = 1", null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            courses.add(courseFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return courses;
    }

    public ArrayList<IncludedIngr> retrieveAllIncludedIngreds(){

        ArrayList<IncludedIngr> ingredients = new ArrayList<>();

        String[] projection = {DBContracts.IncludedIngrTable.COLUMN_ID,
                DBContracts.IncludedIngrTable.COLUMN_NAME};

        Cursor cursor = db.query(DBContracts.IncludedIngrTable.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            ingredients.add(includedIngredFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return ingredients;
    }

    private IncludedIngr includedIngredFromCursor(Cursor cursor){
        return new IncludedIngr(cursor.getLong(0), cursor.getString(1));
    }

    public long insertIncludedIngr(String name){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.IncludedIngrTable.COLUMN_NAME, name);
        return db.insert(DBContracts.IncludedIngrTable.TABLE_NAME, null, cv);
    }

    public void deleteIncludedIngr(long id){
        db.delete(DBContracts.IncludedIngrTable.TABLE_NAME,
                DBContracts.IncludedIngrTable.COLUMN_ID + " = " + id, null);
    }

    public void updateCuisine(long id, int newState){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.CuisineTable.COLUMN_CHOSEN, newState);
        db.update(DBContracts.CuisineTable.TABLE_NAME, cv,
                DBContracts.CuisineTable.COLUMN_ID + " = " + id, null);
    }

    public void updateCourse(long id, int newState){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.CoursesTable.COLUMN_CHOSEN, newState);
        db.update(DBContracts.CoursesTable.TABLE_NAME, cv,
                DBContracts.CoursesTable.COLUMN_ID + " = " + id, null);
    }

    public void resetAllCuisines(){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.CuisineTable.COLUMN_CHOSEN, 0);
        db.update(DBContracts.CuisineTable.TABLE_NAME, cv, null, null);
    }

    public void resetAllCourses(){
        ContentValues cv = new ContentValues();
        cv.put(DBContracts.CoursesTable.COLUMN_CHOSEN, 0);
        db.update(DBContracts.CoursesTable.TABLE_NAME, cv, null, null);
    }

    public void deleteAllIncludedIngr(){
        db.delete(DBContracts.IncludedIngrTable.TABLE_NAME, null, null);
    }
}
