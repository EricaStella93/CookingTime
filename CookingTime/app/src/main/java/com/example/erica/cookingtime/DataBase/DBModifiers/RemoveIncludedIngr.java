package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.POJO.IncludedIngr;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class RemoveIncludedIngr extends AsyncTask<Context, Void, Void>{

    private final long id;

    public RemoveIncludedIngr(long id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        FilterDataSource dataSource = new FilterDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        dataSource.deleteIncludedIngr(this.id);
        return null;
    }
}
