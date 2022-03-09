package com.ematrix.infotech.uniupdates.SharedPrefrances;

import android.content.Context;
import android.content.SharedPreferences;

import com.ematrix.infotech.uniupdates.Utils.Constant;

public class SaveFirebaseToken {

    private final Context context;

    public SaveFirebaseToken(Context context) {
        this.context = context;
    }

    public static void SaveFirebaseToken(Context context, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.FirebaseToken, value);
        editor.apply();
    }

    public static String GetFirebaseToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, 0);
        return sharedPref.getString(Constant.FirebaseToken, null);
    }

    public static void DeleteFirebaseToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}