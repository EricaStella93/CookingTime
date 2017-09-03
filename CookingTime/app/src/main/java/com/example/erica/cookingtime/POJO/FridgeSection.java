package com.example.erica.cookingtime.POJO;

import java.util.ArrayList;

public class FridgeSection {

    private String name;
    private ArrayList<FridgeIngredient> ingredients;

    public FridgeSection(String name, ArrayList<FridgeIngredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FridgeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<FridgeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
