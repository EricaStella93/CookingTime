package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.ShoppingListDataSource;

public class ModifyShopListIngredient extends AsyncTask<Context, Void, Void>{

    private long id;
    private float quantity;
    private String udm;

    public ModifyShopListIngredient(long id, float quantity, String udm){
        this.id = id;
        this.quantity = quantity;
        this.udm = udm;
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);
        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("db ex", "failed to open data source");
        }
        dataSource.updateId(id, quantity, udm);
        dataSource.close();
        return null;
    }
}
