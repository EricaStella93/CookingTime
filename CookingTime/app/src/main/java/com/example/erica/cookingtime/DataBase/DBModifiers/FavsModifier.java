package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FavouritesDataSource;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class FavsModifier extends AsyncTask<Context, Void, Void> {

    private final int addOrRemove;
    private final String id;
    private final Match recipe;

    public FavsModifier(int addOrRemove, String id, Match recipe){
        this.addOrRemove = addOrRemove;
        this.id = id;
        this.recipe = recipe;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        FavouritesDataSource dataSource = new FavouritesDataSource(contexts[0]);
        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        switch (addOrRemove){
            case ConstantsDictionary.ADD:
                dataSource.insertInFavs(this.recipe);
                break;
            case ConstantsDictionary.REMOVE:
                dataSource.deleteFromFavs(id);
                break;
        }
        return null;
    }
}
