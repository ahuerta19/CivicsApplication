package com.huertaalexis.civiladvocacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private final List<Official> oList;
    private final MainActivity mainAct;

    OfficialAdapter(List<Official> empList, MainActivity ma){
        this.oList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_entry, parent, false);
        itemView.setOnClickListener(mainAct);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official official = oList.get(position);
        holder.offTitle.setText(official.getoOffice());
        holder.offName.setText(official.getoName());
        if(official.getoPhoto()!=null){
            Glide.with(mainAct)
                    .load(official.getoPhoto())
                    .placeholder(R.drawable.missing)
                    .error(R.drawable.brokenimage)
                    .into(holder.offImg);
        }else {

            Picasso.get().load(R.drawable.missing).into(holder.offImg);

        }
    }

    @Override
    public int getItemCount() {
        return oList.size();
    }
}
