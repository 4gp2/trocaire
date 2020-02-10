package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class Patient_details extends Activity implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);

        Button submit = findViewById(R.id.btn_submit_patient_details);
        submit.setOnClickListener(this);
    }

    //Run when button is clicked
    @Override
    public void onClick(View v) {

        Intent con = new Intent(Patient_details.this, Symptoms_input.class);
        Patient_details.this.startActivity(con);

    }


}
