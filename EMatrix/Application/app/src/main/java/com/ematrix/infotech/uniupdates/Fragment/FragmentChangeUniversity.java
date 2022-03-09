package com.ematrix.infotech.uniupdates.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ematrix.infotech.uniupdates.Activity.ActivityDashboard;
import com.ematrix.infotech.uniupdates.Activity.ActivitySelectUniversity;
import com.ematrix.infotech.uniupdates.Adapter.ShowUniversityDataAdapter;
import com.ematrix.infotech.uniupdates.Model.University;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentChangeUniversity extends Fragment {

    private final int i = 0;
    private View view;
    private Context mContext;
    private RecyclerView rvdata;
    private SwipeRefreshLayout swiperefresh;
    private ShowUniversityDataAdapter dataAdapter;
    private Button BtnSelectUniversity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_university, container, false);
        mContext = container.getContext();
        FindViewByID();
        GetUniversityList();
        return view;
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    startActivity(new Intent(mContext, ActivityDashboard.class));
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void FindViewByID() {
        BtnSelectUniversity = view.findViewById(R.id.BtnSelectUniversity);
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetUniversityList();
                swiperefresh.setRefreshing(false);
            }
        });
        BtnSelectUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //After save university data
                //Toast.makeText(mContext, ""+ SaveUniversity.GetUniversityName(mContext), Toast.LENGTH_SHORT).show();
                FragmentChangeStream mFragmentChangeStream = new FragmentChangeStream();
                setFragment(mFragmentChangeStream);
            }
        });
    }

    public void GetUniversityList() {
        Utils.AppColorShowProgressBar(mContext);
        RequestQueue mRequestQueue = new Volley().newRequestQueue(mContext);
        try {
            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GetUniversityNameList, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utils.CloseProgressBar(mContext);
                    WebServices.universityList.clear();
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                University university = new University();

                                university.setUniversityID(object.getString("UniversityID"));
                                university.setUniversityName(object.getString("UniversityName"));
                                university.setUniversityWebsite(object.getString("UniversityWebsite"));
                                university.setUploadimg(object.getString("uploadimg"));

                                WebServices.universityList.add(university);
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
        String name = FragmentChangeUniversity.class.getSimpleName();
        dataAdapter = new ShowUniversityDataAdapter(mContext, WebServices.universityList, name);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2,LinearLayoutManager.VERTICAL,false);
        //rvdata.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvdata.setLayoutManager(gridLayoutManager);
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }

}