package com.example.trocaire_disease_outbreak_manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.util.List;


public class Sign_in extends Activity implements View.OnClickListener, LocationListener {

    private LocationManager locationManager;
    private Location locationCurrent;
    private double latitude, longitude;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        Button submit = findViewById(R.id.btn_login);
        submit.setOnClickListener(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            locationCurrent = getLastKnownLocation();
            latitude = locationCurrent.getLatitude();
            longitude = locationCurrent.getLongitude();
            String res = latitude + " " + longitude;
            Toast.makeText(this, res,
                    Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);

        String username_string = username.getText().toString();
        String password_string = password.getText().toString();

        if (username_string.isEmpty()) {
            username.setError("Enter Valid User ID");
            username.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            username.setBackgroundResource(R.drawable.rounded_edittext_box);
        }
        if (password_string.isEmpty()) {
            password.setError("Enter Valid Password");
            password.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            password.setBackgroundResource(R.drawable.rounded_edittext_box);
        }
        if (!username_string.isEmpty() && !password_string.isEmpty()) {
            Intent con = new Intent(Sign_in.this, Fragment_main.class);
            Sign_in.this.startActivity(con);
        }

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

