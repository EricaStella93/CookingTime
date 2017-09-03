package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.Adapters.IngredientAdapter;
import com.example.erica.cookingtime.Adapters.ShoppingListAdapter;
import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.DataBase.DataSources.ShoppingListDataSource;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class DeleteShoppingList extends AsyncTask<Context, Void, Void> {

    private final int allOrGotIt;
    private final long id;

    public DeleteShoppingList(int allOrGotIt, long id) {
        this.allOrGotIt = allOrGotIt;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        switch (allOrGotIt){
            case ConstantsDictionary.ALL:
                dataSource.deleteAllShoppingList();
                break;
            case ConstantsDictionary.GOT_IT:
                dataSource.deleteGotItList();
                break;
            case ConstantsDictionary.SINGLE:
                dataSource.deleteSingleIngredient(id);
        }

        dataSource.close();
        return null;
    }
}
