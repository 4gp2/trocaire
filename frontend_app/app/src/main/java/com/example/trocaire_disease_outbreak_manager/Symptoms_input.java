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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Symptoms_input extends AppCompatActivity implements View.OnClickListener {
    private static final String FILE_NAME = "patients.json";

    public static Object DOB, firstName, lastName;

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

        try {
            firstName = data.get("firstName");
            lastName = data.get("lastName");
            DOB =  data.get("dob");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //check if these match any


        try {
            String json_String = get_json_string();
            JSONObject obj = new JSONObject(json_String);
            JSONArray userArray = obj.getJSONArray("patients");
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userDetail = userArray.getJSONObject(i);

                //If this patient exists already then show symptoms
                if (userDetail.get("firstName").equals(firstName) && userDetail.get("lastName").equals(lastName)
                        && userDetail.get("dob").equals(DOB)){

                    if(userDetail.getJSONObject("symptoms").getBoolean("nausea")){
                        CheckBox nausea = this.findViewById(R.id.chkNausea);
                        nausea.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("diarrhea")){
                        CheckBox diarrhea = this.findViewById(R.id.chkDiahorrea);
                        diarrhea.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("highHeartRate")){
                        CheckBox heartRate = this.findViewById(R.id.chkHeartRate);
                        heartRate.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("dehydration")){
                        CheckBox dehydration = this.findViewById(R.id.chkDehydration);
                        dehydration.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("muscleAches")){
                        CheckBox muscleAche = this.findViewById(R.id.chkMusclePain);
                        muscleAche.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("dryCough")){
                        CheckBox dryCough = this.findViewById(R.id.chkCough);
                        dryCough.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("soreThroat")){
                        CheckBox soreThroat = this.findViewById(R.id.chkSoreThroat);
                        soreThroat.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("headache")){
                        CheckBox headache = this.findViewById(R.id.chkHeadache);
                        headache.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("sweating")){
                        CheckBox sweating = this.findViewById(R.id.chkSweating);
                        sweating.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("bloodyStool")){
                        CheckBox bloodyStool = this.findViewById(R.id.chkBloodyStools);
                        bloodyStool.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("mouthSpots")){
                        CheckBox mouthSpot = this.findViewById(R.id.chkMouthSpots);
                        mouthSpot.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getBoolean("stiffLimbs")){
                        CheckBox stiffLimb = this.findViewById(R.id.chkStiffLimbs);
                        stiffLimb.setChecked(true);
                    }
                    if(userDetail.getJSONObject("symptoms").getJSONObject("rash").getBoolean("hasRash")){
                        CheckBox rash = this.findViewById(R.id.chkRash);
                        rash.setChecked(true);
                    }

                }

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

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

    private String get_json_string() throws IOException {

        File file = new File (this.getFilesDir(), FILE_NAME);
        if (file.exists()){
            StringBuilder output = new StringBuilder();
            FileReader fileReader;
            try {
                fileReader = new FileReader(file.getAbsoluteFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    output.append(line).append("\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output.toString();
        }
        return "";
    }

}
