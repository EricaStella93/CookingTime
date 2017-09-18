package com.example.erica.cookingtime.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.example.erica.cookingtime.DataBase.DBModifiers.FavsModifier;
import com.example.erica.cookingtime.Fragments.FavsRecipeListFragment;
import com.example.erica.cookingtime.Fragments.RecipeListFragment;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.CustomLayout;
import com.example.erica.cookingtime.Utils.RecipeFilter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavsRecipeFrontAdapter extends RecyclerView.Adapter<FavsRecipeFrontAdapter.ViewHolder>{

    private ArrayList<Match> recipes;
    private ArrayList<Match> mDatasetFilter;
    private FavsRecipeListFragment fragment;
    private RecipeFilter filter;
    private RecipeFrontAdapter.OnChangeFavs mListener;

    public FavsRecipeFrontAdapter(ArrayList<Match> list, FavsRecipeListFragment fragment, RecipeFrontAdapter.OnChangeFavs listener){
        recipes = list;
        mDatasetFilter = list;
        this.fragment = fragment;
        filter = new RecipeFilter(recipes, this);
        this.mListener = listener;
    }

    public void add(int position, Match item) {
        recipes.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        recipes.remove(position);
        notifyItemRemoved(position);
    }

    /*public void remove(Match recipe){
        int index = recipes.indexOf(recipe);
        remove(index);
    }*/

    public void removeFromFiltered(Match recipe){
        int index = mDatasetFilter.indexOf(recipe);
        mDatasetFilter.remove(index);
        notifyItemRemoved(index);
        recipes.remove(recipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView recipeName;
        public Button favButton;
        public TextView ratingText;
        public TextView ingredientsText;
        public TextView minutesText;
        public CustomLayout imageLayout;

        public ViewHolder(View v) {
            super(v);
            imageLayout = (CustomLayout) v.findViewById(R.id.image_layout);
            recipeName = (TextView) v.findViewById(R.id.recipe_name);
            favButton = (Button) v.findViewById(R.id.fav_button);
            ratingText = (TextView) v.findViewById(R.id.rating_text);
            ingredientsText = (TextView) v.findViewById(R.id.ingredients_text);
            minutesText = (TextView) v.findViewById(R.id.minutes_text);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavsRecipeFrontAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_recipe_front, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FavsRecipeFrontAdapter.ViewHolder vh = new FavsRecipeFrontAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FavsRecipeFrontAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Match match = mDatasetFilter.get(position);

        //setto il layout di sfondo
        holder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onRecipeSelected(match);
            }
        });

        Picasso.with(holder.imageLayout.getContext())
                .load(match.getRecipe().getImage())
                .placeholder(R.drawable.ic_chef)
                .error(R.drawable.ic_chef)
                .into(holder.imageLayout);

        //setto nome ricetta
        holder.recipeName.setText(match.getRecipeName());

        //setto il pulsante fav di base come presente
        holder.favButton.setBackground(
                holder.favButton.getContext().getResources()
                        .getDrawable(R.drawable.ic_favs_full_orange));

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavsModifier task = new FavsModifier(ConstantsDictionary.REMOVE, match.getId(), null);
                task.execute(view.getContext());
                removeFromFiltered(match);
                mListener.removeFav(match.getId());
                //TODO aggiungere toast con undo?
            }
        });

        String rating = Integer.toString(match.getRating());
        if(rating == null){
            rating = "0";
        }
        holder.ratingText.setText(rating + "/5");
        String ingredients = Integer.toString(match.getIngredientsNumber());
        if(ingredients == null){
            ingredients = "0";
        }
        holder.ingredientsText.setText(ingredients);
        int minutes = match.getTotalTimeInSeconds()/60;
        holder.minutesText.setText(Integer.toString(minutes)+"'");
    }


    @Override
    public int getItemCount(){
        return mDatasetFilter.size();
    }

    public void addList(ArrayList<Match> recipes){
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Match> recipes){
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Match> list){
        mDatasetFilter = list;
    }

    public void filterList(String text){
        filter.filter(text);
    }

}


