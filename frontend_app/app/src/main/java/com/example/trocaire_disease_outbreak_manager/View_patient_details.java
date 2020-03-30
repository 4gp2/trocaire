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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;


public class View_patient_details extends AppCompatActivity{

    private static final String FILE_NAME = "patients.json";

    ArrayList<String> firstNames = new ArrayList<>();
    ArrayList<String> lastNames = new ArrayList<>();
    ArrayList<String> villages = new ArrayList<>();
    ArrayList<String> DOBs = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();
    JSONObject obj = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_records);

        final String token = getIntent().getStringExtra("token");
        if (token != null) {
            Log.d("patient", token);
        }

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        try {
            String json_String = get_json_string();
            // get JSONObject from JSON file
            obj = new JSONObject(json_String);
            // fetch JSONArray named patients
            JSONArray userArray = obj.getJSONArray("patients");
            // implement for loop for getting patients list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single patient data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch and store it in arraylist
                firstNames.add(userDetail.getString("firstName"));
                lastNames.add(userDetail.getString("lastName"));
                villages.add(userDetail.getString("village"));
                DOBs.add(userDetail.getString("dob"));
                gender.add(userDetail.getString("sex"));
            }
            obj.put("token", token);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(this, firstNames, lastNames, villages, DOBs, gender);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView


        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        String page_title = getString(R.string.view_patient_records);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.buttonsenddetails);

        next.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    send_data(obj.toString());
                    Intent con = new Intent(getApplicationContext(), Fragment_main.class);
                    con.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}