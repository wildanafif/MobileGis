package com.example.wildanafif.skripsifix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class TestNotifikasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notifikasy);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
//        FirebaseInstanceId.getInstance().getToken();

        //String g=FirebaseInstanceId.getInstance().getToken();
       // Toast.makeText(this, ""+g, Toast.LENGTH_SHORT).show();
    }
}
