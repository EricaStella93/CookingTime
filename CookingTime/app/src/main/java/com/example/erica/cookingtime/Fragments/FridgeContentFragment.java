package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erica.cookingtime.Adapters.FridgeContentAdapter;
import com.example.erica.cookingtime.POJO.FridgeIngredient;
import com.example.erica.cookingtime.R;

import java.util.ArrayList;

public class FridgeContentFragment extends Fragment implements FridgeContentAdapter.OnChangeCheck{

    private static final String INGREDIENTS = "ingredients";

    private ArrayList<FridgeIngredient> chosenIngredients;
    private ArrayList<FridgeIngredient> allIngredients;

    private OnCloseFilterFragment mListener;

    private RecyclerView ingredientsRecView;

    public FridgeContentFragment(){

    }

    public static FridgeContentFragment newInstance(ArrayList<FridgeIngredient> ingredients){
        FridgeContentFragment fragment = new FridgeContentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS, ingredients);
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allIngredients = getArguments().getParcelableArrayList(INGREDIENTS);
            chosenIngredients = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fridge_content_fragment, container, false);
        ingredientsRecView = (RecyclerView) layout.findViewById(R.id.ingr_rec_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        ingredientsRecView.setLayoutManager(layoutManager);
        FridgeContentAdapter adapter = new FridgeContentAdapter(allIngredients, this, chosenIngredients);
        ingredientsRecView.setAdapter(adapter);
        return layout;
    }

    @Override
    public void onChangeCheck(boolean newCheck, FridgeIngredient ing) {
        if(newCheck){
            chosenIngredients.add(ing);
        }else{
            chosenIngredients.remove(ing);
        }
    }

}
