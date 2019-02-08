package com.iit.rentals.utils;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.iit.rentals.R;

public class FilePaths {
    public static final String USER = "users";
    public static final String HOTEL = "hotels";
    public static final String CATEGORY = "categories";
    public static final String ROOM = "rooms";
    public static final String OTHERS = "otherRentals";
    public static final String BOOKMARK = "userBookmarks";
    public static final String DEFAULT_IMAGE = "https://www.elegantthemes.com/blog/wp-content/uploads/2016/04/category-plugins-header.png";
    public static final String BOOKNOW = "hotelBookings";
    public static final String IMAGE_URI = "drawable://" + R.drawable.main;

    @NonNull
    public static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Rentals/";
    }

    @NonNull
    public static String getNameFromUrl(final String url) {
        return Uri.parse(url).getLastPathSegment();
    }
}
