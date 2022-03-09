package com.ematrix.infotech.uniupdates.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ematrix.infotech.uniupdates.Model.Stream;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.SharedPrefrances.SaveStream;
import com.ematrix.infotech.uniupdates.Utils.WebServices;

import java.util.List;

public class ShowStreamDataAdapter extends RecyclerView.Adapter<ShowStreamDataAdapter.MyViewHolder> {

    Context context;
    List<Stream> dataModelList;
    String whichInstance;

    public ShowStreamDataAdapter(Context context, List<Stream> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Stream dataModel = dataModelList.get(position);
        holder.StreamName.setText(dataModel.getStreamName());
        String StreamID = SaveStream.GetStream(context);
        if (StreamID != null) {
            String[] namesList = StreamID.split(",");
            for (String Id : namesList) {
                if (dataModel.getStreamID().equals(Id)) {
                    holder.SelectedStream.setChecked(true);
                    WebServices.SelectedStreamList.add(Id);
                }
            }
        }

        holder.SelectedStream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    WebServices.SelectedStreamList.add(dataModel.getStreamID());
                } else {
                    WebServices.SelectedStreamList.remove(dataModel.getStreamID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView StreamName;
        CheckBox SelectedStream;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            StreamName = itemView.findViewById(R.id.StreamName);
            SelectedStream = itemView.findViewById(R.id.SelectedStream);
        }
    }
}
