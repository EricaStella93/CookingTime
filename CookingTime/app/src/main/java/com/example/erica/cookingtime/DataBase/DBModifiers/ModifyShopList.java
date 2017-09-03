package com.example.erica.cookingtime.DataBase.DBModifiers;


import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.ShoppingListDataSource;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class ModifyShopList extends AsyncTask<Context, Void, Void> {

    private final Long ids;
    private final int gotItNew;

    public ModifyShopList(long id, boolean gotIt){
        this.ids = id;
        gotItNew = IngredientUtils.fromBoolToInt(gotIt);
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        dataSource.updateId(ids, gotItNew);

        dataSource.close();
        return null;
    }
}
