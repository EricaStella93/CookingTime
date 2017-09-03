package com.example.erica.cookingtime.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.erica.cookingtime.Activities.DietaryPreferencesActivity;
import com.example.erica.cookingtime.DataBase.DBModifiers.RemoveDislIng;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

import java.util.ArrayList;

public class DislIngAdapter extends RecyclerView.Adapter<DislIngAdapter.ViewHolder>{

    private ArrayList<DislikedIng> ingList;

    public DislIngAdapter(ArrayList<DislikedIng> list){
        ingList = list;
    }

    public void add(int position, DislikedIng item) {
        ingList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        ingList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button button;

        public ViewHolder(View v) {
            super(v);
            button = (Button) v.findViewById(R.id.remove_disl_ing_button);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DislIngAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.disl_ing_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DislIngAdapter.ViewHolder vh = new DislIngAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final DislikedIng ing = ingList.get(position);
        holder.button.setText(ing.getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                RemoveDislIng task = new RemoveDislIng(ing.getId());
                task.execute(v.getContext());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ingList.size();
    }
}
