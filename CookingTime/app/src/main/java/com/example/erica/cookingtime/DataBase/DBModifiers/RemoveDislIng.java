package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.POJO.DislikedIng;

public class RemoveDislIng extends AsyncTask<Context, Void, Void> {

    private final long id;

    public RemoveDislIng(long id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        dataSource.removeDislIng(id);

        return null;
    }

}
