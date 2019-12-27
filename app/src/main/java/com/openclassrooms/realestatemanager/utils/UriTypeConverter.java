package com.openclassrooms.realestatemanager.utils;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriTypeConverter {

    /**
     * Transform a URI object to String
     * @param uri the URI to convert
     * @return a String
     */
    @TypeConverter
    public static String toString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    /**
     * Transform a String object to URI
     * @param string the String to convert
     * @return a Uri
     */
    @TypeConverter
    public static Uri toUri(String string) {
        return string == null ? null : Uri.parse(string);
    }

}
