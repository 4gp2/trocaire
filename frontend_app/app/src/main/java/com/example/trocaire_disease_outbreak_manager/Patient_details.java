package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
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
        Intent con = new Intent(view.getContext(), Symptoms_input.class);
        view.getContext().startActivity(con);
    }



}
