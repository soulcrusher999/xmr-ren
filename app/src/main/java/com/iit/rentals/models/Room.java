package com.iit.rentals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    public String id;
    public String name;
    public String desc;
    public String location;
    public String image;
    public String category_name;
    public long contact_no;
    public String price;
    public String owner_name;
    public int no_of_rooms;
    public boolean isFullFlat;
    public String ownerRules;

    public Room() {
    }

    protected Room(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        location = in.readString();
        image = in.readString();
        category_name = in.readString();
        contact_no = in.readLong();
        price = in.readString();
        owner_name = in.readString();
        no_of_rooms = in.readInt();
        isFullFlat = in.readByte() != 0;
        ownerRules = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
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

    public long getContact_no() {
        return contact_no;
    }

    public void setContact_no(long contact_no) {
        this.contact_no = contact_no;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public int getNo_of_rooms() {
        return no_of_rooms;
    }

    public void setNo_of_rooms(int no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public boolean isFullFlat() {
        return isFullFlat;
    }

    public void setFullFlat(boolean fullFlat) {
        isFullFlat = fullFlat;
    }

    public String getOwnerRules() {
        return ownerRules;
    }

    public void setOwnerRules(String ownerRules) {
        this.ownerRules = ownerRules;
    }

    public Room(String id, String name, String desc, String location, String image, String category_name, long contact_no, String price, String owner_name, int no_of_rooms, boolean isFullFlat, String ownerRules) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.image = image;
        this.category_name = category_name;
        this.contact_no = contact_no;
        this.price = price;
        this.owner_name = owner_name;
        this.no_of_rooms = no_of_rooms;
        this.isFullFlat = isFullFlat;
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
        dest.writeLong(contact_no);
        dest.writeString(price);
        dest.writeString(owner_name);
        dest.writeInt(no_of_rooms);
        dest.writeByte((byte) (isFullFlat ? 1 : 0));
        dest.writeString(ownerRules);
    }
}
