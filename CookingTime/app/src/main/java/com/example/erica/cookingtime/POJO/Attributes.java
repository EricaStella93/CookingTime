package com.example.erica.cookingtime.POJO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("course")
    @Expose
    public List<String> course = null;
    @SerializedName("cuisine")
    @Expose
    public List<String> cuisine = null;
    @SerializedName("holiday")
    @Expose
    public List<String> holiday = null;

}
