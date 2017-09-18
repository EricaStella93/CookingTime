package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.erica.cookingtime.Adapters.FavsRecipeFrontAdapter;
import com.example.erica.cookingtime.Adapters.RecipeFrontAdapter;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.SearchViewStyle;

import java.util.ArrayList;


public class FavsRecipeListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecipeFrontAdapter.OnChangeFavs favListener;
    private ArrayList<Match> recipeList = new ArrayList<>();

    private RecyclerView recipeRecView;
    private RecyclerView.LayoutManager layMan;
    private FavsRecipeFrontAdapter adapter;

    private int gridColumns;

    private static final String GRID_COLUMNS = "grid_columns";

    private static final String STATE_QUERY = "state_query";

    private SearchView sv;
    private CharSequence initialQuery;

    public FavsRecipeListFragment(){

    }


    public static FavsRecipeListFragment newInstance(int gridViewColumns, String initialQuery) {
        FavsRecipeListFragment fragment = new FavsRecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(GRID_COLUMNS, gridViewColumns);
        args.putString(STATE_QUERY, initialQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gridColumns = getArguments().getInt(GRID_COLUMNS);
            initialQuery = getArguments().getString(STATE_QUERY);
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
        if(recipeList.isEmpty()){
            ArrayList<Match> recipes = mListener.receiveMatches();
            if(recipes!= null){
                recipeList.addAll(recipes);
            }

        }
        layMan = new GridLayoutManager(recipeRecView.getContext(), gridColumns);
        recipeRecView.setLayoutManager(layMan);
        adapter = new FavsRecipeFrontAdapter(recipeList, this, favListener);
        recipeRecView.setAdapter(adapter);

        if(savedInstanceState == null){
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_favs, menu);
        configureSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureSearchView(final Menu menu){
        final MenuItem search = menu.findItem(R.id.search);

        //per togliere l'overflow menu quando si apre la search view
        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                //to give focus to the search view when it is expanded
                sv.setIconified(false);
                sv.requestFocusFromTouch();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                //to hide the keyboard when back button is pressed
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                if(adapter != null){
                    mListener.saveInitialQuery("");
                    adapter.filterList("");
                }
                return true;
            }
        });

        sv = (SearchView) search.getActionView();

        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);

        //style cursor and other things of the search view
        SearchViewStyle.on(sv)
                .setCursorColor(getResources().getColor(R.color.light_green))
                .setTextColor(getResources().getColor(R.color.medium_grey))
                .setCloseBtnImageResource(R.drawable.ic_close);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                if(adapter != null){
                    mListener.saveInitialQuery(newText);
                    adapter.filterList(newText);
                }
                return true;
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose(){
                if(adapter != null){
                    mListener.saveInitialQuery("");
                    adapter.filterList("");
                }
                return true;
            }
        });
        sv.setSubmitButtonEnabled(false);
        sv.setIconifiedByDefault(true);

        if(initialQuery != null){
            if(!initialQuery.equals("")){
                sv.setIconified(false);
                search.expandActionView();
                sv.setQuery(initialQuery, false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        /*if (!sv.isIconified()) {
            mListener.saveInitialQuery(sv.getQuery().toString());
        }else{
            mListener.saveInitialQuery("");

        }*/
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
