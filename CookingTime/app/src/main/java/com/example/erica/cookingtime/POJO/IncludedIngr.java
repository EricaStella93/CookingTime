package com.example.erica.cookingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class IncludedIngr implements Parcelable{

    private final long id;
    private final String name;

    public IncludedIngr(long id, String name){
        this.id = id;
        this.name = name;
    }

    public IncludedIngr(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
    }

    public static final Parcelable.Creator<IncludedIngr> CREATOR = new Parcelable.Creator<IncludedIngr>() {
        public IncludedIngr createFromParcel(Parcel in) {
            return new IncludedIngr(in);
        }

        public IncludedIngr[] newArray(int size) {
            return new IncludedIngr[size];
        }
    };
}
