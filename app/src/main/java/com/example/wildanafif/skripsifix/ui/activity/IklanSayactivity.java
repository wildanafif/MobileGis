package com.example.wildanafif.skripsifix.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.firebasae.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.adapter.ListAdapterIklan;
import com.example.wildanafif.skripsifix.ui.adapter.ListViewAdapterDaftarKetemuan;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IklanSayactivity extends AppCompatActivity {

    private ListView list_iklan;
    private ArrayList<Iklan> daftar_iklan;
    private AuthFirebase authFirebase;
    private FirebaseUser firebaseUser;
    private ListAdapterIklan adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan_saya);
        this.list_iklan=(ListView) findViewById(R.id.list_iklan);
        this.authFirebase= new AuthFirebase();
        firebaseUser= authFirebase.getUserLogin();
        setTitle("Iklan Saya");
        this.daftarIklan();

    }


    private void daftarIklan(){
        final Loading loading= new Loading(this);
        loading.show();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("iklan");
        Query query =  mDatabaseReference.orderByChild("mail").equalTo(firebaseUser.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftar_iklan = new ArrayList<>();
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    Iklan iklan = userSnpashot.getValue(Iklan.class);
                    if (iklan.getLatitude()!=0){
                        daftar_iklan.add(iklan);
                    }

                }
                adapter = new ListAdapterIklan(IklanSayactivity.this, daftar_iklan);
                list_iklan.setAdapter(adapter);

                loading.hide();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
    }

}
