package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Thermometer extends AppCompatActivity {
    private String DEGREES_CELSIUS = (char) 0x00B0 + "C ";
    private TextView tView;
    private double temperature = 37.5;
    private JSONObject data, symptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermometer);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String page_title = getString(R.string.Temperature);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        tView = findViewById(R.id.textview1);
        String temp = Double.toString(temperature) + DEGREES_CELSIUS;
        tView.setText(temp);

        ImageButton neg = findViewById(R.id.neg);
        neg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (temperature > 32) {
                    temperature -= 0.5;
                    String temp = Double.toString(temperature) + DEGREES_CELSIUS;
                    tView.setText(temp);
                }
            }
        });

        ImageButton pos = findViewById(R.id.pos);
        pos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (temperature < 42) {
                    temperature += 0.5;
                    String temp = Double.toString(temperature) + DEGREES_CELSIUS;
                    tView.setText(temp);
                }
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text_input = tView.getText().toString();

                Pattern pattern = Pattern.compile("(\\d+\\.\\d+)");
                Matcher m = pattern.matcher(text_input);
                double text_temp = 0;
                if( m.find() ){
                    String i=m.group();
                    text_temp = Double.parseDouble(i);
                }else{
                    pattern = Pattern.compile("(\\d+)");
                    m = pattern.matcher(text_input);
                    if( m.find() ){
                        String i=m.group();
                        text_temp = Double.parseDouble(i);
                    }
                }
                if (text_temp <= 42 && text_temp >=32){
                    tView.setBackgroundResource(R.drawable.rounded_edittext_box);
                    String temperature_string = Double.toString(text_temp);
                    try {
                        symptoms.put("temperature", temperature_string);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean has_rash = false;
                    try {
                        JSONObject obj = symptoms.getJSONObject("rash");
                        has_rash = obj.getBoolean("hasRash");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (has_rash){
                        Intent con = new Intent(getApplicationContext(), Rash_type.class);
                        con.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        con.putExtra("data", data.toString());
                        con.putExtra("symptoms", symptoms.toString());
                        getApplicationContext().startActivity(con);
                    }else{
                        JSONObject rash_data = null;
                        try {
                            rash_data = symptoms.getJSONObject("rash");
                            rash_data.put("rashType", "");

                            JSONObject location1 = new JSONObject();
                            try {
                                location1.put("head", false);
                                location1.put("neck", false);
                                location1.put("leftShoulder", false);
                                location1.put("rightShoulder", false);
                                location1.put("leftArm", false);
                                location1.put("rightArm", false);
                                location1.put("leftHand", false);
                                location1.put("rightHand", false);
                                location1.put("centerChest", false);
                                location1.put("leftHip", false);
                                location1.put("rightHip", false);
                                location1.put("genitals", false);
                                location1.put("leftKnee", false);
                                location1.put("rightKnee", false);
                                location1.put("leftFoot", false);
                                location1.put("rightFoot", false);
                                rash_data.put("rashLocationFront", location1);
                                rash_data.put("rashLocationBack", location1);
                                symptoms.put("rash", rash_data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent con = new Intent(getApplicationContext(), Pain_level.class);
                        con.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        con.putExtra("data", data.toString());
                        con.putExtra("symptoms", symptoms.toString());
                        getApplicationContext().startActivity(con);

                    }


                }else{
                    tView.setError("Temperature between 32" +
                            DEGREES_CELSIUS +  "and 42" + DEGREES_CELSIUS);
                    tView.setBackgroundResource(R.drawable.rounded_edittext_error);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}