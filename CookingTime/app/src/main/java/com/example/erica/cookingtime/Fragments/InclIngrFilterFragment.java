package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.erica.cookingtime.Adapters.FridgeContentAdapter;
import com.example.erica.cookingtime.Adapters.IncludedIngrAdapter;
import com.example.erica.cookingtime.DataBase.DataSources.FilterDataSource;
import com.example.erica.cookingtime.Dialogs.AddIncludedIngrDialog;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.POJO.IncludedIngr;
import com.example.erica.cookingtime.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InclIngrFilterFragment extends Fragment implements FridgeContentAdapter.OnChangeCheck{

    private OnCloseFilterFragment mListener;

    private OnIncludedIngredients receiver;

    private static final String INGREDIENTS = "ingredients";
    private ArrayList<IncludedIngr> ingredients;

    private RecyclerView ingredientsRecView;
    IncludedIngrAdapter adapter;
    private RecyclerView fridgeRecView;

    private static final String FRIDGE_OPEN = "fridge_open";
    private boolean fridgeOpen;

    private static final String ALL_FRIDGE = "all_fridge";
    private ArrayList<FridgeIngredient> allFridge;

    private static final String CHOSEN_FRIDGE = "chosen_fridge";
    private ArrayList<FridgeIngredient> chosenFridge;

    private RelativeLayout filterRoot;
    private RelativeLayout fridgeRoot;


    public static InclIngrFilterFragment newInstance(ArrayList<IncludedIngr> ingredients, boolean fridgeOpen,
                                                     ArrayList<FridgeIngredient> allFridge, ArrayList<FridgeIngredient> chosenFridge){
        InclIngrFilterFragment fragment = new InclIngrFilterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS, ingredients);
        args.putBoolean(FRIDGE_OPEN, fridgeOpen);
        args.putParcelableArrayList(ALL_FRIDGE, allFridge);
        args.putParcelableArrayList(CHOSEN_FRIDGE, chosenFridge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnCloseFilterFragment){
            mListener = (OnCloseFilterFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof OnIncludedIngredients){
            receiver = (OnIncludedIngredients) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredients = getArguments().getParcelableArrayList(INGREDIENTS);
            fridgeOpen = getArguments().getBoolean(FRIDGE_OPEN);
            allFridge = getArguments().getParcelableArrayList(ALL_FRIDGE);
            chosenFridge = getArguments().getParcelableArrayList(CHOSEN_FRIDGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.filter_ingredients, container, false);
        layout.findViewById(R.id.back_to_filters_incl_ingr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFilterFragment(FilterSupportFragment.INCL_INGR_OPEN);
            }
        });
        ingredientsRecView = (RecyclerView) layout.findViewById(R.id.incl_ingr_rec_view);
        if(ingredients != null){
            setIngredientsRecView();
        }

        fridgeRecView = (RecyclerView) layout.findViewById(R.id.fridge_rec_view);
        setFridgeRecView();

        filterRoot = (RelativeLayout) layout.findViewById(R.id.ingr_root);
        fridgeRoot = (RelativeLayout) layout.findViewById(R.id.fridge_root);
        layout.findViewById(R.id.add_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIncludedIngrDialog dialog = new AddIncludedIngrDialog();
                dialog.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "add_incl_ingr");
            }
        });

        layout.findViewById(R.id.add_from_fridge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRoot.setVisibility(View.GONE);
                fridgeRoot.setVisibility(View.VISIBLE);
                chosenFridge = new ArrayList<FridgeIngredient>();
                receiver.saveFridgeOpen(true);
                setFridgeRecView();
                //TODO cambiare barra per tornare indietro
            }
        });

        layout.findViewById(R.id.add_ingredients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRoot.setVisibility(View.VISIBLE);
                fridgeRoot.setVisibility(View.GONE);
                receiver.saveFridgeOpen(false);
                //TODo ristabilire barra
                AddIncludedIngredientsToDB task = new AddIncludedIngredientsToDB(chosenFridge);
                task.execute(view.getContext());
            }
        });

        if(fridgeOpen){
            filterRoot.setVisibility(View.GONE);
            fridgeRoot.setVisibility(View.VISIBLE);
        }else{
            filterRoot.setVisibility(View.VISIBLE);
            fridgeRoot.setVisibility(View.GONE);
        }

        return layout;
    }

    private void setFridgeRecView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ingredientsRecView.getContext());
        fridgeRecView.setLayoutManager(layoutManager);
        FridgeContentAdapter adapter = new FridgeContentAdapter(allFridge, this, chosenFridge);
        fridgeRecView.setAdapter(adapter);
    }

    private void setIngredientsRecView(){
        if(ingredientsRecView == null){
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ingredientsRecView.getContext());
        ingredientsRecView.setLayoutManager(layoutManager);
        adapter = new IncludedIngrAdapter(ingredients);
        ingredientsRecView.setAdapter(adapter);
    }

    public void setIngredients(ArrayList<IncludedIngr> list){
        if(this.ingredients == null){
            ingredients = list;
            setIngredientsRecView();
        }
    }

    public void addIngr(IncludedIngr ingr){
        if(ingredientsRecView != null){
            IncludedIngrAdapter adapter = (IncludedIngrAdapter) ingredientsRecView.getAdapter();
            if(adapter != null){
                adapter.add(ingredients.size(), ingr);
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        receiver.saveIngredients(chosenFridge);
    }

    @Override
    public void onChangeCheck(boolean newCheck, FridgeIngredient ing) {
        if(newCheck){
            chosenFridge.add(ing);
        }else{
            chosenFridge.remove(ing);
        }
    }

    public interface OnIncludedIngredients{
        void saveIngredients(ArrayList<FridgeIngredient> chosenIngredients);
        void saveFridgeOpen(boolean fridgeOpen);
    }

    private class AddIncludedIngredientsToDB extends AsyncTask<Context, Void, ArrayList<IncludedIngr>>{

        private ArrayList<FridgeIngredient> ingredients;

        public AddIncludedIngredientsToDB(ArrayList<FridgeIngredient> ingredients){
            this.ingredients = ingredients;
        }

        @Override
        protected ArrayList<IncludedIngr> doInBackground(Context... contexts) {

            FilterDataSource dataSource = new FilterDataSource(contexts[0]);
            try{
                dataSource.openWritable();
            }catch( SQLException e){
                Log.e("Db ex", "failed to open db");
            }
            ArrayList<IncludedIngr> included = new ArrayList<>();
            for(FridgeIngredient ing: ingredients){
                long id = dataSource.insertIncludedIngr(ing.getName());
                included.add(new IncludedIngr(id, ing.getName()));
            }
            return included;
        }

        @Override
        protected void onPostExecute(ArrayList<IncludedIngr> ingrs){
            if(!isCancelled()){
                adapter.addAll(ingrs);
            }

        }
    }
}
