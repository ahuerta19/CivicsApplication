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

public class OfficialActivity extends AppCompatActivity {

    ConstraintLayout layout;
    public static String officialName;
    public static String party;
    public static String image;
    public static String officialOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();

        if (intent.hasExtra(Official.class.getName())) {

            Official c = (Official) intent.getSerializableExtra(Official.class.getName());
            if (c == null)
                return;

            TextView loc = findViewById(R.id.locationOfficial);
            loc.setText(MainActivity.getLocation());

            TextView name1 = findViewById(R.id.nameOfficial);
            name1.setText(c.getoName());
            officialName = c.getoName();
            layout = findViewById(R.id.layout);
            if (c.getoParty() != null){
                if(c.getoParty().contains("Democrat")){
                    party = "Democrat";
                    layout.setBackgroundColor(Color.parseColor("#1405BD"));
                }
                else if (c.getoParty().contains("Republican")){
                    party = "Republican";
                    layout.setBackgroundColor(Color.parseColor("#DE0100"));
                }
                else{
                    party = "none";
                    layout.setBackgroundColor(Color.parseColor("#000000"));
                }
            }else{
                layout.setBackgroundColor(Color.parseColor("#000000"));
            }
            TextView officeName = findViewById(R.id.officeName);
            officialOffice = c.getoOffice();
            officeName.setText(c.getoOffice());

            TextView party = findViewById(R.id.partyName);
            party.setText("(" + c.getoParty()+")");

            ImageView officialImg = findViewById(R.id.offImg);
            if(c.getoPhoto()!=null){
                image = c.getoPhoto();
                Glide.with(this)
                        .load(c.getoPhoto())
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.brokenimage)
                        .into(officialImg);
            }else if(c.getoPhone()==null) {
                Picasso.get().load(R.drawable.missing).into(officialImg);
            }

            if(c.getoPhoto()!=null){
                officialImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
                        intent.putExtra("OPENING CLASS", MainActivity.class.getSimpleName());
                        startActivity(intent);
                    }

                });
            }

            ImageButton faceImg = findViewById(R.id.fbButton);
            ImageButton ytImg = findViewById(R.id.ytButton);
            ImageButton twImg = findViewById(R.id.twButton);

            if(c.getoFacebook()==null){
                faceImg.setVisibility(ImageView.INVISIBLE);
            }else{
                faceImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        web("http://www.facebook.com/"+ c.getoFacebook());

                    }

                });
            }

            if(c.getoYoutube()==null){
                ytImg.setVisibility(ImageView.INVISIBLE);
            }else{
                ytImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        web("http://www.youtube.com/"+ c.getoYoutube());

                    }

                });
            }

            if(c.getoTwitter()==null){
                twImg.setVisibility(ImageView.INVISIBLE);
            }else{
                twImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        web("http://www.twitter.com/"+ c.getoTwitter());

                    }

                });
            }

            TextView desc = findViewById(R.id.info);
            if(c.getoAddress()!=""){
                desc.append("Address: " + "\n");
                SpannableString clickLink1 = new SpannableString(c.getoAddress());
                ClickableSpan clickableSpan1 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        web("https://www.google.com/maps/place/" + c.getoAddress());
                        //Toast.makeText(getApplicationContext(), "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
                    }
                };
                clickLink1.setSpan(clickableSpan1, 0,c.getoAddress().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                desc.append(clickLink1);
                desc.append("\n \n");
                desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(c.getoPhone()!=null){
                desc.append("Phone: " + "\n");
                SpannableString clickLink2 = new SpannableString(c.getoPhone());
                ClickableSpan clickableSpan2 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        web("tel:" + c.getoPhone());
                        //Toast.makeText(getApplicationContext(), "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
                    }
                };
                clickLink2.setSpan(clickableSpan2, 0,c.getoPhone().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                desc.append(clickLink2);
                desc.append("\n \n");
                desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(c.getoEmail()!=null){
                desc.append("Email: " + "\n");
                SpannableString clickLink3 = new SpannableString(c.getoEmail());
                ClickableSpan clickableSpan3 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        web("mailto:" + c.getoEmail());
                        //Toast.makeText(getApplicationContext(), "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
                    }
                };
                clickLink3.setSpan(clickableSpan3, 0,c.getoEmail().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                desc.append(clickLink3);
                desc.append("\n \n");
                desc.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if(c.getoWebsite()!=null){
                desc.append("Website: " + "\n");
                SpannableString clickLink4 = new SpannableString(c.getoWebsite());
                ClickableSpan clickableSpan4 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        web(c.getoWebsite());
                        //Toast.makeText(getApplicationContext(), "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
                    }
                };
                clickLink4.setSpan(clickableSpan4, 0,c.getoWebsite().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                desc.append(clickLink4);
                desc.append("\n \n");
                desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
            ImageButton partyButton = findViewById(R.id.partyButton);
            if(c.getoParty().contains("Democrat")){
                Glide.with(this)
                        .load(R.drawable.dem_logo)
                        .into(partyButton);
                partyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        web("https://democrats.org/");
                    }
                });
            }

            else if(c.getoParty().contains("Republican")){
                Glide.with(this)
                        .load(R.drawable.rep_logo)
                        .into(partyButton);
                partyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "It Works! :)", Toast.LENGTH_SHORT).show();
                        web("https://www.gop.com");
                    }
                });
            }
            else{
                partyButton.setVisibility(ImageButton.INVISIBLE);
            }
        }
        }

    private void web(String s) {
            Uri uri = Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    public static String getOfficialName(){
        return officialName;
    }
    public static String getParty(){
        return party;
    }
    public static String getImage(){
        return image;
    }
    public static String getOfficialOffice(){
        return officialOffice;
    }
}