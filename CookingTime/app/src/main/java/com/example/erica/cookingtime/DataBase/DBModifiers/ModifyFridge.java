package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FridgeDataSource;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class ModifyFridge extends AsyncTask<Context, Void, Void> {

    private final int allOrOne;
    private final long id;

    public ModifyFridge(int allOrOne, long id) {
        this.allOrOne = allOrOne;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        FridgeDataSource dataSource = new FridgeDataSource(contexts[0]);
        try{
            dataSource.openWritable();
        }catch(SQLException e){
            Log.e("DB EX", e.getMessage());
        }

        switch (allOrOne){
            case ConstantsDictionary.ALL:
                dataSource.deleteAllIngredients();
                break;
            case ConstantsDictionary.SINGLE:
                dataSource.deleteIngredient(this.id);
                break;
        }
        return null;
    }
}
