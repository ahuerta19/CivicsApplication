package com.huertaalexis.civiladvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PhotoActivity extends AppCompatActivity {
    ConstraintLayout layout;
    TextView offName;
    TextView offOffice;
    ImageView offPhoto;
    ImageButton partyButton;
    TextView loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        layout = findViewById(R.id.photoLayout);

        this.loc = findViewById(R.id.locationPhoto);
        loc.setText(MainActivity.getLocation());

        this.offName = findViewById(R.id.photoOff);
        offName.setText(OfficialActivity.getOfficialName());

        this.offOffice = findViewById(R.id.officeOff);
        offOffice.setText(OfficialActivity.getOfficialOffice());

        this.offPhoto = findViewById(R.id.photoImg);
        String offImg = OfficialActivity.getImage();

        if(offImg!=null){
            Glide.with(this)
                    .load(offImg)
                    .placeholder(R.drawable.missing)
                    .error(R.drawable.brokenimage)
                    .into(offPhoto);
        }else {
            Picasso.get().load(R.drawable.missing).into(offPhoto);
        }

        this.partyButton = findViewById(R.id.partyButton2);

        String party = OfficialActivity.getParty();

        if(party.equals("Democrat")){
            layout.setBackgroundColor(Color.parseColor("#1405BD"));
            Glide.with(this)
                    .load(R.drawable.dem_logo)
                    .into(partyButton);
            partyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                    web("https://democrats.org/");
                }
                private void web(String s) {
                    Uri uri = Uri.parse(s);
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });
        }
        else if(party.equals("Republican")){
            layout.setBackgroundColor(Color.parseColor("#DE0100"));
            Glide.with(this)
                    .load(R.drawable.rep_logo)
                    .into(partyButton);
            partyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                    web("https://www.gop.com");
                }
                private void web(String s) {
                    Uri uri = Uri.parse(s);
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });
        }
        else{
            layout.setBackgroundColor(Color.parseColor("#000000"));
            partyButton.setVisibility(ImageView.INVISIBLE);
        }
    }
}
