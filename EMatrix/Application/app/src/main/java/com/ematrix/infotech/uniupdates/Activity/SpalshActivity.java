package com.ematrix.infotech.uniupdates.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ematrix.infotech.uniupdates.Firebase.MyFirebaseMessagingService;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveFirebaseToken;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveStream;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveUniversity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class SpalshActivity extends AppCompatActivity {

    private Context mContext;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SpalshActivity.this;
        ComponentName componentName = new ComponentName(
                getApplicationContext(),
                MyFirebaseMessagingService.class);

        getApplicationContext().getPackageManager().setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseMessaging.getInstance().subscribeToTopic("Global");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("token", "" + token);
                        SaveFirebaseToken.SaveFirebaseToken(mContext, token);
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SaveUniversity.GetUniversityName(mContext) == null) {
                    startActivity(new Intent(mContext, ActivitySelectUniversity.class));
                    SpalshActivity.this.finish();
                } else if (SaveStream.GetStream(mContext) == null) {
                    startActivity(new Intent(mContext, ActivitySelectStream.class));
                    SpalshActivity.this.finish();
                } else if (SaveLanguage.GetLanguage(mContext) == null) {
                    startActivity(new Intent(mContext, ActivitySelectLanguage.class));
                    SpalshActivity.this.finish();
                } else {
                    startActivity(new Intent(mContext, ActivityDashboard.class));
                    SpalshActivity.this.finish();
                }
            }
        }, 3000);
    }
}