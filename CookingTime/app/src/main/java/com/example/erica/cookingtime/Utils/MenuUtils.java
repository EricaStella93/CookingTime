package com.example.erica.cookingtime.Utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.example.erica.cookingtime.Activities.DietaryPreferencesActivity;
import com.example.erica.cookingtime.Activities.FavouritesActivity;
import com.example.erica.cookingtime.Activities.FridgeActivity;
import com.example.erica.cookingtime.Activities.HomeActivity;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Activities.SearchActivity;
import com.example.erica.cookingtime.Activities.ShoppingListActivity;

public class MenuUtils {

    public static boolean menuNavigation(MenuItem item, Activity activity){
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.home_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.HOME, activity);
        } else if (id == R.id.shop_list_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.SHOPPING_LIST, activity);
        } else if (id == R.id.search_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.SEARCH_RECIPE, activity);
        } else if (id == R.id.favs_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.FAVOURITES, activity);
        } else if (id == R.id.fridge_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.FRIDGE, activity);
        } else if (id == R.id.diet_pref_menu) {
            intent = IntentFactory.intentFactory(IntentFactory.DIETARY_PREF, activity);
        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        activity.startActivity(intent);
        return true;
    }

}
