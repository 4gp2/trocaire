package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class Patient_details extends Activity implements View.OnClickListener {

    private CheckBox chkSymptom1, chkSymptom2, chkSymptom3;
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

        chkSymptom1 = (CheckBox) findViewById(R.id.chkSymptom1);
        chkSymptom2 = (CheckBox) findViewById(R.id.chkSymptom2);
        chkSymptom3 = (CheckBox) findViewById(R.id.chkSymptom3);
        btnDisplay = (Button) findViewById(R.id.btnSubmitSymptoms);

        StringBuffer result = new StringBuffer();
        result.append("Symptom1 check : ").append(chkSymptom1.isChecked());
        result.append("\nSymptom2 check : ").append(chkSymptom2.isChecked());
        result.append("\nSymptom3 check :").append(chkSymptom3.isChecked());

    }


}
