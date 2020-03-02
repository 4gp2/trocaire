package com.example.trocaire_disease_outbreak_manager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Patient_details extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Patient Details</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button submit = findViewById(R.id.btn_submit_patient_details);
        submit.setOnClickListener(this);
    }

    //Run when button is clicked
    @Override
    public void onClick(View view) {

        EditText  village = findViewById(R.id.et_village);
        EditText fullName = findViewById(R.id.et_full_name);

        RadioGroup radioSexGroup = findViewById(R.id.radioSex);
        DatePicker datePicker = findViewById(R.id.dp_DOB);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String sDate = day + "-"+ month + "-" + year;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yyyy");

        Date date = null;
        try {
            date = sdf.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();

        int time_difference = c.getTime().compareTo(date);


        String village_string = village.getText().toString();
        String fullName_string = fullName.getText().toString();

        if (village_string.isEmpty()) {
            village.setError("Enter Valid Village Name");
            village.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            village.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (fullName_string.isEmpty()) {
            fullName.setError("Enter Valid Name");
            fullName.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            fullName.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (time_difference < 0){
            Toast.makeText(getApplication(),
                    "Enter date before todays date", Toast.LENGTH_SHORT).show();
        }

        else if (!fullName_string.isEmpty() && !village_string.isEmpty()) {

            JSONObject data = new JSONObject();


            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            RadioButton radioSexButton = findViewById(selectedId);

            String first_name = fullName_string.substring(0, fullName_string.indexOf(' '));
            String last_name = fullName_string.substring(fullName_string.indexOf(' ') + 1);

            try {
                data.put("firstName", first_name);
                data.put("lastName", last_name);
                assert date != null;
                data.put("dob", date.toString());
                data.put("village", village_string);
                data.put("sex", radioSexButton.getText());
                data.put("date", Calendar.getInstance().getTime());
            }
            catch (JSONException e) {
                    e.printStackTrace();
            }
            Intent con = new Intent(view.getContext(), Symptoms_input.class);
            con.putExtra("data", data.toString());
            view.getContext().startActivity(con);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
