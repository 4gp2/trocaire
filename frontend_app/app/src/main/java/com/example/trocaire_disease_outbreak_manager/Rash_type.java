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

public class Rash_type extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rash_type);

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

        Boolean clustered_isChecked = clustered.isChecked();
        Boolean large_patches_isChecked = large_patches.isChecked();
        Boolean mixture_isChecked = mixture.isChecked();
        Boolean single_scaley_isChecked = single_scaley.isChecked();
        Boolean small_spots_isChecked = small_spots.isChecked();
        Boolean small_scaley_isChecked = small_scaley.isChecked();

        Intent con = new Intent(Rash_type.this, Rash_locator_front.class);
        Rash_type.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}