package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_in extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

        String userStr = username.getText().toString();
        String passStr = password.getText().toString();

        if (userStr.isEmpty()) {
            username.setError("Enter Valid User ID");
            username.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            username.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (passStr.isEmpty()) {
            password.setError("Enter Valid Password");
            password.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            password.setBackgroundResource(R.drawable.rounded_edittext_box);
        }

        if (!userStr.isEmpty() && !passStr.isEmpty()) {
            Log.d("auth", fullUsername(userStr));
            mAuth
                .signInWithEmailAndPassword(fullUsername(userStr), passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            navigateToPatientDetails();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Sign_in.this, R.string.signin_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void navigateToPatientDetails() {
        Intent con = new Intent(Sign_in.this, View_patient_details.class);
        Sign_in.this.startActivity(con);
    }

    private String fullUsername(String id) {
        return String.format("%s@%s", id, getResources().getString(R.string.auth_email_extension));
    }
}

