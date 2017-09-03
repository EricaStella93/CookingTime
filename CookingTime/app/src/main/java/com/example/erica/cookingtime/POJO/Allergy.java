package com.example.erica.cookingtime.POJO;


import android.os.Parcel;
import android.os.Parcelable;

public class Allergy implements Parcelable{

    private final long id;
    private final String name;
    private final String code;
    private boolean chosen;

    public Allergy(long id, String name, String code, boolean chosen) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.chosen = chosen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.code);
        parcel.writeInt(this.chosen ? 1 : 0);
    }

    public Allergy(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.code = in.readString();
        this.chosen = in.readInt() != 0;
    }

    public static final Parcelable.Creator<Allergy> CREATOR = new Parcelable.Creator<Allergy>() {
        public Allergy createFromParcel(Parcel in) {
            return new Allergy(in);
        }

        public Allergy[] newArray(int size) {
            return new Allergy[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
