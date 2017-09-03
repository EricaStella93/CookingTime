package com.example.erica.cookingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class DislikedIng implements Parcelable {

    private final long id;
    private final String name;

    public DislikedIng(long id, String name) {
        this.id = id;
        this.name = name;
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

    public DislikedIng(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<DislikedIng> CREATOR = new Parcelable.Creator<DislikedIng>() {
        public DislikedIng createFromParcel(Parcel in) {
            return new DislikedIng(in);
        }

        public DislikedIng[] newArray(int size) {
            return new DislikedIng[size];
        }
    };
}
