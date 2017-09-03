package com.example.erica.cookingtime.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("sourceRecipeUrl")
    @Expose
    private String sourceRecipeUrl;
    @SerializedName("sourceSiteUrl")
    @Expose
    private String sourceSiteUrl;
    @SerializedName("sourceDisplayName")
    @Expose
    private String sourceDisplayName;

    public Source() {
    }

    /**
     *
     * @param sourceSiteUrl
     * @param sourceDisplayName
     * @param sourceRecipeUrl
     */
    public Source(String sourceRecipeUrl, String sourceSiteUrl, String sourceDisplayName) {
        super();
        this.sourceRecipeUrl = sourceRecipeUrl;
        this.sourceSiteUrl = sourceSiteUrl;
        this.sourceDisplayName = sourceDisplayName;
    }

    public String getSourceRecipeUrl() {
        return sourceRecipeUrl;
    }

    public void setSourceRecipeUrl(String sourceRecipeUrl) {
        this.sourceRecipeUrl = sourceRecipeUrl;
    }

    public String getSourceSiteUrl() {
        return sourceSiteUrl;
    }

    public void setSourceSiteUrl(String sourceSiteUrl) {
        this.sourceSiteUrl = sourceSiteUrl;
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public void setSourceDisplayName(String sourceDisplayName) {
        this.sourceDisplayName = sourceDisplayName;
    }

}