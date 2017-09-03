package com.example.erica.cookingtime.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.erica.cookingtime.DataBase.DBModifiers.UpdateDietPref;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder>{

    private ArrayList<Diet> dietsList;

    public DietAdapter(ArrayList<Diet> list){
        dietsList = list;
    }

    public void add(int position, Diet item) {
        dietsList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        dietsList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            checkbox = (CheckBox) v.findViewById(R.id.checkbox);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DietAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.diet_pref_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DietAdapter.ViewHolder vh = new DietAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DietAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Diet diet = dietsList.get(position);
        holder.checkbox.setText(diet.getName());
        holder.checkbox.setChecked(diet.isChosen());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                diet.setChosen(checked);

                UpdateDietPref task = new UpdateDietPref(diet.getId(),
                        IngredientUtils.fromBoolToInt(checked), ConstantsDictionary.DIET);
                task.execute(v.getContext());

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dietsList.size();
    }
}
