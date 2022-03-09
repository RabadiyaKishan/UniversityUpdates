package com.ematrix.infotech.uniupdates.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

public class FragmentLetestUpdatesDetails extends Fragment {

    private View view;
    private int position;
    private TextView TxtUpdatesData, WebsiteLink;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_letest_updates_details, container, false);
        mContext = container.getContext();
        FIndViewByID(view);
        Bundle bundle = this.getArguments();
        position = bundle.getInt("position");
        Body(position);
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
                    FragmentLetestUpdates mFragmentLetestUpdates = new FragmentLetestUpdates();
                    setFragment(mFragmentLetestUpdates);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void FIndViewByID(View view) {
        TxtUpdatesData = view.findViewById(R.id.TxtUpdatesData);
        WebsiteLink = view.findViewById(R.id.WebsiteLink);
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