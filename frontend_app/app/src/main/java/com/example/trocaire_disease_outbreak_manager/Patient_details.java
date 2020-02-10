package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class Patient_details extends Activity implements View.OnClickListener {

    private CheckBox chkNausea, chkDiahorrea, chkHeartRate, chkMusclePain, chkDehydration;
    private Button btnDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);

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


        btnDisplay = (Button) findViewById(R.id.btnSubmitSymptoms);

        StringBuffer result = new StringBuffer();
        result.append("Nausea check : ").append(chkNausea.isChecked());
        result.append("\nDiahorrea check : ").append(chkDiahorrea.isChecked());
        result.append("\nHeart rate check :").append(chkHeartRate.isChecked());

        Intent con = new Intent(Patient_details.this, Thermometer.class);
        Patient_details.this.startActivity(con);

    }


}
