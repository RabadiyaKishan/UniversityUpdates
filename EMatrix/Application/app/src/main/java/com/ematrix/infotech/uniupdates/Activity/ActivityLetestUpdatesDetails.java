package com.ematrix.infotech.uniupdates.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLetestUpdatesDetails extends AppCompatActivity {

    private View view;
    private int position;
    private TextView TxtUpdatesData, WebsiteLink;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letest_updates_details);
        mContext = ActivityLetestUpdatesDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FIndViewByID();
        Intent mIntent = getIntent();
        if (mIntent.hasExtra("position")) {
            position = mIntent.getIntExtra("position", 0);
            Body(position);
        } else if (mIntent.hasExtra("PostID")) {
            String position = mIntent.getStringExtra("PostID");
            GetPost(position);
        }
    }

    private void GetPost(String position) {
        Utils.AppColorShowProgressBar(mContext);
        RequestQueue mRequestQueue = new Volley().newRequestQueue(mContext);
        try {
            JSONObject mObject = new JSONObject();
            mObject.put("UpID", position);
            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GetUpdatesSingle, mObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utils.CloseProgressBar(mContext);
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String Link = object.getString("UniversityLink");
                                if ("English".equals(SaveLanguage.GetLanguage(mContext))) {
                                    TxtUpdatesData.setText(object.getString("English"));
                                } else if ("Gujarati".equals(SaveLanguage.GetLanguage(mContext))) {
                                    TxtUpdatesData.setText(object.getString("Gujrati"));
                                }
                                WebsiteLink.setText(Link);
                                WebsiteLink.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(Link));
                                        startActivity(browserIntent);
                                    }
                                });
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void FIndViewByID() {
        TxtUpdatesData = findViewById(R.id.TxtUpdatesData);
        WebsiteLink = findViewById(R.id.WebsiteLink);
    }

    private void Body(int position) {
        if ("English".equals(SaveLanguage.GetLanguage(mContext))) {
            TxtUpdatesData.setText(WebServices.UpdatesList.get(position).getEnglish());
        } else if ("Gujarati".equals(SaveLanguage.GetLanguage(mContext))) {
            TxtUpdatesData.setText(WebServices.UpdatesList.get(position).getGujrati());
        }
        WebsiteLink.setText(WebServices.UpdatesList.get(position).getUniversityLink());
        WebsiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(WebServices.UpdatesList.get(position).getUniversityLink()));
                startActivity(browserIntent);
            }
        });
    }
}