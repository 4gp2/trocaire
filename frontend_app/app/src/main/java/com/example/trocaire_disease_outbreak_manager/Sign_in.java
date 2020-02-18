package com.example.trocaire_disease_outbreak_manager;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;



public class Sign_in extends AppCompatActivity implements View.OnClickListener {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setTitle(Html.fromHtml("<font color='#0B85C8'>Sign in</font>"));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Button submit = findViewById(R.id.btn_login);
        submit.setOnClickListener(this);
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
            Intent con = new Intent(Sign_in.this, View_patient_details.class);
            Sign_in.this.startActivity(con);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

