package com.ematrix.infotech.uniupdates.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.ematrix.infotech.uniupdates.Activity.ActivityDashboard;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;

public class FragmentChangeLanguage extends Fragment {

    private Context mContext;
    private CheckBox ChkEnglish, ChkGujarati;
    private Button BtnSaveLanguage;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_language, container, false);
        mContext = container.getContext();
        FindViewByID();
        Events();
        return view;
    }

    public void FindViewByID() {
        ChkEnglish = view.findViewById(R.id.ChkEnglish);
        ChkGujarati = view.findViewById(R.id.ChkGujarati);
        if ("Gujarati".equals(SaveLanguage.GetLanguage(mContext))) {
            ChkGujarati.setChecked(true);
        } else if ("English".equals(SaveLanguage.GetLanguage(mContext))) {
            ChkEnglish.setChecked(true);
        }
        BtnSaveLanguage = view.findViewById(R.id.BtnSaveLanguage);
        BtnSaveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ActivityDashboard.class));
                getActivity().finish();
            }
        });
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