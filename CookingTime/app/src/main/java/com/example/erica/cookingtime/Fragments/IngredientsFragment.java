package com.example.erica.cookingtime.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.erica.cookingtime.Activities.ShoppingListActivity;
import com.example.erica.cookingtime.Adapters.IngredientAdapter;
import com.example.erica.cookingtime.POJO.ExtendedIngredient;
import com.example.erica.cookingtime.R;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment
implements IngredientAdapter.OnIngredientChanges {

    private static final String ING_LIST = "ingredient_list";
    private static final String RECIPE = "recipe_name";

    private ArrayList<ExtendedIngredient> ingredients;
    private String recipeName;

    private IngredientAdapter adapter;
    private Button addAll;
    private Button removeAll;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    public static IngredientsFragment newInstance(ArrayList<ExtendedIngredient> ingredients, String recipe) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ING_LIST, ingredients);
        args.putString(RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredients = getArguments().getParcelableArrayList(ING_LIST);
            recipeName = getArguments().getString(RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.ingredient_list_layout, container, false);
        addAll = (Button) layout.findViewById(R.id.add_all_to_shop_list);
        addAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addAllToShoppingList(view.getContext());
                addAll.setVisibility(View.GONE);
                removeAll.setVisibility(View.VISIBLE);
            }
        });
        removeAll = (Button) layout.findViewById(R.id.remove_all_from_shop_list);
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.removeAllFromShoppingList(view.getContext());
                addAll.setVisibility(View.VISIBLE);
                removeAll.setVisibility(View.GONE);
            }
        });
        checkIngredients(addAll, removeAll);
        layout.findViewById(R.id.go_to_shop_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView recView = (RecyclerView) layout.findViewById(R.id.ingredients_rec_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recView.setLayoutManager(layoutManager);
        adapter = new IngredientAdapter(ingredients, recipeName, this);
        recView.setAdapter(adapter);
        return layout;
    }

    private void checkIngredients(View addAll, View removeAll){
        boolean allAdded = true;
        for(ExtendedIngredient ing: ingredients){
            if(ing.getDbId() <= 0){
                allAdded = false;
            }
        }
        if(allAdded){
            removeAll.setVisibility(View.VISIBLE);
            addAll.setVisibility(View.GONE);
        }else{
            removeAll.setVisibility(View.GONE);
            addAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAllAdded() {
        addAll.setVisibility(View.GONE);
        removeAll.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAllRemoved() {
        addAll.setVisibility(View.VISIBLE);
        removeAll.setVisibility(View.GONE);
    }
}
