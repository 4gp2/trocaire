package com.example.trocaire_disease_outbreak_manager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Thermometer extends AppCompatActivity {
    private String DEGREES_CELSIUS = (char) 0x00B0 + "C ";
    private TextView tView;
    private double temperature = 37.5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermometer);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Temperature</font>"));
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
                    Toast.makeText(getApplication(), temperature_string,
                            Toast.LENGTH_LONG).show();
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