package com.example.erica.cookingtime.Utils;

import android.widget.Filter;

import com.example.erica.cookingtime.Adapters.FavsRecipeFrontAdapter;
import com.example.erica.cookingtime.POJO.Match;

import java.util.ArrayList;

public class RecipeFilter extends Filter {

    private ArrayList<Match> recipes;
    private ArrayList<Match> filteredRecipes;
    private FavsRecipeFrontAdapter adapter;

    public RecipeFilter(ArrayList<Match> recipes, FavsRecipeFrontAdapter adapter){
        this.recipes = recipes;
        this.adapter = adapter;
        this.filteredRecipes = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredRecipes.clear();
        final FilterResults results = new FilterResults();

        for (final Match item : recipes) {
            if (item.getRecipeName().toLowerCase().trim().contains(constraint.toString().toLowerCase())) {
                filteredRecipes.add(item);
            }
        }

        results.values = filteredRecipes;
        results.count = filteredRecipes.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredRecipes);
        adapter.notifyDataSetChanged();
    }
}
