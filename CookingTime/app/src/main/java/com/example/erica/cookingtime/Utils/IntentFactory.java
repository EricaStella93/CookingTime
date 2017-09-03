package com.example.erica.cookingtime.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.erica.cookingtime.Activities.DietaryPreferencesActivity;
import com.example.erica.cookingtime.Activities.FavouritesActivity;
import com.example.erica.cookingtime.Activities.FridgeActivity;
import com.example.erica.cookingtime.Activities.HomeActivity;
import com.example.erica.cookingtime.Activities.SearchActivity;
import com.example.erica.cookingtime.Activities.ShoppingListActivity;

public class IntentFactory {

    public static final int SEARCH_RECIPE = 1;
    public static final int FAVOURITES = 2;
    public static final int FRIDGE = 3;
    public static final int SHOPPING_LIST = 4;
    public static final int HOME = 5;
    public static final int DIETARY_PREF = 6;

    public static Intent intentFactory(int activityChoice, Context context){

        Class mActivityClass;

        switch(activityChoice){
            case SEARCH_RECIPE:
                mActivityClass = SearchActivity.class;
                break;
            case FAVOURITES:
                mActivityClass = FavouritesActivity.class;
                break;
            case FRIDGE:
                mActivityClass = FridgeActivity.class;
                break;
            case SHOPPING_LIST:
                mActivityClass = ShoppingListActivity.class;
                break;
            case HOME:
                mActivityClass = HomeActivity.class;
                break;
            case DIETARY_PREF:
                mActivityClass = DietaryPreferencesActivity.class;
                break;
            default:
                mActivityClass = HomeActivity.class;
        }

        return new Intent(context, mActivityClass);
    }
}
