package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class Symptoms_input extends AppCompatActivity implements View.OnClickListener {

    private CheckBox chkNausea, chkDiahorrea, chkHeartRate, chkMusclePain, chkDehydration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptoms_page);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Patient Symptoms</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button submit = findViewById(R.id.btnSubmitSymptoms);
        submit.setOnClickListener(this);
    }

    //Run when button is clicked
    @Override
    public void onClick(View v) {

        chkNausea = (CheckBox) findViewById(R.id.chkNausea);
        chkDiahorrea = (CheckBox) findViewById(R.id.chkDiahorrea);
        chkHeartRate = (CheckBox) findViewById(R.id.chkHeartRate);
        chkMusclePain = (CheckBox) findViewById(R.id.chkMusclePain);
        chkDehydration = (CheckBox) findViewById(R.id.chkDehydration);


//        StringBuffer result = new StringBuffer();
//        result.append("Nausea check : ").append(chkNausea.isChecked());
//        result.append("\nDiahorrea check : ").append(chkDiahorrea.isChecked());
//        result.append("\nHeart rate check :").append(chkHeartRate.isChecked());

        Intent con = new Intent(Symptoms_input.this, Thermometer.class);
        Symptoms_input.this.startActivity(con);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
