package com.example.wildanafif.skripsifix.entitas.firebase;

import android.content.Context;

import com.example.wildanafif.skripsifix.control.IklanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class IklanFirebase {
    private Context context;
    private IklanControl iklanControl;
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("iklan");

    public IklanFirebase(Context context, IklanControl iklanControl) {
        this.context = context;
        this.iklanControl=iklanControl;
    }

    public ArrayList<Iklan> getIklan(double latitude, double longitude){
        final Loading loading=new Loading(context);
        loading.show();
        GeoLocation geoLocation=new GeoLocation(latitude,longitude,context);
        geoLocation.convert();
        String provinsi=geoLocation.getProvinsi();
        final ArrayList<Iklan> daftar_iklan = new ArrayList<>();
        //Query query =  mDatabaseReference.orderByChild("provinsi").equalTo(provinsi);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    Iklan iklan = userSnpashot.getValue(Iklan.class);
                    if (iklan.getLatitude()!=0){
                        daftar_iklan.add(iklan);
                    }
                    System.out.println("Provinsi : "+iklan.getProvinsi());
                }

                loading.hide();

                iklanControl.showIklan(daftar_iklan);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        return daftar_iklan;
    }



    public void pasangiklan(Iklan iklan){
        this.mDatabaseReference.child(iklan.getId_iklan()).setValue(iklan);
    }


}
