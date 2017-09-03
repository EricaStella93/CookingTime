package com.example.erica.cookingtime.POJO;

import java.util.List;

import com.example.erica.cookingtime.POJO.Match;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YummlySearchResponse {

    @SerializedName("matches")
    @Expose
    public List<Match> matches = null;
}