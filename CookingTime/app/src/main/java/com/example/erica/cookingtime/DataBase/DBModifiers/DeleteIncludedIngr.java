package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class DeleteIncludedIngr extends AsyncTask<Context, Void, Void> {

    private int allOrOne;
    private long id;

    public DeleteIncludedIngr(int allOrOne, long id) {
        this.allOrOne = allOrOne;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Context... contexts){
        FilterDataSource dataSource = new FilterDataSource(contexts[0]);
        try{
            dataSource.openWritable();
        }catch(SQLException e){
            Log.e("db ex", "failed to open data source");
        }
        switch (allOrOne){
            case ConstantsDictionary.ALL:
                dataSource.deleteAllIncludedIngr();
                break;
            case ConstantsDictionary.SINGLE:
                dataSource.deleteIncludedIngr(this.id);
                break;
        }
        dataSource.close();
        return null;
    }
}
