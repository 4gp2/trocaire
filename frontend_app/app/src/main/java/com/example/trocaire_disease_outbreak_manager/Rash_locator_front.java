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

public class Rash_locator_front extends AppCompatActivity implements View.OnClickListener {

    private JSONObject data, symptoms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rash_locator_front);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String page_title = getString(R.string.rash_front);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

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
        CheckBox right_foot = findViewById(R.id.checkBox15);
        CheckBox left_foot = findViewById(R.id.checkBox16);

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

            JSONObject rash_data = symptoms.getJSONObject("rash");
            rash_data.put("rashLocationFront", location);
            symptoms.put("rash", rash_data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(getApplicationContext(), Rash_locator_back.class);
        con.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.putExtra("data", data.toString());
        con.putExtra("symptoms", symptoms.toString());
        getApplicationContext().startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
