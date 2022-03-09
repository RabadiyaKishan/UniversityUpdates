package com.ematrix.infotech.uniupdates.SharedPrefrances;

import android.content.Context;
import android.content.SharedPreferences;

import com.ematrix.infotech.uniupdates.Utils.Constant;

public class SaveStream {

    private final Context context;

    public SaveStream(Context context) {
        this.context = context;
    }

    public static void SaveStream(Context context, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.StreamID, value);
        editor.apply();
    }

    public static String GetStream(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, 0);
        return prefs.getString(Constant.StreamID, null);
    }

    public static void DeleteStream(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, 0);
        prefs.edit().remove(Constant.StreamID).commit();
    }
}