package com.example.trocaire_disease_outbreak_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Sign_in extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        Button submit = findViewById(R.id.btn_login);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);
        EditText med_id = findViewById(R.id.et_med_id);

        String username_string = username.getText().toString();
        String password_string = password.getText().toString();
        String med_id_string = med_id.getText().toString();

        if (username_string.isEmpty()){
            username.setError("Enter Valid User ID");
            username.setBackgroundResource(R.drawable.rounded_edittext_error);
        }else{
            username.setBackgroundResource(R.drawable.rounded_edittext_box);
        }
        if (password_string.isEmpty()){
            password.setError("Enter Valid Password");
            password.setBackgroundResource(R.drawable.rounded_edittext_error);
        }else{
            password.setBackgroundResource(R.drawable.rounded_edittext_box);
        }
        if (med_id_string.isEmpty()){
            med_id.setError("Enter Valid Medical ID");
            med_id.setBackgroundResource(R.drawable.rounded_edittext_error);
        }else{
            med_id.setBackgroundResource(R.drawable.rounded_edittext_box);
        }
        if (!username_string.isEmpty() && !password_string.isEmpty() && !med_id_string.isEmpty()){

//          add code here to validate user account!
            Intent con = new Intent(Sign_in.this, Patient_details.class);
            Sign_in.this.startActivity(con);
        }

    }
}
