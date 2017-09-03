package com.example.erica.cookingtime.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.erica.cookingtime.DataBase.DBModifiers.ModifyShopList;
import com.example.erica.cookingtime.POJO.ShoppingListIngredient;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.MyTextView;

import java.util.ArrayList;

public class SquareShopListAdapter extends ShoppingListAdapter{

    public SquareShopListAdapter(ArrayList<ShoppingListIngredient> ingList,
                                 ShoppingListAdapter.OnMoveIngredients listener,
                                 View gotItRow, AppCompatActivity context){
        super(ingList, listener, gotItRow, context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SquareShopListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.shopping_list_square, parent, false);
        // set the view's size, margins, paddings and layout parameters
        SquareShopListAdapter.ViewHolder vh = new SquareShopListAdapter.ViewHolder(v);
        return vh;
    }

}

