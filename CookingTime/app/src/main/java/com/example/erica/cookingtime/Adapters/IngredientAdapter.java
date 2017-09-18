package com.example.erica.cookingtime.Adapters;


import android.content.Context;
import android.database.SQLException;
import android.nfc.TagLostException;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erica.cookingtime.DataBase.DBModifiers.DeleteShoppingList;
import com.example.erica.cookingtime.DataBase.DBModifiers.RemoveDislIng;
import com.example.erica.cookingtime.DataBase.DataSources.ShoppingListDataSource;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.POJO.ExtendedIngredient;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{

    private ArrayList<ExtendedIngredient> ingredients;
    private final String recipeName;
    private OnIngredientChanges mListener;

    public IngredientAdapter(ArrayList<ExtendedIngredient> ingredients, String recipeName,
                             OnIngredientChanges mListener){
        this.ingredients = ingredients;
        this.recipeName = recipeName;
        this.mListener = mListener;
    }

    public void add(int position, ExtendedIngredient item) {
        ingredients.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    public void addAllToShoppingList(Context context){
        for(int i = 0; i < ingredients.size(); i++){
            if(ingredients.get(i).getDbId()<=0){
                AddToShoppingList task = new AddToShoppingList(ingredients.get(i), recipeName, this);
                task.execute(context);
            }
        }
    }

    public void removeAllFromShoppingList(Context context){
        for(int i = 0; i < ingredients.size(); i++){
            if(ingredients.get(i).getDbId()>0){
                DeleteShoppingList task = new DeleteShoppingList(ConstantsDictionary.SINGLE, ingredients.get(i).getDbId());
                task.execute(context);
                ingredients.get(i).setDbId(-1);
            }
        }
        notifyDataSetChanged();
    }

    private void verifyAllAdded(){
        for(ExtendedIngredient ing : ingredients){
            if(ing.getDbId()<= 0){
                return;
            }
        }
        mListener.onAllAdded();
    }

    private void verifyAllRemoved(){
        for(ExtendedIngredient ing : ingredients){
            if(ing.getDbId()> 0){
                return;
            }
        }
        mListener.onAllRemoved();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        public ImageButton addIng;
        public ImageButton delIng;
        public TextView ingName;
        public LinearLayout container;

        public ViewHolder(View v){
            super(v);
            addIng = (ImageButton) v.findViewById(R.id.add_ing);
            delIng = (ImageButton) v.findViewById(R.id.delete_ing);
            ingName = (TextView) v.findViewById(R.id.ing_name);
            container = (LinearLayout) v.findViewById(R.id.container);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.ingredient_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        IngredientAdapter.ViewHolder vh = new IngredientAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final IngredientAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ExtendedIngredient ing = ingredients.get(position);

        if(position % 2 == 0){
            holder.container.setBackgroundColor(holder.container.getResources().getColor(R.color.light_grey));
        }else{
            holder.container.setBackgroundColor(holder.container.getResources().getColor(R.color.lighter_grey));
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String amount = df.format(ing.getAmount());
        String name = amount+" "+ing.getUnitShort()+" "+ing.getName();
        holder.ingName.setText(name);
        final IngredientAdapter adapter = this;
        holder.addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToShoppingList task = new AddToShoppingList(ing, recipeName, adapter);
                task.execute(view.getContext());
                holder.addIng.setVisibility(View.GONE);
                holder.delIng.setVisibility(View.VISIBLE);
                verifyAllAdded();
            }
        });
        holder.delIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteShoppingList task = new DeleteShoppingList(ConstantsDictionary.SINGLE, ing.getDbId());
                task.execute(view.getContext());
                ing.setDbId(-1);
                holder.addIng.setVisibility(View.VISIBLE);
                holder.delIng.setVisibility(View.GONE);
                verifyAllRemoved();
            }
        });
        //l'ing è da aggiungere al db
        if(ing.getDbId()<=0){
            holder.delIng.setVisibility(View.GONE);
            holder.addIng.setVisibility(View.VISIBLE);
        }
        //l'ing è già nel db
        else{
            holder.addIng.setVisibility(View.GONE);
            holder.delIng.setVisibility(View.VISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class AddToShoppingList extends AsyncTask<Context, Void, Long> {

        private final ExtendedIngredient ing;
        private final String recipeName;
        private IngredientAdapter adapter;

        public AddToShoppingList(ExtendedIngredient ing, String recipeName, IngredientAdapter adapter){
            this.ing = ing;
            this.recipeName = recipeName;
            this.adapter = adapter;
        }

        @Override
        protected Long doInBackground(Context... contexts) {

            ShoppingListDataSource dataSource = new ShoppingListDataSource(contexts[0]);

            try{
                dataSource.openWritable();
            }catch (SQLException e){
                Log.e("DBEX", e.getMessage());
            }

            long id = dataSource.insertIng(ing.getName(), ing.getAisle(),
                    Double.valueOf(ing.getAmount()).floatValue(),
                    ing.getUnitShort(), recipeName);

            dataSource.close();
            return id;
        }

        @Override
        protected void onPostExecute(Long value){
            ing.setDbId(value);
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnIngredientChanges{
        void onAllAdded();
        void onAllRemoved();
    }
}
