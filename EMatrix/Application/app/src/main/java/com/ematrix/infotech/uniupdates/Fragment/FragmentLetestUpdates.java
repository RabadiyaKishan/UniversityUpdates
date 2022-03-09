package com.ematrix.infotech.uniupdates.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ematrix.infotech.uniupdates.Adapter.ShowLetestUpdatesDataAdapter;
import com.ematrix.infotech.uniupdates.Adapter.ShowStreamDataAdapter;
import com.ematrix.infotech.uniupdates.Model.Stream;
import com.ematrix.infotech.uniupdates.Model.Updates;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveFirebaseToken;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveStream;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveUniversity;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentLetestUpdates extends Fragment {
    public static Dialog dialog;
    private View view;
    private Context mContext;
    private RecyclerView rvdata;
    private ImageView ImageFilter;
    private LottieAnimationView animationView;
    private SwipeRefreshLayout swiperefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_letest_updates, container, false);
        mContext = container.getContext();
        FindViewByID();
        GetStreamList();
        Log.d("Token", "" + SaveFirebaseToken.GetFirebaseToken(mContext));
        Log.d("UniversityID", "" + SaveUniversity.GetUniversityID(mContext));
        Log.d("Stream", "" + SaveStream.GetStream(mContext));
        RegisterDevice();
        return view;
    }

    public void FindViewByID() {
        ImageFilter = view.findViewById(R.id.ImageFilter);
        animationView = view.findViewById(R.id.animationView);
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        ImageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                LayoutInflater factory = LayoutInflater.from(view.getContext());
                final View view1 = factory.inflate(R.layout.filter, null);
                alertDialog.setView(view1);

                RecyclerView recyclerView = view1.findViewById(R.id.recycler);
                Button BtnSelectStream = view1.findViewById(R.id.BtnSelectStream);
                ShowStreamDataAdapter dataAdapter = new ShowStreamDataAdapter(mContext, WebServices.StreamList, "");
                recyclerView.setAdapter(dataAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                AlertDialog ad = alertDialog.show();
                BtnSelectStream.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (WebServices.SelectedStreamList.size() > 0) {
                            StringBuilder sbString = new StringBuilder();
                            for (String language : WebServices.SelectedStreamList) {
                                sbString.append(language).append(",");
                            }
                            String strList = sbString.toString();
                            if (strList.length() > 0)
                                strList = strList.substring(0, strList.length() - 1);
                            ad.dismiss();
                            SaveStream.DeleteStream(mContext);
                            SaveStream.SaveStream(mContext, strList);
                            WebServices.SelectedStreamList.clear();
                            FragmentLetestUpdates fragment1 = new FragmentLetestUpdates();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment1);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(mContext, "Select Stream", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdates();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    public void GetStreamList() {
        Utils.AppColorShowProgressBar(mContext);
        RequestQueue mRequestQueue = new Volley().newRequestQueue(mContext);
        try {
            JSONObject mObject = new JSONObject();
            mObject.put("UniversityID", SaveUniversity.GetUniversityID(mContext));
            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GetStreamNameList, mObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    WebServices.StreamList.clear();
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Stream stream = new Stream();

                                stream.setStreamID(object.getString("StreamID"));
                                stream.setStreamName(object.getString("StreamName"));
                                stream.setUniversityName(object.getString("UniversityName"));
                                stream.setUniversityID(object.getString("UniversityID"));

                                WebServices.StreamList.add(stream);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getUpdates();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.CloseProgressBar(mContext);
                    Log.d("Error : ", error.toString());
                }
            });
            mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(mJsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUpdates() {
        WebServices.UpdatesList.clear();
        RequestQueue mRequestQueue = new Volley().newRequestQueue(mContext);
        try {
            JSONObject mObject = new JSONObject();
            mObject.put("UniversityID", SaveUniversity.GetUniversityID(mContext));
            mObject.put("StreamList", SaveStream.GetStream(mContext));
            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GetUpdatesList, mObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utils.CloseProgressBar(mContext);
                    WebServices.UpdatesList.clear();
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Updates updates = new Updates();

                                updates.setID(object.getString("ID"));
                                updates.setUniversityID(object.getString("UniversityID"));
                                updates.setStreamID(object.getString("StreamID"));
                                updates.setEnglish(object.getString("English"));
                                updates.setGujrati(object.getString("Gujrati"));
                                updates.setUniversityLink(object.getString("UniversityLink"));

                                WebServices.UpdatesList.add(updates);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setData();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.CloseProgressBar(mContext);
                    Log.d("Error : ", error.toString());
                    if (WebServices.UpdatesList.size() == 0) {
                        rvdata.setVisibility(View.GONE);
                        animationView.setVisibility(View.VISIBLE);
                    }
                }
            });
            mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(mJsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        String name = FragmentLetestUpdates.class.getSimpleName();
        ShowLetestUpdatesDataAdapter dataAdapter = new ShowLetestUpdatesDataAdapter(mContext, WebServices.UpdatesList, name);
        rvdata.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }

    private void RegisterDevice() {
        JSONObject mObject = new JSONObject();
        try {
            mObject.put("fb_token", "" + SaveFirebaseToken.GetFirebaseToken(mContext));
            mObject.put("UniversityID", "" + SaveUniversity.GetUniversityID(mContext));
            mObject.put("StreamID", "" + SaveStream.GetStream(mContext));
            RequestQueue mRequestQueue = new Volley().newRequestQueue(mContext);
            try {
                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.NewFirebaseToken, mObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                boolean Status = jsonObject.getBoolean("Status");
                                String msg = jsonObject.getString("Message");
                                if (Status) {
                                    // true operation success
                                } else {
                                    // something went wrong
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.CloseProgressBar(mContext);
                        Log.d("Error : ", error.toString());
                    }
                });
                mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(mJsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}