package com.example.trocaire_disease_outbreak_manager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Review_patient_details extends AppCompatActivity implements View.OnClickListener, LocationListener {

    double latitude, longitude;
    private JSONObject data;
    private static final String FILE_NAME = "patients.json";
    private boolean waiting_for_gps = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_patient_details);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String page_title = getString(R.string.Review_patient_details);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);



        Location locationCurrent = getLastKnownLocation();
        try {
            latitude = locationCurrent.getLatitude();
            longitude = locationCurrent.getLongitude();
        }catch (Exception e){
            latitude = -200;
            longitude = -200;
        }

        try {
            data.put("latitude", latitude);
            data.put("longitude", longitude);
            view_data(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button next = findViewById(R.id.buttonreviewdetails);
        next.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void store_data(JSONObject data) throws JSONException, IOException {

        File file = new File (this.getFilesDir(), FILE_NAME);

        if (!file.exists()){
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("{'patients':[]}");
            bufferedWriter.flush();
        }

        StringBuilder output = new StringBuilder();
        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while((line = bufferedReader.readLine()) != null){
            output.append(line).append("\n");
        }

        String file_data = output.toString();
        bufferedReader.close();

        JSONObject obj = new JSONObject(file_data);
        JSONArray patients = obj.getJSONArray("patients");

        // If patient already exists then replace
        for (int i = 0; i < patients.length(); i++) {
            JSONObject current = (JSONObject) patients.get(i);
            if (current.get("firstName").equals(data.get("firstName")) && current.get("lastName").equals(data.get("lastName")) && current.get("dob").equals(data.get("dob"))) {

                //replace patient
                patients.remove(i);

            }
        }

        patients.put(data);

        JSONObject ret = new JSONObject();
        ret.put("patients", patients);

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(ret.toString());
        bufferedWriter.close();
    }


    public void view_data(JSONObject data) throws JSONException {

        Log.d("MAGGA", "view_data: " + data.toString());

        LinearLayout lv = findViewById(R.id.lv_review);

        String id = "<b>Personal Details:</b><br>"
                + "<br>First Name: " + data.getString("firstName")
                + "<br>Last Name: " + data.getString("lastName")
                + "<br>Village: " + data.getString("village")
                + "<br>DOB: " + data.getString("dob")
                + "<br>Sex: " + data.getString("sex")
                + "<br>Time: " + data.getString("date")
                + "<br>Latitude: " + data.getDouble("latitude")
                + "<br>Longitude: " + data.getDouble("longitude");
        CardView c = createCardViewProgrammatically(id);
        lv.addView(c);

        JSONObject symptoms = data.getJSONObject("symptoms");
        id = "<b>General Symptoms:</b><br>"
                + "<br>Nausea: " + symptoms.getBoolean("nausea")
                + "<br>Diarrhea: " + symptoms.getBoolean("diarrhea")
                + "<br>High Heart Rate: " + symptoms.getBoolean("highHeartRate")
                + "<br>Dehydration: " + symptoms.getBoolean("dehydration")
                + "<br>Muscle Aches: " + symptoms.getBoolean("muscleAches")
                + "<br>Dry Cough: " + symptoms.getBoolean("dryCough")
                + "<br>Sore Throat: " + symptoms.getBoolean("soreThroat")
                + "<br>Headache: " + symptoms.getBoolean("headache")
                + "<br>Sweating: " + symptoms.getBoolean("sweating")
                + "<br>Bloody Stool: " + symptoms.getBoolean("bloodyStool")
                + "<br>Mouth Spots: " + symptoms.getBoolean("mouthSpots")
                + "<br>Stiff Limbs: " + symptoms.getBoolean("stiffLimbs")
                + "<br>Temperature: " + symptoms.getDouble("temperature")
                + "<br>";
        c = createCardViewProgrammatically(id);
        lv.addView(c);

        JSONObject rash_obj = symptoms.getJSONObject("rash");
        boolean rash = rash_obj.getBoolean("hasRash");

        if (rash){
            String rash_type = rash_obj.getString("rashType");
            JSONObject rash_data = rash_obj.getJSONObject("rashLocationFront");
            String rash_front_locations = get_selected_items(rash_data);
            rash_data = rash_obj.getJSONObject("rashLocationBack");
            String rash_back_locations = get_selected_items(rash_data);
            id = "<b>Rash Indicator:</b>"
                    + "<br>Rash Type: " + rash_type
                    + "<br><br><b>Rash Location: (front)</b><br>" + rash_front_locations
                    + "<br><b>Rash Location (back):</b><br>" + rash_back_locations + "<br>";
            c = createCardViewProgrammatically(id);
            lv.addView(c);
        }

        JSONObject pain_obj = symptoms.getJSONObject("pain");
        double pain_level = pain_obj.getDouble("painDiscomfortLevel");

        if (pain_level > 0){
            JSONObject pain_data = pain_obj.getJSONObject("painLocation");

            id = get_selected_items(pain_data);
            id = "<b>Pain:<br></b> <br>"
                    + "Pain Level: " + pain_level
                    + "<br>Pain Location: " + id + "<br>";
            c = createCardViewProgrammatically(id);
            lv.addView(c);
        }

        String other_info = symptoms.getString("otherInfo");

        if (!other_info.isEmpty()){
            id = "<b>Other Information:<br></b>"
                    + other_info + "<br>";
            c = createCardViewProgrammatically(id);
            lv.addView(c);
        }
    }


    public String get_selected_items(JSONObject data) throws JSONException {

        String[] items = {"head", "neck", "leftShoulder", "rightShoulder", "leftArm",
        "rightArm", "leftHand", "rightHand", "centerChest", "genitals",
        "leftHip", "rightHip", "leftKnee", "rightKnee", "leftFoot", "rightFoot"};
        boolean value;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < items.length-1; i++){
            value = data.getBoolean(items[i]);
            if (value){
                String cleanTitle = items[i].replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2");
                result.append(cleanTitle).append(": True<br>");
            }
        }
        return result.toString();
    }


    public CardView createCardViewProgrammatically(String text) {

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
        tv.setText(Html.fromHtml(text));
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#0B85C8"));
        card.addView(tv);
        return card;
    }

    @Override
    public void onClick(View v) {

        try {
            store_data(data);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent con = new Intent(Review_patient_details.this, Fragment_main.class);
        Review_patient_details.this.startActivity(con);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        assert mLocationManager != null;
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            Location loc = mLocationManager.getLastKnownLocation(provider);
            if (loc == null) {
                waiting_for_gps = true;
                continue;
            }
            if (bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = loc;
            }
        }
        return bestLocation;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                else {


                }

            }

        }
    }
}