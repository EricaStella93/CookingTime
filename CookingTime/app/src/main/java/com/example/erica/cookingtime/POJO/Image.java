package com.example.erica.cookingtime.POJO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("hostedLargeUrl")
    @Expose
    private String hostedLargeUrl;
    @SerializedName("hostedSmallUrl")
    @Expose
    private String hostedSmallUrl;

    public Image() {
    }

    /**
     *
     * @param hostedSmallUrl
     * @param hostedLargeUrl
     */
    public Image(String hostedLargeUrl, String hostedSmallUrl) {
        super();
        this.hostedLargeUrl = hostedLargeUrl;
        this.hostedSmallUrl = hostedSmallUrl;
    }

    public String getHostedLargeUrl() {
        return hostedLargeUrl;
    }

    public void setHostedLargeUrl(String hostedLargeUrl) {
        this.hostedLargeUrl = hostedLargeUrl;
    }

    public String getHostedSmallUrl() {
        return hostedSmallUrl;
    }

    public void setHostedSmallUrl(String hostedSmallUrl) {
        this.hostedSmallUrl = hostedSmallUrl;
    }

}