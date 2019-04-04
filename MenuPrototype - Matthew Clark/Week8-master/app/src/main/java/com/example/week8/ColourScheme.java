package com.example.week8;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ColourScheme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_scheme);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ImageView ChangeImage = findViewById(R.id.imageViewColourScheme);
        final Button style1 = findViewById(R.id.style1);
        //final Button clearcache = findViewById(R.id.clearcache);
        final Button style2 = findViewById(R.id.style2);
        style1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage.setImageResource(R.drawable.background2);
                style1.setBackgroundResource(R.drawable.sample2);
                style2.setBackgroundResource(R.drawable.sample2);
                //clearcache.setBackgroundResource(R.drawable.sample2);
                toolbar.setBackgroundColor(0xFF0000);
            }
        });
        style2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage.setImageResource(R.drawable.background);
                style1.setBackgroundResource(R.drawable.sample);
                style2.setBackgroundResource(R.drawable.sample);
                //clearcache.setBackgroundResource(R.drawable.sample);
            }
        });
    }
}