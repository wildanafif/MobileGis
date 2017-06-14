package com.example.wildanafif.skripsifix.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
