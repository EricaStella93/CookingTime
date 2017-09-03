package com.example.erica.cookingtime.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erica.cookingtime.Adapters.CuisineAdapter;
import com.example.erica.cookingtime.Dialogs.ResetCuisinesDialog;
import com.example.erica.cookingtime.POJO.Cuisine;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

import java.util.ArrayList;

public class CuisineFilterFragment extends Fragment {

    private static final String CUISINES = "cuisines";
    private ArrayList<Cuisine> cuisines;

    private RecyclerView cuisineRecView;

    private OnCloseFilterFragment mListener;

    public static CuisineFilterFragment newInstance(ArrayList<Cuisine> cuisines){
        CuisineFilterFragment fragment = new CuisineFilterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CUISINES, cuisines);
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
            cuisines = getArguments().getParcelableArrayList(CUISINES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.filter_cuisine, container, false);
        layout.findViewById(R.id.back_to_filters_cuisine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFilterFragment(FilterSupportFragment.CUISINE_OPEN);
            }
        });
        layout.findViewById(R.id.reset_filter_cuisine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ResetCuisinesDialog dialog = new ResetCuisinesDialog();
                dialog.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "reset_filter");
            }
        });

        cuisineRecView = (RecyclerView) layout.findViewById(R.id.cuisines_rec_view);
        if(cuisines != null){
            setCuisineRecView();
        }
        return layout;
    }

    private void setCuisineRecView(){
        if(cuisineRecView == null){
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(cuisineRecView.getContext());
        cuisineRecView.setLayoutManager(layoutManager);
        CuisineAdapter adapter = new CuisineAdapter(cuisines);
        cuisineRecView.setAdapter(adapter);
    }

    public void setCuisines(ArrayList<Cuisine> cuisines){
        if(this.cuisines == null){
            this.cuisines = cuisines;
            setCuisineRecView();
        }
    }

    public void resetAllCuisines(){
        if(cuisines != null){
            for(int i = 0; i < cuisines.size(); i++){
                cuisines.get(i).setChosen(false);
            }

            if(cuisineRecView != null){
                cuisineRecView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
