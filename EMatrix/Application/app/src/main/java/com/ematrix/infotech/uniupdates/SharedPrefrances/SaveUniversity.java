package com.ematrix.infotech.uniupdates.SharedPrefrances;

import android.content.Context;
import android.content.SharedPreferences;

import com.ematrix.infotech.uniupdates.Utils.Constant;

public class SaveUniversity {

    private final Context context;

    public SaveUniversity(Context context) {
        this.context = context;
    }

    public static void SaveUniversity(Context context, String UniversityID,String UniversityName) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.UniversityID, UniversityID);
        editor.putString(Constant.UniversityName, UniversityName);
        editor.apply();
    }

    public static String GetUniversityID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(Constant.UniversityID, null);
    }

    public static String GetUniversityName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(Constant.UniversityName, null);
    }

    public static void DeleteUniversity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceName, Context.MODE_PRIVATE);
        prefs.edit().remove(Constant.UniversityID).commit();
        prefs.edit().remove(Constant.UniversityName).commit();
    }
}