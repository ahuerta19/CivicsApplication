package com.huertaalexis.civiladvocacy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    ImageView offImg;
    TextView offTitle;
    TextView offName;

    OfficialViewHolder(View view){
        super(view);
        offImg = view.findViewById(R.id.officialPhoto);
        offTitle = view.findViewById(R.id.officialTitle);
        offName = view.findViewById(R.id.officialName);

    }

}
