package com.example.erica.cookingtime.DataBase.DBModifiers;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class UpdateDietPref extends AsyncTask<Context, Void, Void>{

    private final long id;
    private final int newState;
    private final int dietOrAllergy;

    public UpdateDietPref(long id, int newState, int dietOrAllergy) {
        this.id = id;
        this.newState = newState;
        this.dietOrAllergy = dietOrAllergy;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        DietPrefDataSource dataSource = new DietPrefDataSource(contexts[0]);

        try{
            dataSource.openWritable();
        }catch (SQLException e){
            Log.e("DBEX", e.getMessage());
        }

        switch (dietOrAllergy){
            case ConstantsDictionary.ALLERGY:
                dataSource.updateAllergy(id, newState);
                break;
            case ConstantsDictionary.DIET:
                dataSource.updateDiet(id, newState);
                break;
        }

        dataSource.close();
        return null;
    }

}
