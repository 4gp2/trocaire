package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;



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
    public void onClick(View view) {

        EditText  village = findViewById(R.id.et_village);
        EditText  gender = findViewById(R.id.et_gender);
        EditText DOB = findViewById(R.id.et_DOB);
        EditText fullName = findViewById(R.id.et_full_name);

        String village_string = village.getText().toString();
        String gender_string = gender.getText().toString();
        String DOB_string = DOB.getText().toString();
        String fullName_string = fullName.getText().toString();

        if (village_string.isEmpty()) {
            village.setError("Enter Valid Village Name");
            village.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            village.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (gender_string.isEmpty()) {
            gender.setError("Enter Valid Gender");
            gender.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            gender.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (DOB_string.isEmpty()) {
            DOB.setError("Enter Valid DOB");
            DOB.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            DOB.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (fullName_string.isEmpty()) {
            fullName.setError("Enter Valid Name");
            fullName.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            fullName.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (!fullName_string.isEmpty() && !gender_string.isEmpty() && !village_string.isEmpty() && !DOB_string.isEmpty()) {
            Intent con = new Intent(view.getContext(), Symptoms_input.class);
            view.getContext().startActivity(con);
        }

    }



}
