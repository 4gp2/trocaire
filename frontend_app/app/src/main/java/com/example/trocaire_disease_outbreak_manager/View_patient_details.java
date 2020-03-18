package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class View_patient_details extends AppCompatActivity implements View.OnClickListener{

    private static final String FILE_NAME = "patients.json";

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

        final String token = getIntent().getStringExtra("token");
        if (token != null) {
            Log.d("patient", token);
        }

        JSONObject patient_data = null;
        try {
            patient_data = view_patient_data();
            patient_data.put("token", token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject data = patient_data;
        Button next = findViewById(R.id.buttonsenddetails);
        next.setOnClickListener(this);

        next.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    send_data(data.toString());
                    Intent con = new Intent(getApplicationContext(), Fragment_main.class);
                    getApplicationContext().startActivity(con);
                }
        });
    }

    private void send_data(final String data) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://4gp2.hieu.dev/api/upload");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(data);
                    os.flush();
                    os.close();

                    Log.i("STATUS12", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG12" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private JSONObject view_patient_data() throws IOException {

        LinearLayout lv = findViewById(R.id.lv);
        String json_String = get_json_string();
        JSONArray patients = null;
        JSONObject patient_obj = null;

        try {
            patient_obj = new JSONObject(json_String);
            patients = patient_obj.getJSONArray("patients");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject patient;
        String name = null;
        String dob = null;
        String village = null;
        String sex = null;
        assert patients != null;
        for(int i = 0; i < patients.length(); i++)
        {
            try {
                patient = patients.getJSONObject(i);
                name = patient.getString("firstName");
                village = patient.getString("village");
                dob = patient.getString("dob");
                sex = patient.getString("sex");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CardView c = createCardViewProgrammatically(name, dob, village, sex);
            c.setOnClickListener(this);
            lv.addView(c);
        }
        return patient_obj;
    }


    private void clear_json_file(File file) throws IOException {

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("{'patients':[]}");
        bufferedWriter.flush();
    }


    private String get_json_string() throws IOException {

        File file = new File (this.getFilesDir(), FILE_NAME);
        if (file.exists()){
            StringBuilder output = new StringBuilder();
            FileReader fileReader;
            try {
                fileReader = new FileReader(file.getAbsoluteFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    output.append(line).append("\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clear_json_file(file);
            return output.toString();
        }
        return "";
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