package com.ematrix.infotech.uniupdates.Utils;

import com.ematrix.infotech.uniupdates.Model.Stream;
import com.ematrix.infotech.uniupdates.Model.University;
import com.ematrix.infotech.uniupdates.Model.Updates;

import java.util.ArrayList;

public class WebServices {

    public static final String BASE_URL = "https://dev.ematrixinfotech.com/uni/api/";
    //public static final String BASE_URL = "http://192.168.2.21/uni/api/";
   
    public static final String GetUniversityNameList = BASE_URL + "GetUniversityNameList/";
    public static final String GetStreamNameList = BASE_URL + "GetStreamNameList/";
    public static final String GetUpdatesList = BASE_URL + "GetUpdatesList/";
    public static final String NewFirebaseToken = BASE_URL + "NewFirebaseToken/";
    public static final String GetUpdatesSingle = BASE_URL + "GetUpdatesSingle/";

    public static ArrayList<University> universityList = new ArrayList<>();
    public static ArrayList<Stream> StreamList = new ArrayList<>();
    public static ArrayList<Updates> UpdatesList = new ArrayList<>();
    public static ArrayList<String> SelectedStreamList = new ArrayList<>();
}