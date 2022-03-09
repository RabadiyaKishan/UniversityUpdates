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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.ematrix.infotech.uniupdates.Adapter.ShowStreamDataAdapter;
import com.ematrix.infotech.uniupdates.Model.Stream;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveStream;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveUniversity;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentChangeStream extends Fragment {

    private final int i = 0;
    private Context mContext;
    private RecyclerView rvdata;
    private SwipeRefreshLayout swiperefresh;
    private ShowStreamDataAdapter dataAdapter;
    private Button BtnSelectStream;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_stream, container, false);
        mContext = container.getContext();
        FindViewByID();
        GetStreamList();
        WebServices.SelectedStreamList.clear();
        return view;
    }

    public void FindViewByID() {
        BtnSelectStream = view.findViewById(R.id.BtnSelectStream);
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetStreamList();
                swiperefresh.setRefreshing(false);
            }
        });
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

                    SaveStream.DeleteStream(mContext);

                    SaveStream.SaveStream(mContext, strList);

                    WebServices.SelectedStreamList.clear();

                    startActivity(new Intent(mContext, ActivityDashboard.class));
                } else {
                    Toast.makeText(mContext, "Select Stream", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    FragmentChangeUniversity mFragmentChangeUniversity = new FragmentChangeUniversity();
                    setFragment(mFragmentChangeUniversity);
                    return true;
                }
                return false;
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
                    Utils.CloseProgressBar(mContext);
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
        String name = FragmentChangeStream.class.getSimpleName();
        dataAdapter = new ShowStreamDataAdapter(mContext, WebServices.StreamList, name);
        rvdata.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }
}