package com.example.erica.cookingtime.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.erica.cookingtime.DataBase.DBModifiers.UpdateDietPref;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.ArrayList;

public class AllergyAdapter  extends RecyclerView.Adapter<AllergyAdapter.ViewHolder>{

    private ArrayList<Allergy> allergList;

    public AllergyAdapter(ArrayList<Allergy> list){
        allergList = list;
    }

    public void add(int position, Allergy item) {
        allergList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        allergList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            checkbox = (CheckBox) v.findViewById(R.id.checkbox);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AllergyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.diet_pref_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        AllergyAdapter.ViewHolder vh = new AllergyAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AllergyAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Allergy allergy = allergList.get(position);
        holder.checkbox.setText(allergy.getName());
        holder.checkbox.setChecked(allergy.isChosen());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                allergy.setChosen(checked);

                UpdateDietPref task = new UpdateDietPref(allergy.getId(),
                        IngredientUtils.fromBoolToInt(checked), ConstantsDictionary.ALLERGY);
                task.execute(v.getContext());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return allergList.size();
    }
}
