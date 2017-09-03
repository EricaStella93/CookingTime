package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.erica.cookingtime.Activities.DietaryPreferencesActivity;
import com.example.erica.cookingtime.Adapters.ListAdapter;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

import java.util.ArrayList;

public class DietPrefFilterFragment extends Fragment {

    private static final String DISLIKED_ING = "disl_ing";
    private static final String DIET = "diet";
    private static final String ALLERGY = "allergy";

    private OnCloseFilterFragment mListener;

    private ArrayList<String> dislIngs;
    private ArrayList<String> diets;
    private ArrayList<String> allergies;

    private SharedPreferences sharedPref;

    private RecyclerView dislIngRecView;
    private RecyclerView dietRecView;
    private RecyclerView allergyRecView;

    private TextView dislIngText;
    private TextView dietText;
    private TextView allergyText;

    public static DietPrefFilterFragment newInstance(ArrayList<String> dislIngs,
                                                     ArrayList<String> diets,
                                                     ArrayList<String> allergies){
        DietPrefFilterFragment fragment = new DietPrefFilterFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(DISLIKED_ING, dislIngs);
        args.putStringArrayList(DIET, diets);
        args.putStringArrayList(ALLERGY, allergies);
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
        sharedPref = context.getSharedPreferences(ConstantsDictionary.SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArrayList<String> placeHolder = getArguments().getStringArrayList(DISLIKED_ING);
            if(placeHolder != null){
                dislIngs = placeHolder;
            }
            placeHolder = getArguments().getStringArrayList(DIET);
            if(placeHolder != null){
                diets = placeHolder;
            }
            placeHolder = getArguments().getStringArrayList(ALLERGY);
            if(placeHolder != null){
                allergies = placeHolder;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.filter_diet_pref, container, false);
        layout.findViewById(R.id.back_to_filters_diet_pref).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFilterFragment(FilterSupportFragment.DIET_PREF_OPEN);
            }
        });

        Switch mySwitch = (Switch) layout.findViewById(R.id.switch_diet_pref);
        boolean on = sharedPref.getBoolean(ConstantsDictionary.SHARED_DIET_PREF_SWITCH, false);
        mySwitch.setChecked(on);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(ConstantsDictionary.SHARED_DIET_PREF_SWITCH, isChecked);
                editor.apply();
            }
        });

        dislIngRecView = (RecyclerView) layout.findViewById(R.id.disl_ing_rec_view);
        dietRecView = (RecyclerView) layout.findViewById(R.id.diet_rec_view);
        allergyRecView = (RecyclerView) layout.findViewById(R.id.allerg_rec_view);

        layout.findViewById(R.id.edit_diet_pref).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DietaryPreferencesActivity.class);
                startActivity(intent);
            }
        });

        dislIngText = (TextView) layout.findViewById(R.id.disl_text);
        dietText = (TextView) layout.findViewById(R.id.diet_text);
        allergyText = (TextView) layout.findViewById(R.id.allergy_text);

        if(dislIngs != null){
            setDislIngRecView();
        }

        if(allergies != null){
            setAllergyRecView();
        }

        if(diets != null){
            setDietRecView();
        }

        return layout;
    }

    private void setDislIngRecView(){
        if(dislIngs.isEmpty()){
            dislIngText.setVisibility(View.GONE);
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dislIngRecView.getContext());
        dislIngRecView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(dislIngs);
        dislIngRecView.setAdapter(adapter);
    }

    private void setDietRecView(){
        if(diets.isEmpty()){
            dietText.setVisibility(View.GONE);
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dietRecView.getContext());
        dietRecView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(diets);
        dietRecView.setAdapter(adapter);
    }

    private void setAllergyRecView(){
        if(allergies.isEmpty()){
            allergyText.setVisibility(View.GONE);
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(allergyRecView.getContext());
        allergyRecView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(allergies);
        allergyRecView.setAdapter(adapter);
    }

    public void setDislIngs(ArrayList<String> dislIngs) {
        if(this.dislIngs == null){
            this.dislIngs = dislIngs;
            setDislIngRecView();
        }

    }

    public void setDiets(ArrayList<String> diets) {
        if(this.diets == null){
            this.diets = diets;
            setDietRecView();
        }
    }

    public void setAllergies(ArrayList<String> allergies) {
        if(this.allergies == null){
            this.allergies = allergies;
            setAllergyRecView();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
