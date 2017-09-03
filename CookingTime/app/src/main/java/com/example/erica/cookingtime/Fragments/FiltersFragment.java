package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.erica.cookingtime.Activities.DietaryPreferencesActivity;
import com.example.erica.cookingtime.Adapters.AllergyAdapter;
import com.example.erica.cookingtime.Adapters.DietAdapter;
import com.example.erica.cookingtime.Adapters.DislIngAdapter;
import com.example.erica.cookingtime.DataBase.DataSources.DietPrefDataSource;
import com.example.erica.cookingtime.Dialogs.ResetFiltersDialog;
import com.example.erica.cookingtime.POJO.Allergy;
import com.example.erica.cookingtime.POJO.Diet;
import com.example.erica.cookingtime.POJO.DislikedIng;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;
import com.example.erica.cookingtime.Utils.IngredientUtils;
import com.example.erica.cookingtime.Utils.MyTextView;

import java.util.ArrayList;


public class FiltersFragment extends Fragment{


    //hasBar = true -> l'activity modifica la toolbar e non c'è bisogno del primo linear layout nel fragment
    private boolean hasBar;
    private static final String HAS_BAR = "has_bar";

    private OnFiltersChange mListener;

    public FiltersFragment(){

    }

    public static FiltersFragment newInstance(boolean hasBar){
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putBoolean(HAS_BAR, hasBar);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFiltersChange{
        void onFiltersExit();
        void onFiltersOpen(String filterToOpen);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFiltersChange){
            mListener = (OnFiltersChange) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasBar = getArguments().getBoolean(HAS_BAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_filters, container, false);
        //se devo nascondere la barra in alto con i pulsanti del layout
        if(hasBar){
            layout.findViewById(R.id.filter_bar).setVisibility(View.GONE);
        }else{
            layout.findViewById(R.id.close_filters_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onFiltersExit();
                }
            });
        }
        setUpAddons();

        layout.findViewById(R.id.diet_pref_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFiltersOpen(FilterSupportFragment.DIET_PREF_OPEN);
            }
        });

        layout.findViewById(R.id.prep_time_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFiltersOpen(FilterSupportFragment.PREP_TIME_OPEN);
            }
        });

        layout.findViewById(R.id.cuisines_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFiltersOpen(FilterSupportFragment.CUISINE_OPEN);
            }
        });

        layout.findViewById(R.id.courses_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFiltersOpen(FilterSupportFragment.COURSE_OPEN);
            }
        });

        layout.findViewById(R.id.incl_ingr_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFiltersOpen(FilterSupportFragment.INCL_INGR_OPEN);
            }
        });

        layout.findViewById(R.id.reset_filter_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ResetFiltersDialog dialog = new ResetFiltersDialog();
                dialog.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "reset_dialog");
            }
        });


        return layout;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void resetAddons(){
        //TODO
    }

    private void setUpAddons(){
        //TODO i pulsantini arancio sotto ogni riga
    }

}
