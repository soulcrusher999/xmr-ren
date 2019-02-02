package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HotelService implements Parcelable {
    public String name;
    public String desc;
    public String facilities;

    public HotelService() {
    }

    public HotelService(String name, String desc, String facilities) {
        this.name = name;
        this.desc = desc;
        this.facilities = facilities;
    }

    protected HotelService(Parcel in) {
        name = in.readString();
        desc = in.readString();
        facilities = in.readString();
    }

    public static final Creator<HotelService> CREATOR = new Creator<HotelService>() {
        @Override
        public HotelService createFromParcel(Parcel in) {
            return new HotelService(in);
        }

        @Override
        public HotelService[] newArray(int size) {
            return new HotelService[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(facilities);
    }
}
