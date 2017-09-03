package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erica.cookingtime.DataBase.DataSources.FridgeDataSource;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.Date;

public class UpdateFridge extends AsyncTask<Context, Void, Void> {

    private long id;
    private float quantity;
    private String udm;
    private String date;

    public UpdateFridge(long id, float quantity, String udm, Date date){
        this.id = id;
        this.quantity = quantity;
        this.udm = udm;
        this.date = IngredientUtils.fromDateToString(date);
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        FridgeDataSource dataSource = new FridgeDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("db ex", "failed to open db");
        }

        dataSource.updateIngredient(id, quantity, udm, date);
        dataSource.close();

        return null;
    }
}
