package com.example.trocaire_disease_outbreak_manager;

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

import org.json.JSONException;
import org.json.JSONObject;

public class Other_information extends AppCompatActivity implements View.OnClickListener {

    private JSONObject data, symptoms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_information);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Any Other Information</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.buttonotherinfo);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText other_information = findViewById(R.id.editTextOtherInfo);
        String other_info = other_information.getText().toString();

        try {
            symptoms.put("otherInfo", other_info);
            data.put("symptoms", symptoms);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(Other_information.this, Review_patient_details.class);
        con.putExtra("data", data.toString());
        Other_information.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}