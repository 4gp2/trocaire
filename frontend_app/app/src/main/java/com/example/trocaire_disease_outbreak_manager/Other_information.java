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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Other_information extends AppCompatActivity implements View.OnClickListener {

    private JSONObject data, symptoms;
    double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_information);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setup_location();
        }

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            symptoms = new JSONObject(getIntent().getStringExtra("symptoms"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        String page_title = getString(R.string.Any_other_info_title);
        String html_title = "<font color='#0B85C8'>" + page_title + "</font>";
        bar.setTitle(Html.fromHtml(html_title));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button next = findViewById(R.id.buttonotherinfo);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText other_information = findViewById(R.id.editTextOtherInfo);
        String other_info = other_information.getText().toString();

        try {
            symptoms.put("otherInfo", other_info);
            data.put("symptoms", symptoms);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent con = new Intent(Other_information.this, Review_patient_details.class);
        con.putExtra("data", data.toString());
        con.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Other_information.this.startActivity(con);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setup_location(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        Location locationCurrent = getLastKnownLocation();
        try {
            latitude = locationCurrent.getLatitude();
            longitude = locationCurrent.getLongitude();
        }catch (Exception e){
            latitude = 0;
            longitude = 0;
        }
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

}