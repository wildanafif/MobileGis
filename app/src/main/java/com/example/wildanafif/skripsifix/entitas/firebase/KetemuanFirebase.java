package com.example.wildanafif.skripsifix.entitas.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.activity.DaftarKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuanDiterima;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wildan afif on 5/21/2017.
 */

public class KetemuanFirebase {
    DatabaseReference database_daftar_ketemuan = FirebaseDatabase.getInstance().getReference("ketemuan");
    DatabaseReference lokasi_daftar_ketemuan = FirebaseDatabase.getInstance().getReference("lokasi_ketemuan");
    private Context context;

    public KetemuanFirebase(Context context) {
        this.context = context;
    }



    public void simpan(Ketemuan ketemuan, HashMap<Marker, LokasiKetemuan> mHashMapMarker){
        Loading loading= new Loading(this.context);
        loading.show();
        String IdKetemuan =database_daftar_ketemuan.push().getKey();
        ketemuan.setId_ketemuan(IdKetemuan);
        Ketemuan k_t=new Ketemuan(ketemuan.getNama_pengirim(),ketemuan.getTimestamp_ketemuan(),IdKetemuan,ketemuan.getEmail_sender(),ketemuan.getEmail_receiver(),ketemuan.getWaktu(),ketemuan.getTempat_ketemuan(),ketemuan.getLatitude(),ketemuan.getLongitude(),ketemuan.getMessage(),ketemuan.getIklan(),false,0,0,0,0,false,false);
        database_daftar_ketemuan.child(IdKetemuan).setValue(k_t);
        Iterator it = mHashMapMarker.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            LokasiKetemuan lokasiKetemuan= (LokasiKetemuan) pair.getValue();
            String id_lokasi_ketemuan =lokasi_daftar_ketemuan.push().getKey();
            LokasiKetemuan data_baru=new LokasiKetemuan(id_lokasi_ketemuan,lokasiKetemuan.getLatitude(),lokasiKetemuan.getLongitude(),lokasiKetemuan.getAlamat(),IdKetemuan);
            lokasi_daftar_ketemuan.child(id_lokasi_ketemuan).setValue(data_baru);
            //save_lokasi.put(this.getSaltString(),new LokasiKetemuan(lokasiKetemuan.getLatitude(),lokasiKetemuan.getLongitude(),lokasiKetemuan.getAlamat(),IdKetemuan));
        }
        //this.lokasi_daftar_ketemuan.setValue(save_lokasi);



        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        String url = "http://developper.otomotifstore.com/notifikasi/kirim_permintaan_ketemuan?key=1234&email_tujuan="+k_t.getEmail_receiver()+"&nama_sender="+k_t.getNama_pengirim()+"&id_ketemuan="+k_t.getId_ketemuan();
        url=url.replace(" ","%20");
        Log.d("simpan", "simpan: "+url);
        //Toast.makeText(context, ""+url, Toast.LENGTH_LONG).show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET,url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               //Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG", error.toString());

                //Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);



        loading.hide();
        Intent intent=new Intent(this.context, DaftarKetemuanActivity.class);
        intent.putExtra("back","profil");
        this.context.startActivity(intent);


    }


    public void ketemuan(Iklan iklan){
        this.database_daftar_ketemuan.child(iklan.getId_iklan()).setValue(iklan);
    }

    public void konfirmasi(Ketemuan ketemuan){
        database_daftar_ketemuan.child(ketemuan.getId_ketemuan()).child("confirmReceiver").setValue(ketemuan.isConfirmReceiver());
        Toast.makeText(context, "Berhasil di Konfirmasi", Toast.LENGTH_LONG).show();
    }

    public void simpan_konfirmasi_lokasi_ketemuan(LokasiKetemuan lokasiKetemuan){
        database_daftar_ketemuan.child(lokasiKetemuan.getId_ketemuan()).child("latitude").setValue(lokasiKetemuan.getLatitude());
        database_daftar_ketemuan.child(lokasiKetemuan.getId_ketemuan()).child("longitude").setValue(lokasiKetemuan.getLongitude());
        database_daftar_ketemuan.child(lokasiKetemuan.getId_ketemuan()).child("tempat_ketemuan").setValue(lokasiKetemuan.getAlamat());
        database_daftar_ketemuan.child(lokasiKetemuan.getId_ketemuan()).child("confirmReceiver").setValue(true);
    }

    public void showDetailKetemuan(String id_ketemuan){
        database_daftar_ketemuan.child(id_ketemuan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Ketemuan ketemuan=dataSnapshot.getValue(Ketemuan.class);
                Intent intent= new Intent(context, DetailActivityKetemuanDiterima.class);
                intent.putExtra("ketemuan", ketemuan);
                intent.putExtra("konfirmasi",true);
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
