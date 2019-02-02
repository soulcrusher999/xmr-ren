package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OthersRent implements Parcelable {
    public String id;
    public String name;
    public String desc;
    public String location;
    public String image;
    public String category_name;
    public List<String> room_images;
    public String price;
    public String ownerRules;

    public OthersRent(String id, String name, String desc, String location, String image,
                      String category_name, List<String> room_images, String price, String ownerRules) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.image = image;
        this.category_name = category_name;
        this.room_images = room_images;
        this.price = price;
        this.ownerRules = ownerRules;
    }

    public OthersRent() {
    }

    protected OthersRent(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        location = in.readString();
        image = in.readString();
        category_name = in.readString();
        room_images = in.createStringArrayList();
        price = in.readString();
        ownerRules = in.readString();
    }

    public static final Creator<OthersRent> CREATOR = new Creator<OthersRent>() {
        @Override
        public OthersRent createFromParcel(Parcel in) {
            return new OthersRent(in);
        }

        @Override
        public OthersRent[] newArray(int size) {
            return new OthersRent[size];
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<String> getRoom_images() {
        return room_images;
    }

    public void setRoom_images(List<String> room_images) {
        this.room_images = room_images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwnerRules() {
        return ownerRules;
    }

    public void setOwnerRules(String ownerRules) {
        this.ownerRules = ownerRules;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(location);
        parcel.writeString(image);
        parcel.writeString(category_name);
        parcel.writeStringList(room_images);
        parcel.writeString(price);
        parcel.writeString(ownerRules);
    }
}
