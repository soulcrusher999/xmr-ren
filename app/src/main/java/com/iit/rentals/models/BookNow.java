package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BookNow implements Parcelable {
    public String id;
    public String userId;
    public String hotelId;
    public String hotelName;
    public String hotelImage;
    public String ownerName;
    public String ownerContact;
    public String userName;
    public String userContact;

    public BookNow() {
    }

    protected BookNow(Parcel in) {
        id = in.readString();
        userId = in.readString();
        hotelId = in.readString();
        hotelName = in.readString();
        hotelImage = in.readString();
        ownerName = in.readString();
        ownerContact = in.readString();
        userName = in.readString();
        userContact = in.readString();
    }

    public static final Creator<BookNow> CREATOR = new Creator<BookNow>() {
        @Override
        public BookNow createFromParcel(Parcel in) {
            return new BookNow(in);
        }

        @Override
        public BookNow[] newArray(int size) {
            return new BookNow[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public BookNow(String id, String userId, String hotelId, String hotelName, String hotelImage, String ownerName, String ownerContact, String userName, String userContact) {
        this.id = id;
        this.userId = userId;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelImage = hotelImage;
        this.ownerName = ownerName;
        this.ownerContact = ownerContact;
        this.userName = userName;
        this.userContact = userContact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(hotelId);
        parcel.writeString(hotelName);
        parcel.writeString(hotelImage);
        parcel.writeString(ownerName);
        parcel.writeString(ownerContact);
        parcel.writeString(userName);
        parcel.writeString(userContact);
    }
}
