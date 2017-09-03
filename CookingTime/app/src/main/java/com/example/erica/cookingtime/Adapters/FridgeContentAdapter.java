package com.example.erica.cookingtime.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.erica.cookingtime.DataBase.DBModifiers.RemoveDislIng;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.R;

import java.util.ArrayList;

public class FridgeContentAdapter extends RecyclerView.Adapter<FridgeContentAdapter.ViewHolder>{

    private ArrayList<FridgeIngredient> ingredients;
    private ArrayList<FridgeIngredient> chosenIngredients;
    private OnChangeCheck mListener;

    public FridgeContentAdapter(ArrayList<FridgeIngredient> ingredients,
                                OnChangeCheck mListener,
                                ArrayList<FridgeIngredient> chosenIngredients){
        this.ingredients = ingredients;
        this.mListener = mListener;
        this.chosenIngredients = chosenIngredients;
    }

    public void add(int position, FridgeIngredient item) {
        ingredients.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox button;
        public TextView ingName;

        public ViewHolder(View v) {
            super(v);
            button = (CheckBox) v.findViewById(R.id.add_box);
            ingName = (TextView) v.findViewById(R.id.name);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FridgeContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.content_ingr_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FridgeContentAdapter.ViewHolder vh = new FridgeContentAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FridgeContentAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FridgeIngredient ing = ingredients.get(position);
        holder.ingName.setText(ing.getName());
        if(chosenIngredients.contains(ing)){
            holder.button.setChecked(true);
        }else{
            holder.button.setChecked(false);
        }
        holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mListener.onChangeCheck(b, ing);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public interface OnChangeCheck{
        void onChangeCheck(boolean newCheck, FridgeIngredient ing);
    }
}
