package com.example.erica.cookingtime.Fragments;

import com.example.erica.cookingtime.POJO.Match;

import java.util.ArrayList;

public interface OnFragmentInteractionListener {
    void onFragmentRecipeSelected(Match recipe);
    ArrayList<String> receiveFavs();
    ArrayList<Match> receiveMatches();
    void saveInitialQuery(String initialQuery);
}
