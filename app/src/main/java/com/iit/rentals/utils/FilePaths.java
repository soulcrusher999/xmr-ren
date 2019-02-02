package com.iit.rentals.utils;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

public class FilePaths {
    public static final String USER = "users";
    public static final String HOTEL = "hotels";
    public static final String CATEGORY = "categories";
    public static final String ROOM = "rooms";
    public static final String OTHERS = "otherRentals";

    @NonNull
    public static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Rentals/";
    }

    @NonNull
    public static String getNameFromUrl(final String url) {
        return Uri.parse(url).getLastPathSegment();
    }
}
