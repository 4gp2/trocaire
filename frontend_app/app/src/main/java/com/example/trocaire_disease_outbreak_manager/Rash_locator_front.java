package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Rash_locator_front extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rash_locator_front);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Rash Locator (front)</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.button_next);
        next.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        CheckBox right_hand = findViewById(R.id.checkBox1);
        CheckBox left_hand = findViewById(R.id.checkBox2);
        CheckBox right_elbow = findViewById(R.id.checkBox3);
        CheckBox left_elbow = findViewById(R.id.checkBox4);
        CheckBox right_shoulder = findViewById(R.id.checkBox5);
        CheckBox left_shoulder = findViewById(R.id.checkBox6);
        CheckBox head = findViewById(R.id.checkBox7);
        CheckBox neck = findViewById(R.id.checkBox8);
        CheckBox center_chest = findViewById(R.id.checkBox9);
        CheckBox right_hip = findViewById(R.id.checkBox10);
        CheckBox left_hip = findViewById(R.id.checkBox11);
        CheckBox genitals = findViewById(R.id.checkBox12);
        CheckBox left_knee= findViewById(R.id.checkBox13);
        CheckBox right_knee = findViewById(R.id.checkBox14);
        CheckBox right_foot = findViewById(R.id.checkBox14);
        CheckBox left_foot = findViewById(R.id.checkBox14);


        // Add to Json object here for all the Rash locations

        Toast.makeText(getApplication(), Boolean.toString(head.isChecked()),
                Toast.LENGTH_LONG).show();

        Intent con = new Intent(getApplicationContext(), Rash_locator_back.class);
        con.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
