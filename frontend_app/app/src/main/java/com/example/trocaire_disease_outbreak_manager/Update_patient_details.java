package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Update_patient_details extends AppCompatActivity implements View.OnClickListener {


    ArrayList<String> firstNames = new ArrayList<>();
    ArrayList<String> lastNames = new ArrayList<>();
    ArrayList<String> villages = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_patient_details);

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named patients
            JSONArray userArray = obj.getJSONArray("patients");
            // implement for loop for getting patients list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single patient data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch and store it in arraylist
                firstNames.add(userDetail.getString("firstName"));
                lastNames.add(userDetail.getString("lastName"));
                villages.add(userDetail.getString("village"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(this, firstNames, lastNames, villages);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Update Patient Details</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

//        Button next = findViewById(R.id.buttonupdatedetails);
//        next.setOnClickListener(this);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("sample.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        Intent con = new Intent(Update_patient_details.this, Fragment_main.class);
        Update_patient_details.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}