package com.example.erica.cookingtime.POJO;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YummlySingleRecipe {

    @SerializedName("ingredientLines")
    @Expose
    private List<String> ingredientLines = null;

    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("name")
    @Expose
    private String name;
    /*@SerializedName("yield")
    @Expose
    private String yield;*/
    /*@SerializedName("attributes")
    @Expose
    private Attributes attributes;*/
    /*@SerializedName("rating")
    @Expose
    private Double rating;*/
    @SerializedName("numberOfServings")
    @Expose
    private Integer numberOfServings;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public YummlySingleRecipe() {
    }

    /**
     *
     * @param id
     * @param source
     * @param yield
     * @param numberOfServings
     * @param name
     * @param ingredientLines
     * @param images
     * @param rating
     * @param attributes
     */
    public YummlySingleRecipe(List<String> ingredientLines, List<Image> images, String name, String yield, Attributes attributes, Double rating, Integer numberOfServings, Source source, String id) {
        super();
        this.ingredientLines = ingredientLines;
        this.images = images;
        this.name = name;
        //this.yield = yield;
        //this.attributes = attributes;
        //this.rating = rating;
        this.numberOfServings = numberOfServings;
        this.source = source;
        this.id = id;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getImage(){
        if(images == null){
            return "";
        }
        if(images.isEmpty()){
            return "";
        }
        Image im = images.get(0);
        if(im.getHostedLargeUrl() != null) {
            return im.getHostedLargeUrl();
        }
        if(im.getHostedSmallUrl() != null){
            return im.getHostedSmallUrl();
        }
        return "";
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }*/


    /*public Attributes getAttributes() {
        return attributes;
    }*/

    /*public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }*/

    /*public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }*/

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}