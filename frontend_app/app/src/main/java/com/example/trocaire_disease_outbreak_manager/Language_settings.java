package com.example.trocaire_disease_outbreak_manager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import android.content.Intent;

import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;


public class Language_settings extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_settings);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        String page_title = getString(R.string.Language_settings_title);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";

        bar.setTitle(Html.fromHtml(html_title));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        ImageButton somalian = findViewById(R.id.imageButtonSomalian);

        ImageButton english = findViewById(R.id.imageButtonEnglish);

        somalian.setOnClickListener(this);
        english.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        String lang = "";

        switch (id) {

            case R.id.imageButtonSomalian:
                Toast.makeText(getApplicationContext(),
                        R.string.lang_selected, Toast.LENGTH_SHORT)
                        .show();
                SharedPreferences settings = getSharedPreferences("com.example.trocaire_disease_outbreak_manager",0);
                SharedPreferences.Editor editor = settings.edit();
                lang = "so";
                editor.putString("lang", lang);
                editor.apply();

                break;

            case R.id.imageButtonEnglish:
                Toast.makeText(getApplicationContext(),
                        R.string.lang_selected, Toast.LENGTH_SHORT)
                        .show();
                settings = getSharedPreferences("com.example.trocaire_disease_outbreak_manager",0);
                editor = settings.edit();
                lang = "en-rIE";
                editor.putString("lang",lang);
                editor.apply();

                break;
        }
        changeLang(lang);
        Intent refresh = new Intent(this, Fragment_main.class);
        startActivity(refresh);
    }



    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }



}