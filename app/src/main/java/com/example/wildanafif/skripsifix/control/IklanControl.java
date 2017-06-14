package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.firebasae.IklanFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.maps.Lokasi;
import com.example.wildanafif.skripsifix.entitas.maps.Maps;
import com.example.wildanafif.skripsifix.ui.activity.PasangIklanActivity;
import com.example.wildanafif.skripsifix.ui.fragment.IklanFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class IklanControl {
    private GoogleMap mMap;
    private Context context;
    private Lokasi lokasi;
    private Maps maps;
    private IklanFragment iklanFragment;
    private IklanFirebase iklanfirebase;
    private Iklan iklan;

    public IklanControl(Context context, Iklan iklan) {
        this.context = context;
        this.iklan = iklan;
    }

    public IklanControl(IklanFragment iklanFragment, GoogleMap mMap, Context context) {
        this.iklanFragment=iklanFragment;
        this.mMap = mMap;
        this.context = context;
    }



    public void LihatDaftarIklan(double radius){
        lokasi=new Lokasi(this.context);
        lokasi.cariLokasi();
        if (lokasi.isLocation()){
            maps=new Maps(this.iklanFragment,this.mMap,this.context,lokasi.getLatitude(),lokasi.getLongitude());
            maps.setLokasi(radius);
        }

        iklanfirebase=new IklanFirebase(context,this);
        iklanfirebase.getIklan(lokasi.getLatitude(),lokasi.getLongitude());

    }
    public void filterDaftarIklan(double radius){
        maps.updateMaps(radius);
        maps.showUpdateMarker();


//        IklanFirebase iklanfirebase=new IklanFirebase(context,this);
//        iklanfirebase.getIklan(lokasi.getLatitude(),lokasi.getLongitude());

    }

    public void tampilDaftarIklan(ArrayList<Iklan> daftar_iklan){
        maps.showMarker(daftar_iklan);
    }


    public void pasang(){
        iklanfirebase=new IklanFirebase(context,this);
        iklanfirebase.pasangiklan(this.iklan);
    }

    public void listIklanProfil(){

    }

}
