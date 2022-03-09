package com.ematrix.infotech.uniupdates.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ematrix.infotech.uniupdates.Model.University;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveUniversity;

import java.util.List;

public class ShowUniversityDataAdapter extends RecyclerView.Adapter<ShowUniversityDataAdapter.MyViewHolder> {

    private static int row_index;
    private final Context context;
    private final List<University> dataModelList;
    private final String whichInstance;
    boolean flag = false;
    public ShowUniversityDataAdapter(Context context, List<University> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_with_selection, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        University dataModel = dataModelList.get(position);
        holder.UniversityName.setText(dataModel.getUniversityName());
        Glide.with(context).load(dataModel.getUploadimg()).into(holder.UniversityLogo);

        holder.SelectedUniversity.setChecked(dataModel.getUniversityName().equals(SaveUniversity.GetUniversityName(context)));

        holder.SelectedUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                SaveUniversity.DeleteUniversity(context);
                SaveUniversity.SaveUniversity(context, dataModel.getUniversityID(), dataModel.getUniversityName());
                notifyDataSetChanged();
            }
        });

        holder.SelectedUniversity.setChecked(row_index == position);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whichInstance.equals("ActivitySelectUniversity")) {
                    SaveUniversity.SaveUniversity(context, dataModel.getUniversityID(), dataModel.getUniversityName());
                    Intent mIntent = new Intent(context, ActivitySelectStream.class);
                    context.startActivity(mIntent);
                } else if (whichInstance.equals("FragmentChangeUniversity")) {
                    SaveUniversity.DeleteUniversity(context);
                    SaveUniversity.SaveUniversity(context, dataModel.getUniversityID(), dataModel.getUniversityName());
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new FragmentChangeStream();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, myFragment).addToBackStack("FragmentChangeUniversity").commit();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView UniversityLogo;
        TextView UniversityName;
        CheckBox SelectedUniversity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            SelectedUniversity = itemView.findViewById(R.id.SelectedUniversity);
            UniversityLogo = itemView.findViewById(R.id.UniversityLogo);
            UniversityName = itemView.findViewById(R.id.UniversityName);
        }
    }
}
