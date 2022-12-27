package com.huertaalexis.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.link = findViewById(R.id.infoLink);
        String linkUse = link.getText().toString();
        SpannableString content = new SpannableString(linkUse);
        content.setSpan(new UnderlineSpan(), 0, linkUse.length(), 0);
        link.setText(content);


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web("https://developers.google.com/civic-information/");
            }
            private void web(String s) {
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

    }

}