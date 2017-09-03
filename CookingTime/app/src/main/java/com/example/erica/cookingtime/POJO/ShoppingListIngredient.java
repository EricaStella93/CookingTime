package com.example.erica.cookingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.erica.cookingtime.Utils.IngredientUtils;

public class ShoppingListIngredient implements Parcelable{

    private final long id;
    private final String name;
    private final String aisle;
    private float quantity;
    private String udm;
    private String recipe;
    private boolean gotIt;

    public ShoppingListIngredient(long id, String name, String aisle, float quantity, String udm, String recipe, boolean gotIt) {
        this.id = id;
        this.name = name;
        this.aisle = aisle;
        this.quantity = quantity;
        this.udm = udm;
        this.recipe = recipe;
        this.gotIt = gotIt;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUdm() {
        return udm;
    }

    public String getRecipe() {
        return recipe;
    }

    public boolean isGotIt() {
        return gotIt;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUdm(String udm) {
        this.udm = udm;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setGotIt(boolean gotIt) {
        this.gotIt = gotIt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAisle() {
        return aisle;
    }

    public ShoppingListIngredient(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.aisle = in.readString();
        this.quantity = in.readFloat();
        this.udm = in.readString();
        this.recipe = in.readString();
        this.gotIt = in.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.aisle);
        parcel.writeFloat(this.quantity);
        parcel.writeString(this.udm);
        parcel.writeString(this.recipe);
        parcel.writeInt(this.gotIt ? 1 : 0);
    }

    public static final Parcelable.Creator<ShoppingListIngredient> CREATOR = new Parcelable.Creator<ShoppingListIngredient>() {
        public ShoppingListIngredient createFromParcel(Parcel in) {
            return new ShoppingListIngredient(in);
        }

        public ShoppingListIngredient[] newArray(int size) {
            return new ShoppingListIngredient[size];
        }
    };

}
