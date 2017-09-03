package com.example.erica.cookingtime.POJO;

import java.util.ArrayList;

public class ShoppingListSection {

    private String name;
    private ArrayList<ShoppingListIngredient> ingredients;

    public ShoppingListSection(String name, ArrayList<ShoppingListIngredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ShoppingListIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<ShoppingListIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
