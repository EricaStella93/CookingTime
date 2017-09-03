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
import com.example.erica.cookingtime.Utils.MyTextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private ArrayList<String> strings;

    public ListAdapter(ArrayList<String> strings){
        this.strings = strings;
    }

    public void add(int position, String item) {
        strings.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        strings.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MyTextView text;

        public ViewHolder(View v) {
            super(v);
            text = (MyTextView) v.findViewById(R.id.title);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.titleview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListAdapter.ViewHolder vh = new ListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String string = strings.get(position);
        holder.text.setText(string);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }
}
