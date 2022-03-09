package com.ematrix.infotech.uniupdates.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.ematrix.infotech.uniupdates.Adapter.ShowUniversityDataAdapter;
import com.ematrix.infotech.uniupdates.Model.University;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySelectUniversity extends AppCompatActivity {

    private final int i = 0;
    private Context mContext;
    private RecyclerView rvdata;
    private SwipeRefreshLayout swiperefresh;
    private ShowUniversityDataAdapter dataAdapter;
    private Button BtnSelectUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_university);
        mContext = ActivitySelectUniversity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select SaveUniversity");
        setSupportActionBar(toolbar);
        FindViewByID();
        GetUniversityList();
    }

    public void FindViewByID() {
        BtnSelectUniversity = findViewById(R.id.BtnSelectUniversity);
        rvdata = (RecyclerView) findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
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
                Intent mIntent = new Intent(mContext, ActivitySelectStream.class);
                startActivity(mIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        String name = ActivitySelectUniversity.class.getSimpleName();
        dataAdapter = new ShowUniversityDataAdapter(mContext, WebServices.universityList, name);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        //rvdata.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvdata.setLayoutManager(gridLayoutManager);
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }
}