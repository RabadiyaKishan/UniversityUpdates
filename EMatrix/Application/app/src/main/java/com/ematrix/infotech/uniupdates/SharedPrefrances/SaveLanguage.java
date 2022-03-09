package com.ematrix.infotech.uniupdates.SharedPrefrances;

import android.content.Context;
import android.content.SharedPreferences;

import com.ematrix.infotech.uniupdates.Utils.Constant;

public class SaveLanguage {

    private final Context context;

    public SaveLanguage(Context context) {
        this.context = context;
    }

    public static void SaveLanguage(Context context, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.Language, value);
        editor.apply();
    }

    public static String GetLanguage(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, 0);
        return sharedPref.getString(Constant.Language, null);
    }

    public static void DeleteLanguage(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}