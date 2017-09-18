package com.example.erica.cookingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendedIngredient implements Parcelable {

    @SerializedName("aisle")
    @Expose
    public String aisle;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("unit")
    @Expose
    public String unit;
    @SerializedName("unitShort")
    @Expose
    public String unitShort;
    @SerializedName("unitLong")
    @Expose
    public String unitLong;
    @SerializedName("originalString")
    @Expose
    public String originalString;

    public long dbId = -1;

    @Override
    public int describeContents() {
        return 0;
    }

    public ExtendedIngredient(Parcel in){
        this.aisle = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.amount = in.readDouble();
        this.unit = in.readString();
        this.unitShort = in.readString();
        this.unitLong = in.readString();
        this.originalString = in.readString();
        this.dbId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.aisle);
        parcel.writeString(this.image);
        parcel.writeString(this.name);
        parcel.writeDouble(this.amount);
        parcel.writeString(this.unit);
        parcel.writeString(this.unitShort);
        parcel.writeString(this.unitLong);
        parcel.writeString(this.originalString);
        parcel.writeLong(this.dbId);
    }

    public static final Parcelable.Creator<ExtendedIngredient> CREATOR = new Parcelable.Creator<ExtendedIngredient>() {
        public ExtendedIngredient createFromParcel(Parcel in) {
            return new ExtendedIngredient(in);
        }

        public ExtendedIngredient[] newArray(int size) {
            return new ExtendedIngredient[size];
        }
    };

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }


    public String getAisle() {
        return aisle;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public String getUnitLong() {
        return unitLong;
    }

    public String getOriginalString() {
        return originalString;
    }
}