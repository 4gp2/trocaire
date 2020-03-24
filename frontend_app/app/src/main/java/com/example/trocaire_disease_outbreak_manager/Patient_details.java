package com.example.trocaire_disease_outbreak_manager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

    public static String firstName;
    public static String lastName;
    public static String patientFullName;
    public static String villageName;
    public static String DOB;
    public static String gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String page_title = getString(R.string.patient_details);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Bundle bundle=getIntent().getExtras();

        if(bundle != null){
            firstName = bundle.getString("first");
            lastName = bundle.getString("last");
            villageName = bundle.getString("village");
            DOB = bundle.getString("DOB");
            gender = bundle.getString("gender");

            if(firstName!=null && lastName != null && villageName!=null && DOB !=null && gender!=null) {
                patientFullName = (firstName + " " + lastName);

                // Set full name and village
                EditText fullName = findViewById(R.id.et_full_name);
                EditText village = findViewById(R.id.et_village);

                fullName.setText(patientFullName);
                village.setText(villageName);

                // Set date of birth
                // DOB: Fri Mar 13 00:00:00 GMT 2020
                int year = Integer.parseInt(DOB.substring(24, 28));
                int dayOfMonth = Integer.parseInt(DOB.substring(8,10));
                int monthOfYear = getMonth(DOB.substring(4,7));

                DatePicker dp =  (DatePicker)this.findViewById(R.id.dp_DOB);
                dp.init(year, monthOfYear, dayOfMonth, null);

                // Set gender
                RadioButton radMale = this.findViewById(R.id.radioMale);
                RadioButton radFemale = this.findViewById(R.id.radioFemale);

                if(gender.equals("Male")){
                    radMale.setChecked(true);
                    radFemale.setChecked(false);
                }else{
                    radMale.setChecked(false);
                    radFemale.setChecked(true);
                }

            }
        }

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
            village.setError(getText(R.string.valid_village_name));
            village.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            village.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (fullName_string.isEmpty()) {
            fullName.setError(getText(R.string.valid_name));
            fullName.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            fullName.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (time_difference < 0){
            Toast.makeText(getApplication(),
                    getText(R.string.valid_date), Toast.LENGTH_SHORT).show();
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

    public int getMonth(String month){
        if(month.equals("Jan")){
            return 0;
        }else if(month.equals("Feb")){
            return 1;
        }else if(month.equals("Mar")){
            return 2;
        }else if(month.equals("Apr")){
            return 3;
        }else if(month.equals("May")){
            return 4;
        }else if(month.equals("Jun")){
            return 5;
        }else if(month.equals("Jul")){
            return 6;
        }else if(month.equals("Aug")){
            return 7;
        }else if(month.equals("Sep")){
            return 8;
        }else if(month.equals("Oct")){
            return 9;
        }else if(month.equals("Nov")){
            return 10;
        }else{
            return 11;
        }
    }


}
