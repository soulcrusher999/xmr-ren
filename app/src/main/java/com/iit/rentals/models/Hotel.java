package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Hotel implements Parcelable {
    public String id;
    public String name;
    public String desc;
    public String location;
    public String image;
    public String category_name;
    public String price;
    public String discount;
    public String contact_no;
    public String owner_name;
    public String ownerRules;

    public Hotel() {
    }

    public Hotel(String id, String name, String desc, String location, String image, String category_name, String price, String discount, String contact_no, String owner_name, String ownerRules) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.image = image;
        this.category_name = category_name;
        this.price = price;
        this.discount = discount;
        this.contact_no = contact_no;
        this.owner_name = owner_name;
        this.ownerRules = ownerRules;
    }

    protected Hotel(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        location = in.readString();
        image = in.readString();
        category_name = in.readString();
        price = in.readString();
        discount = in.readString();
        contact_no = in.readString();
        owner_name = in.readString();
        ownerRules = in.readString();
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(location);
        dest.writeString(image);
        dest.writeString(category_name);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(contact_no);
        dest.writeString(owner_name);
        dest.writeString(ownerRules);
    }
}
