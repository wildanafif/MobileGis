package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.firebasae.IklanFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.maps.Lokasi;
import com.example.wildanafif.skripsifix.entitas.maps.Maps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class IklanControl {
    private GoogleMap mMap;
    private Context context;
    private Lokasi lokasi;
    private Maps maps;

    public IklanControl(GoogleMap mMap, Context context) {
        this.mMap = mMap;
        this.context = context;
    }



    public void LihatDaftarIklan(){
        lokasi=new Lokasi(this.context);
        lokasi.cariLokasi();
        if (lokasi.isLocation()){
            maps=new Maps(this.mMap,this.context,lokasi.getLatitude(),lokasi.getLongitude());
            maps.setLokasi(2);
        }

        IklanFirebase iklanfirebase=new IklanFirebase(context,this);
        iklanfirebase.getIklan(lokasi.getLatitude(),lokasi.getLongitude());

    }

    public void tampilDaftarIklan(ArrayList<Iklan> daftar_iklan){
        maps.showMarker(daftar_iklan);
    }




}
