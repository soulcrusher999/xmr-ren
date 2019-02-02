package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    public String id;
    public String name;
    public String description;
    public String picture;
    public boolean isCategoryPrimary;

    public Category() {
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        picture = in.readString();
        isCategoryPrimary = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isCategoryPrimary() {
        return isCategoryPrimary;
    }

    public void setCategoryPrimary(boolean categoryPrimary) {
        isCategoryPrimary = categoryPrimary;
    }

    public Category(String id, String name, String description, String picture, boolean isCategoryPrimary) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.isCategoryPrimary = isCategoryPrimary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(picture);
        parcel.writeByte((byte) (isCategoryPrimary ? 1 : 0));
    }
}
