package com.example.wildanafif.skripsifix.entitas.firebasae;

import android.content.Context;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.control.KetemuanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuan;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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

    public void simpan(Ketemuan ketemuan){
        Loading loading= new Loading(this.context);
        loading.show();
        String IdKetemuan =database_daftar_ketemuan.push().getKey();
        ketemuan.setId_ketemuan(IdKetemuan);
        Ketemuan k_t=new Ketemuan(IdKetemuan,ketemuan.getEmail_sender(),ketemuan.getEmail_receiver(),ketemuan.getWaktu(),ketemuan.getTempat_ketemuan(),ketemuan.getLatitude(),ketemuan.getLongitude(),ketemuan.getMessage(),ketemuan.getIklan(),false);
        database_daftar_ketemuan.child(IdKetemuan).setValue(k_t);
        loading.hide();

    }

    public void simpan(Ketemuan ketemuan, HashMap<Marker, LokasiKetemuan> mHashMapMarker){
        Loading loading= new Loading(this.context);
        loading.show();
        String IdKetemuan =database_daftar_ketemuan.push().getKey();
        ketemuan.setId_ketemuan(IdKetemuan);
        Ketemuan k_t=new Ketemuan(IdKetemuan,ketemuan.getEmail_sender(),ketemuan.getEmail_receiver(),ketemuan.getWaktu(),ketemuan.getTempat_ketemuan(),ketemuan.getLatitude(),ketemuan.getLongitude(),ketemuan.getMessage(),ketemuan.getIklan(),false);
        database_daftar_ketemuan.child(IdKetemuan).setValue(k_t);


        Iterator it = mHashMapMarker.entrySet().iterator();
        HashMap<String, LokasiKetemuan> save_lokasi =  new HashMap<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            LokasiKetemuan lokasiKetemuan= (LokasiKetemuan) pair.getValue();
            LokasiKetemuan data_baru=new LokasiKetemuan(lokasiKetemuan.getLatitude(),lokasiKetemuan.getLongitude(),lokasiKetemuan.getAlamat(),IdKetemuan);
            String id_lokasi_ketemuan =lokasi_daftar_ketemuan.push().getKey();
            lokasi_daftar_ketemuan.child(id_lokasi_ketemuan).setValue(data_baru);
            //save_lokasi.put(this.getSaltString(),new LokasiKetemuan(lokasiKetemuan.getLatitude(),lokasiKetemuan.getLongitude(),lokasiKetemuan.getAlamat(),IdKetemuan));
        }
        //this.lokasi_daftar_ketemuan.setValue(save_lokasi);
        loading.hide();

    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public void ketemuan(Iklan iklan){
        this.database_daftar_ketemuan.child(iklan.getId_iklan()).setValue(iklan);
    }

    public void konfirmasi(Ketemuan ketemuan){
        database_daftar_ketemuan.child(ketemuan.getId_ketemuan()).child("confirmReceiver").setValue(ketemuan.isConfirmReceiver());
        Toast.makeText(context, "Berhasil di Konfirmasi", Toast.LENGTH_LONG).show();
    }
}
