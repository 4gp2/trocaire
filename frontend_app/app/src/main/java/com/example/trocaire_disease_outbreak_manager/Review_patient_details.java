package com.example.trocaire_disease_outbreak_manager;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class Review_patient_details extends AppCompatActivity implements View.OnClickListener, LocationListener {

    double latitude, longitude;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_patient_details);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Review Patient Submission</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        setup_location();

        JSONObject data = create_sample_patient();
        try {
            view_data(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button next = findViewById(R.id.buttonreviewdetails);
        next.setOnClickListener(this);
    }


    public void view_data(JSONObject data) throws JSONException {

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
                + "<br>Rash: " + symptoms.getBoolean("rash")
                + "<br>Red Eyes: " + symptoms.getBoolean("redEyes")
                + "<br>Temperature: " + symptoms.getDouble("temperature")
                + "<br>PainLevel: " + symptoms.getDouble("painDiscomfortLevel");
        c = createCardViewProgrammatically(id);
        lv.addView(c);

        boolean rash = symptoms.getBoolean("rash");
        if (rash){
            String rash_type = symptoms.getString("rashType");
            boolean itchy = symptoms.getBoolean("itchyRash");
            JSONObject rash_data = symptoms.getJSONObject("rashLocationFront");
            String rash_front_locations = get_selected_items(rash_data);
            rash_data = symptoms.getJSONObject("rashLocationBack");
            String rash_back_locations = get_selected_items(rash_data);
            id = "<b>Rash Indicator:</b>"
                    + "<br>Rash Type: " + rash_type
                    + "<br>Itchy?: " + itchy
                    + "<br><br><b>Rash Location: (front)</b><br>" + rash_front_locations
                    + "<br><b>Rash Location (back):</b><br>" + rash_back_locations;
            c = createCardViewProgrammatically(id);
            lv.addView(c);
        }

        double pain_level = symptoms.getDouble("painDiscomfortLevel");

        if (pain_level > 0){
            JSONObject pain_data = symptoms.getJSONObject("painLocation");
            String pain_type = symptoms.getString("painType");

            id = get_selected_items(pain_data);
            id = "<b>Pain:<br></b> <br>"
                    + "Pain Level: " + pain_level
                    + "<br>Pain Type: " + pain_type
                    + "<br>Pain Location: " + id;
            c = createCardViewProgrammatically(id);
            lv.addView(c);
        }

    }


    public String get_selected_items(JSONObject data) throws JSONException {

        String[] items = {"head", "eyes", "neck", "leftShoulder", "rightShoulder", "leftArm",
        "rightArm", "leftHand", "rightHand", "centerChest", "leftSide", "rightSide", "genitals",
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


    public JSONObject create_sample_patient(){

        JSONObject sample_input = new JSONObject();
        try {
            sample_input.put("firstName", "AAAAA");
            sample_input.put("lastName", "BBBBB");
            sample_input.put("dob", "1990-01-02");
            sample_input.put("village", "CCCCC");
            sample_input.put("sex", "f");
            sample_input.put("latitude", latitude);
            sample_input.put("longitude", longitude);
            sample_input.put("date", Calendar.getInstance().getTime());

            JSONObject symptoms = new JSONObject();
            symptoms.put("nausea", false);
            symptoms.put("diarrhea", true);
            symptoms.put("highHeartRate", true);
            symptoms.put("dehydration", false);
            symptoms.put("muscleAches", true);
            symptoms.put("dryCough", true);
            symptoms.put("soreThroat", false);
            symptoms.put("headache", true);
            symptoms.put("rash", true);
            symptoms.put("redEyes", false);
            symptoms.put("temperature", 39.5);
            symptoms.put("painDiscomfortLevel", 7);
            symptoms.put("painType", "sharp");
            symptoms.put("rashType", "small spots");
            symptoms.put("itchyRash", true);

            JSONObject painLocation = new JSONObject();
            painLocation.put("head", false);
            painLocation.put("eyes", true);
            painLocation.put("neck", true);
            painLocation.put("leftShoulder", false);
            painLocation.put("rightShoulder", true);
            painLocation.put("leftArm", true);
            painLocation.put("rightArm", false);
            painLocation.put("leftHand", true);
            painLocation.put("rightHand", true);
            painLocation.put("centerChest", false);
            painLocation.put("leftSide", true);
            painLocation.put("rightSide", false);
            painLocation.put("genitals", true);
            painLocation.put("leftHip", false);
            painLocation.put("rightHip", true);
            painLocation.put("leftKnee", true);
            painLocation.put("rightKnee", false);
            painLocation.put("leftFoot", true);
            painLocation.put("rightFoot", true);

            symptoms.put("painLocation",  painLocation);

            JSONObject rashFront = new JSONObject();
            rashFront.put("head", false);
            rashFront.put("eyes", true);
            rashFront.put("neck", true);
            rashFront.put("leftShoulder", false);
            rashFront.put("rightShoulder", true);
            rashFront.put("leftArm", true);
            rashFront.put("rightArm", false);
            rashFront.put("leftHand", true);
            rashFront.put("rightHand", true);
            rashFront.put("centerChest", false);
            rashFront.put("leftSide", true);
            rashFront.put("rightSide", false);
            rashFront.put("genitals", true);
            rashFront.put("leftHip", false);
            rashFront.put("rightHip", true);
            rashFront.put("leftKnee", true);
            rashFront.put("rightKnee", false);
            rashFront.put("leftFoot", true);
            rashFront.put("rightFoot", true);

            symptoms.put("rashLocationFront", rashFront);

            JSONObject rashBack = new JSONObject();
            rashBack.put("head", false);
            rashBack.put("eyes", true);
            rashBack.put("neck", true);
            rashBack.put("leftShoulder", false);
            rashBack.put("rightShoulder", true);
            rashBack.put("leftArm", true);
            rashBack.put("rightArm", false);
            rashBack.put("leftHand", true);
            rashBack.put("rightHand", true);
            rashBack.put("centerChest", false);
            rashBack.put("leftSide", true);
            rashBack.put("rightSide", false);
            rashBack.put("genitals", true);
            rashBack.put("leftHip", false);
            rashBack.put("rightHip", true);
            rashBack.put("leftKnee", true);
            rashBack.put("rightKnee", false);
            rashBack.put("leftFoot", true);
            rashBack.put("rightFoot", true);

            symptoms.put("rashLocationBack", rashBack);
            sample_input.put("symptoms", symptoms);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sample_input;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setup_location(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Location locationCurrent = getLastKnownLocation();
            latitude = locationCurrent.getLatitude();
            longitude = locationCurrent.getLongitude();
        }
    }


    @Override
    public void onClick(View v) {
        Intent con = new Intent(Review_patient_details.this, Fragment_main.class);
        Review_patient_details.this.startActivity(con);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is Required for this app, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
}