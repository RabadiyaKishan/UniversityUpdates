package com.ematrix.infotech.uniupdates.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ematrix.infotech.uniupdates.Activity.ActivityLetestUpdatesDetails;
import com.ematrix.infotech.uniupdates.Model.Updates;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveLanguage;

import java.util.List;

public class ShowLetestUpdatesDataAdapter extends RecyclerView.Adapter<ShowLetestUpdatesDataAdapter.MyViewHolder> {

    private final Context context;
    private final List<Updates> dataModelList;
    private final String whichInstance;

    public ShowLetestUpdatesDataAdapter(Context context, List<Updates> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.letest_updates, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Updates dataModel = dataModelList.get(position);

        if ("English".equals(SaveLanguage.GetLanguage(context))) {
            holder.TxtUpdates.setText(dataModel.getEnglish() + "...");
        } else if ("Gujarati".equals(SaveLanguage.GetLanguage(context))) {
            holder.TxtUpdates.setText(dataModel.getGujrati() + "...");
        }

        holder.ButtonReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, ActivityLetestUpdatesDetails.class);
                mIntent.putExtra("position", position);
                context.startActivity(mIntent);
            }
        });
    }

    protected void setFragment(Fragment fragment, Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TxtUpdates;
        Button ButtonReadMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtUpdates = itemView.findViewById(R.id.TxtUpdates);
            ButtonReadMore = itemView.findViewById(R.id.ButtonReadMore);
        }
    }
}
