package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Rash_type extends AppCompatActivity implements View.OnClickListener {

    private JSONObject data, symptoms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rash_type);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Rash Type</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.btnSubmitRashType);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        CheckBox clustered =  findViewById(R.id.chkClustered);
        CheckBox large_patches =  findViewById(R.id.chkLargePatches);
        CheckBox mixture =  findViewById(R.id.chkMixture);
        CheckBox single_scaley = findViewById(R.id.chkSingleScaley);
        CheckBox small_spots =  findViewById(R.id.chkSmallSpots);
        CheckBox small_scaley = findViewById(R.id.SmallScaley);

        boolean clustered_isChecked = clustered.isChecked();
        boolean large_patches_isChecked = large_patches.isChecked();
        boolean mixture_isChecked = mixture.isChecked();
        boolean single_scaley_isChecked = single_scaley.isChecked();
        boolean small_spots_isChecked = small_spots.isChecked();
        boolean small_scaley_isChecked = small_scaley.isChecked();

        String rash = "";
        if (clustered_isChecked){
            rash = "Clustered";
        }else if(large_patches_isChecked){
            rash = "Large Patches";
        }else if(mixture_isChecked){
            rash = "Mixture";
        }else if(single_scaley_isChecked){
            rash =  "Single Scaly";
        }else if(small_spots_isChecked){
            rash = "Small Spots";
        }else if(small_scaley_isChecked){
            rash = "Small Scaly";
        }

        try {
            JSONObject rash_data = symptoms.getJSONObject("rash");
            rash_data.put("rashType", rash);
            symptoms.put("rash", rash_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(Rash_type.this, Rash_locator_front.class);
        con.putExtra("data", data.toString());
        con.putExtra("symptoms", symptoms.toString());
        Rash_type.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}