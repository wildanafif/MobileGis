package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.btn_register=(Button)findViewById(R.id.btn_register);
        this.btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        this.email=(EditText)findViewById(R.id.email);
        this.password=(EditText)findViewById(R.id.password);
        this.btnLogin=(Button)findViewById(R.id.btn_login);
        setTitle("Login");
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth=new Auth(LoginActivity.this);

                auth.login(
                        email.getText().toString(),
                        password.getText().toString()
                );


            }
        });
    }
}
