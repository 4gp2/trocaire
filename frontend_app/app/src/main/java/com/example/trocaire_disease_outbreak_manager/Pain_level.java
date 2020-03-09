package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Pain_level extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pain_level);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Pain location/intensity</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.btn_submitFrontPain);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        CheckBox right_hand = findViewById(R.id.chk_painFront_rightHand);
        CheckBox left_hand = findViewById(R.id.chk_painFront_leftHand);
        CheckBox right_elbow = findViewById(R.id.chk_painFront_rightForearm);
        CheckBox left_elbow = findViewById(R.id.chk_painFront_leftForearm);
        CheckBox right_shoulder = findViewById(R.id.chk_painFront_rightUpperArm);
        CheckBox left_shoulder = findViewById(R.id.chk_painFront_leftUpperArm);
        CheckBox head = findViewById(R.id.chk_painFront_Head);
        CheckBox neck = findViewById(R.id.chk_painFront_Neck);
        CheckBox center_chest = findViewById(R.id.chk_painFront_chest);
        CheckBox right_hip = findViewById(R.id.chk_painFront_rightStomach);
        CheckBox left_hip = findViewById(R.id.chk_painFront_leftStomach);
        CheckBox genitals = findViewById(R.id.chk_painFront_genitals);
        CheckBox left_knee= findViewById(R.id.chk_painFront_leftThigh);
        CheckBox right_knee = findViewById(R.id.chk_painFront_rightThigh);
        CheckBox right_foot = findViewById(R.id.chk_painFront_rightShin);
        CheckBox left_foot = findViewById(R.id.chk_painFront_leftShin);


        SeekBar painLevel = findViewById(R.id.frontPainLevelSeekBar);

        // TODO Add to Json object here for all the pain locations


        Intent con = new Intent(Pain_level.this, Other_information.class);
        Pain_level.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}