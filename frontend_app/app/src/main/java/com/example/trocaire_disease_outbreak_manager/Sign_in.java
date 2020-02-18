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
import com.google.firebase.auth.GetTokenResult;

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
            mAuth.signInWithEmailAndPassword(fullUsername(userStr), passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            assert mAuth.getCurrentUser() != null;

                            mAuth.getCurrentUser().getIdToken(false)
                                .addOnCompleteListener(Sign_in.this, new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> tokenTask) {
                                        if (tokenTask.isSuccessful()) {
                                            assert tokenTask.getResult() != null;
                                            navigateToPatientDetails(tokenTask.getResult().getToken());
                                        } else {
                                            warnAndfailureToast("getIdToken:failure", tokenTask.getException());
                                        }
                                    }
                                });
                        } else {
                            warnAndfailureToast("signInWithEmail:failure", task.getException());
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

    private void navigateToPatientDetails(String token) {
        Intent con = new Intent(Sign_in.this, View_patient_details.class);
        con.putExtra("token", token);
        Sign_in.this.startActivity(con);
    }

    private String fullUsername(String id) {
        return String.format("%s@%s", id, getResources().getString(R.string.auth_email_extension));
    }

    private void warnAndfailureToast(String msg, Exception e) {
        Log.w("auth", msg, e);
        Toast.makeText(Sign_in.this, R.string.signin_failed, Toast.LENGTH_SHORT).show();
    }
}

