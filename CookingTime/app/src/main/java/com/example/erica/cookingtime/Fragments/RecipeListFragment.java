package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erica.cookingtime.Adapters.RecipeFrontAdapter;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.R;

import java.util.ArrayList;


public class RecipeListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecipeFrontAdapter.OnChangeFavs favListener;
    private ArrayList<Match> recipeList = new ArrayList<>();
    private ArrayList<String> favList = new ArrayList<>();

    private RecyclerView recipeRecView;
    private RecyclerView.LayoutManager layMan;
    private RecipeFrontAdapter adapter;

    private int gridColumns;

    private static final String GRID_COLUMNS = "grid_columns";

    public RecipeListFragment(){

    }

    public void setFavList(ArrayList<String> favList){
            this.favList = favList;
    }

    public static RecipeListFragment newInstance(int gridViewColumns) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(GRID_COLUMNS, gridViewColumns);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gridColumns = getArguments().getInt(GRID_COLUMNS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        recipeRecView = (RecyclerView) layout.findViewById(R.id.recipe_list_rec_view);
        return layout;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        favListener = (RecipeFrontAdapter.OnChangeFavs) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(favList.isEmpty()){
            ArrayList<String> favs = mListener.receiveFavs();
            if(favs != null){
                favList.addAll(favs);
            }
        }
        if(recipeList.isEmpty()){
            ArrayList<Match> recipes = mListener.receiveMatches();
            if(recipes!= null){
                recipeList.addAll(recipes);
            }

        }
        layMan = new GridLayoutManager(recipeRecView.getContext(), gridColumns);
        recipeRecView.setLayoutManager(layMan);
        adapter = new RecipeFrontAdapter(recipeList, this, favList, favListener);
        recipeRecView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void changeGridColumns(int columns, Context context){
        layMan = new GridLayoutManager(context, columns);
        recipeRecView.setLayoutManager(layMan);
    }

    private void setRecipeList(ArrayList<Match> recipes){
        adapter.addList(recipes);
    }

    public void onRecipeSelected(Match recipe){
        mListener.onFragmentRecipeSelected(recipe);
    }
}
