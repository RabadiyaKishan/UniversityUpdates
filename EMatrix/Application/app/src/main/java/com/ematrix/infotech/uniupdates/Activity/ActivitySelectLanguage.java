package com.ematrix.infotech.uniupdates.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;

public class ActivitySelectLanguage extends AppCompatActivity {

    private Context mContext;
    private CheckBox ChkEnglish, ChkGujarati;
    private Button BtnSaveLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        mContext = ActivitySelectLanguage.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select Language");
        setSupportActionBar(toolbar);
        FindViewByID();
        Events();
    }

    public void FindViewByID() {
        ChkEnglish = findViewById(R.id.ChkEnglish);
        ChkGujarati = findViewById(R.id.ChkGujarati);
        BtnSaveLanguage = findViewById(R.id.BtnSaveLanguage);
        BtnSaveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ActivityDashboard.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(mContext, ActivitySelectStream.class);
        startActivity(mIntent);
        finish();
    }

    public void Events() {
        ChkEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    String name = ChkEnglish.getText().toString();
                    SaveLanguage.SaveLanguage(mContext, name);
                    ChkGujarati.setChecked(false);
                } else {
                    //ChkGujarati.setChecked(false);
                }
            }
        });

        ChkGujarati.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    String name = ChkGujarati.getText().toString();
                    SaveLanguage.SaveLanguage(mContext, name);
                    ChkEnglish.setChecked(false); // disable checkbox
                } else {
                    //ChkEnglish.setChecked(false);
                }
            }
        });
    }
}