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
        String page_title = getString(R.string.Any_other_info_title);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

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
        con.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Other_information.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}