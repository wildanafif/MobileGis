package com.example.wildanafif.skripsifix.ui.activity;


import android.app.AlertDialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setTitle("Register");
        Button btn_register=(Button)findViewById(R.id.sign_up_button);
        final EditText et_nama=(EditText)findViewById(R.id.nama);
        final EditText et_email=(EditText)findViewById(R.id.email);
        final EditText et_password=(EditText)findViewById(R.id.password);
        final EditText et_ulangi_password=(EditText)findViewById(R.id.ulangi_password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth=new Auth(RegisterActivity.this);
                auth.register(
                        et_nama.getText().toString(),
                        et_email.getText().toString(),
                        et_password.getText().toString(),
                        et_ulangi_password.getText().toString()
                );


            }
        });

    }
}
