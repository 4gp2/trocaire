package com.example.trocaire_disease_outbreak_manager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class View_patient_details extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_records);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>View Patient Records</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        LinearLayout lv = findViewById(R.id.lv);

        for(int i = 0; i < 20; i++)
        {
            // ---- Template filler ----------
            String name = Integer.toString(i);
            String village = Integer.toString(i);
            String age = Integer.toString(i);
            String sex = Integer.toString(i);
            //-----------------------------------
            CardView c = createCardViewProgrammatically(name, age, village, sex);
            c.setOnClickListener(this);
            lv.addView(c);
        }


        Button next = findViewById(R.id.buttonsenddetails);
        next.setOnClickListener(this);

        String token = getIntent().getStringExtra("token");
        if (token != null) {
            Log.d("patient", token);
        }
        next.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "Submit Request Here!",
                            Toast.LENGTH_LONG).show();

                }
            });
    }



    public CardView createCardViewProgrammatically(String name, String age, String village,
                                                   String sex) {


        String text = "Name: " + name + "\nAge: " + age + "\nVillage: " + village + "\nSex: " + sex;

        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        card.setLayoutParams(params);

        card.setRadius(9);
        card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        card.setMaxCardElevation(15);
        card.setCardElevation(9);
        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#0B85C8"));
        card.addView(tv);
        return card;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Submit Request Here!", Toast.LENGTH_LONG).show();

        Toast.makeText(getApplication(), "CARD HIT",
                Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}