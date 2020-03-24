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

import org.json.JSONException;
import org.json.JSONObject;


public class Symptoms_input extends AppCompatActivity implements View.OnClickListener {

    private CheckBox chkNausea, chkDiahorrea, chkHeartRate, chkMusclePain, chkDehydration,
            chkCough, chkSoreThroat, chkHeadache, chkRash, chkSweating, chkBloodyStools,
            chkMouthSpots, chkStiffLimbs;
    private JSONObject data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptoms_page);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String page_title = getString(R.string.patient_symptoms_title);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button submit = findViewById(R.id.btnSubmitSymptoms);
        submit.setOnClickListener(this);
    }

    //Run when button is clicked
    @Override
    public void onClick(View v) {

        chkNausea = findViewById(R.id.chkNausea);
        chkDiahorrea = findViewById(R.id.chkDiahorrea);
        chkHeartRate = findViewById(R.id.chkHeartRate);
        chkMusclePain = findViewById(R.id.chkMusclePain);
        chkDehydration = findViewById(R.id.chkDehydration);
        chkCough =  findViewById(R.id.chkCough);
        chkSoreThroat = findViewById(R.id.chkSoreThroat);
        chkHeadache = findViewById(R.id.chkHeadache);
        chkRash =  findViewById(R.id.chkRash);
        chkSweating = findViewById(R.id.chkSweating);
        chkBloodyStools = findViewById(R.id.chkBloodyStools);
        chkMouthSpots = findViewById(R.id.chkMouthSpots);
        chkStiffLimbs = findViewById(R.id.chkStiffLimbs);

        JSONObject symptoms = new JSONObject();
        JSONObject rash = new JSONObject();

        try {
            symptoms.put("nausea",  chkNausea.isChecked());
            symptoms.put("diarrhea", chkDiahorrea.isChecked());
            symptoms.put("highHeartRate", chkHeartRate.isChecked());
            symptoms.put("dehydration", chkDehydration.isChecked());
            symptoms.put("muscleAches", chkMusclePain.isChecked());
            symptoms.put("dryCough", chkCough.isChecked());
            symptoms.put("soreThroat", chkSoreThroat.isChecked());
            symptoms.put("headache", chkHeadache.isChecked());
            rash.put("hasRash", chkRash.isChecked());
            symptoms.put("rash", rash);
            symptoms.put("sweating", chkSweating.isChecked());
            symptoms.put("bloodyStool", chkBloodyStools.isChecked());
            symptoms.put("mouthSpots", chkMouthSpots.isChecked());
            symptoms.put("stiffLimbs", chkStiffLimbs.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(Symptoms_input.this, Thermometer.class);
        con.putExtra("data", data.toString());
        con.putExtra("symptoms", symptoms.toString());
        Symptoms_input.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
