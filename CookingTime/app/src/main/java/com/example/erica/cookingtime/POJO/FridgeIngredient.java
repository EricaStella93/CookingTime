package com.example.erica.cookingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.erica.cookingtime.Utils.IngredientUtils;

import java.util.Date;

public class FridgeIngredient implements Parcelable{

    private long id;
    private final String name;
    private float quantity;
    private final String category;
    private String udm;
    private Date bestBefore;

    public FridgeIngredient(String name, float quantity, String category, String date, String udm, long id) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.udm = udm;
        this.bestBefore = IngredientUtils.fromStringToDate(date);
        this.id = id;
    }

    public FridgeIngredient(Parcel in){
        this.name = in.readString();
        this.quantity = in.readFloat();
        this.category = in.readString();
        this.udm = in.readString();
        this.bestBefore = IngredientUtils.fromStringToDate(in.readString());
        this.id = in.readLong();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getUdm() {
        return udm;
    }

    public void setUdm(String udm) {
        this.udm = udm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeFloat(this.quantity);
        parcel.writeString(this.category);
        parcel.writeString(this.udm);
        parcel.writeString(IngredientUtils.fromDateToString(this.bestBefore));
        parcel.writeLong(this.id);
    }

    public static final Parcelable.Creator<FridgeIngredient> CREATOR = new Parcelable.Creator<FridgeIngredient>() {
        public FridgeIngredient createFromParcel(Parcel in) {
            return new FridgeIngredient(in);
        }

        public FridgeIngredient[] newArray(int size) {
            return new FridgeIngredient[size];
        }
    };

}
