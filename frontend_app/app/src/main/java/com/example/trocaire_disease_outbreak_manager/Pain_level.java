package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Pain_level extends AppCompatActivity implements View.OnClickListener {

    private JSONObject data, symptoms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pain_level);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Pain Level</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.btn_submitFrontPain);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        SeekBar seekBar = findViewById(R.id.frontPainLevelSeekBar);
        double percentage = Math.round(seekBar.getProgress()/10);

        CheckBox right_hand = findViewById(R.id.chk_painFront_rightHand);
        CheckBox left_hand = findViewById(R.id.chk_painFront_leftHand);
        CheckBox right_elbow = findViewById(R.id.chk_painFront_rightForearm);
        CheckBox left_elbow = findViewById(R.id.chk_painFront_leftForearm);
        CheckBox right_shoulder = findViewById(R.id.chk_painFront_rightUpperArm);
        CheckBox left_shoulder = findViewById(R.id.chk_painFront_leftUpperArm);
        CheckBox head = findViewById(R.id.chk_painFront_Head);
        CheckBox neck = findViewById(R.id.chk_painFront_Neck);
        CheckBox center_chest = findViewById(R.id.chk_painFront_chest);
        CheckBox right_hip = findViewById(R.id.chk_painFront_rightStomach);
        CheckBox left_hip = findViewById(R.id.chk_painFront_leftStomach);
        CheckBox genitals = findViewById(R.id.chk_painFront_genitals);
        CheckBox left_knee= findViewById(R.id.chk_painFront_leftThigh);
        CheckBox right_knee = findViewById(R.id.chk_painFront_rightThigh);
        CheckBox right_foot = findViewById(R.id.chk_painFront_rightShin);
        CheckBox left_foot = findViewById(R.id.chk_painFront_leftShin);

        JSONObject location = new JSONObject();
        try {
            location.put("head", head.isChecked());
            location.put("neck", neck.isChecked());
            location.put("leftShoulder", left_shoulder.isChecked());
            location.put("rightShoulder", right_shoulder.isChecked());
            location.put("leftArm", left_elbow.isChecked());
            location.put("rightArm", right_elbow.isChecked());
            location.put("leftHand", left_hand.isChecked());
            location.put("rightHand", right_hand.isChecked());
            location.put("centerChest", center_chest.isChecked());
            location.put("leftHip", left_hip.isChecked());
            location.put("rightHip", right_hip.isChecked());
            location.put("genitals", genitals.isChecked());
            location.put("leftKnee", left_knee.isChecked());
            location.put("rightKnee", right_knee.isChecked());
            location.put("leftFoot", left_foot.isChecked());
            location.put("rightFoot", right_foot.isChecked());
            symptoms.put("painLocation", location);
            symptoms.put("painDiscomfortLevel", percentage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(Pain_level.this, Other_information.class);
        con.putExtra("data", data.toString());
        con.putExtra("symptoms", symptoms.toString());
        Pain_level.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}