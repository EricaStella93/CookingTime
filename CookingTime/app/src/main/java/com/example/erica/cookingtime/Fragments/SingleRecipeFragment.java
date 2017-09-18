package com.example.erica.cookingtime.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.erica.cookingtime.POJO.ExtendedIngredient;
import com.example.erica.cookingtime.POJO.Match;
import com.example.erica.cookingtime.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SingleRecipeFragment extends Fragment{

    private static final String HAS_BAR = "has_bar";

    private SingleRecipeListener mListener;
    //hasBar = true -> l'activity modifica la toolbar e non c'è bisogno del primo linear layout nel fragment
    private boolean hasBar;
    private Match recipe;

    private ViewPager pager;
    private TabLayout tabLayout;
    private ImageView image;
    private TextView recipeName;

    public static SingleRecipeFragment newInstance(boolean hasBar){
        SingleRecipeFragment fragment = new SingleRecipeFragment();
        Bundle args = new Bundle();
        args.putBoolean(HAS_BAR, hasBar);
        fragment.setArguments(args);
        return fragment;
    }

    public interface SingleRecipeListener{
        Match receiveRecipe();
        void onExitSingleRecipe();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof SingleRecipeListener) {
            mListener = (SingleRecipeListener) context;
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
        View layout = inflater.inflate(R.layout.fragment_single_recipe, container, false);
        //se devo nascondere la barra in alto con i pulsanti del layout
        if(hasBar){
            layout.findViewById(R.id.button_layout).setVisibility(View.GONE);
        }else{
            layout.findViewById(R.id.back_from_recipe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onExitSingleRecipe();
                }
            });

            layout.findViewById(R.id.share_recipe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, recipe.getDetailedRecipe().sourceUrl);
                    startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_using)));
                }
            });
        }
        image = (ImageView) layout.findViewById(R.id.recipe_image);
        pager = (ViewPager) layout.findViewById(R.id.pager);
        tabLayout = (TabLayout) layout.findViewById(R.id.sliding_tabs);
        recipeName = (TextView) layout.findViewById(R.id.recipe_name_hid);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        recipe = mListener.receiveRecipe();
        PagerAdapter adapter = new PagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        if(image != null){
            Log.e("image", image.toString());
            Picasso.with(pager.getContext())
                    .load(recipe.getRecipe().getImage())
                    .fit()
                    .into(image);
        }
        if(recipeName != null){
            recipeName.setText(recipe.getRecipeName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 2;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    if(recipe.getDetailedRecipe() != null){
                        if(recipe.getDetailedRecipe().extendedIngredients != null){
                            return IngredientsFragment
                                    .newInstance(
                                            (ArrayList<ExtendedIngredient>) recipe.getDetailedRecipe().extendedIngredients,
                                            recipe.getRecipeName());
                        }
                    }
                    if(recipe.getRecipe() != null){
                        if(recipe.getRecipe().getIngredientLines() != null){
                            return IngredientsFragment
                                    .newInstance(
                                            new ArrayList<ExtendedIngredient>(),
                                            recipe.getRecipeName());
                        }
                    }

                case 1:
                    if(recipe.getDetailedRecipe() != null){
                        if(recipe.getDetailedRecipe().extendedIngredients != null){
                            return InstructionsFragment.newInstance(recipe.getDetailedRecipe().text);
                        }
                    }
                    return InstructionsFragment.newInstance("No instructions found");

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position){
                case 0:
                    if(recipe.getDetailedRecipe() != null){
                        if(recipe.getDetailedRecipe().extendedIngredients != null){
                            return String.valueOf(recipe.getDetailedRecipe().extendedIngredients.size())+ " Ingredients ";
                        }
                    }
                    if(recipe.getRecipe() != null){
                        if(recipe.getRecipe().getIngredientLines() != null){
                            return String.valueOf(recipe.getRecipe().getIngredientLines().size())+ " Ingredients ";
                        }
                    }

                    return "0 ingredients";

                case 1:
                    return String.valueOf(recipe.getTotalTimeInSeconds() /60)+ " Minutes";
                default:
                    return null;
            }
        }
    }

}
