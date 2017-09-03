package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class ModifyFilters extends AsyncTask<Context, Void, Void>{

    private final int cuisineOrCourse;
    private final int allOrOne;
    private final long id;
    private final int newState;

    public ModifyFilters(int cuisineOrCourse, int allOrOne, long id, int newState) {
        this.cuisineOrCourse = cuisineOrCourse;
        this.allOrOne = allOrOne;
        this.id = id;
        this.newState = newState;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        FilterDataSource dataSource = new FilterDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        switch (cuisineOrCourse){
            case ConstantsDictionary.CUISINE:
                modifyCuisine(dataSource);
                break;
            case ConstantsDictionary.COURSE:
                modifyCourse(dataSource);
                break;
        }
        dataSource.close();
        return null;
    }

    private void modifyCuisine(FilterDataSource dataSource){
        switch (allOrOne){
            case ConstantsDictionary.ALL:
                dataSource.resetAllCuisines();
                break;
            case ConstantsDictionary.SINGLE:
                dataSource.updateCuisine(this.id, this.newState);
                break;
        }
    }

    private void modifyCourse(FilterDataSource dataSource){
        switch (allOrOne){
            case ConstantsDictionary.ALL:
                dataSource.resetAllCourses();
                break;
            case ConstantsDictionary.SINGLE:
                dataSource.updateCourse(this.id, this.newState);
                break;
        }
    }
}
