package com.example.erica.cookingtime.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.erica.cookingtime.DataBase.DBModifiers.UpdateDietPref;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.ShoppingListSection;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class ShoppingListSectionAdapter extends RecyclerView.Adapter<ShoppingListSectionAdapter.ViewHolder>{

    private ArrayList<ShoppingListSection> sections;
    private ShoppingListAdapter.OnMoveIngredients mListener;
    private View gotItRow;
    private int rowOrSquare;
    private AppCompatActivity context;

    public ShoppingListSectionAdapter(ArrayList<ShoppingListSection> sections,
                                      ShoppingListAdapter.OnMoveIngredients mListener,
                                      View gotItRow, int rowOrSquare, AppCompatActivity context){
        this.sections = sections;
        this.mListener = mListener;
        this.gotItRow = gotItRow;
        this.rowOrSquare = rowOrSquare;
        this.context = context;
    }

    public void add(int position, ShoppingListSection item) {
        sections.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        sections.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        for(ShoppingListSection section: sections){
            int index = sections.indexOf(section);
            remove(index);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView aisle;
        public RecyclerView ingredients;

        public ViewHolder(View v) {
            super(v);
            aisle = (TextView) v.findViewById(R.id.aisle_name);
            ingredients = (RecyclerView) v.findViewById(R.id.rec_view);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ShoppingListSectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.shop_list_section, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShoppingListSectionAdapter.ViewHolder vh = new ShoppingListSectionAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ShoppingListSectionAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ShoppingListSection sec = sections.get(position);
        holder.aisle.setText(sec.getName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.aisle.getContext());
        holder.ingredients.setLayoutManager(layoutManager);
        ShoppingListAdapter adapter;
        if(rowOrSquare == ConstantsDictionary.ROW){
            adapter = new ShoppingListAdapter(sec.getIngredients(), mListener, gotItRow, context);
        }else{
            adapter = new SquareShopListAdapter(sec.getIngredients(), mListener, gotItRow, context);
        }
        holder.ingredients.setAdapter(adapter);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sections.size();
    }
}
