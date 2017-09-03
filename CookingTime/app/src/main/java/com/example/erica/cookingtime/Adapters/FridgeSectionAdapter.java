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
import com.example.erica.cookingtime.POJO.FridgeSection;
import com.example.erica.cookingtime.POJO.ShoppingListSection;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class FridgeSectionAdapter extends RecyclerView.Adapter<FridgeSectionAdapter.ViewHolder>{

    private ArrayList<FridgeSection> sections;
    private int rowOrSquare;
    private FridgeAdapter.OnEditIngredients mListener;

    public FridgeSectionAdapter(ArrayList<FridgeSection> sections, int rowOrSquare,
                                FridgeAdapter.OnEditIngredients mListener){
        this.sections = sections;
        this.rowOrSquare = rowOrSquare;
        this.mListener = mListener;
    }

    public void add(int position, FridgeSection item) {
        sections.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        sections.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll(){
        for(FridgeSection section: sections){
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
    public FridgeSectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.shop_list_section, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FridgeSectionAdapter.ViewHolder vh = new FridgeSectionAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FridgeSectionAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FridgeSection sec = sections.get(position);
        holder.aisle.setText(sec.getName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.aisle.getContext());
        holder.ingredients.setLayoutManager(layoutManager);
        FridgeAdapter adapter= new FridgeAdapter(sec.getIngredients(), mListener, (AppCompatActivity) mListener);
        holder.ingredients.setAdapter(adapter);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sections.size();
    }
}
