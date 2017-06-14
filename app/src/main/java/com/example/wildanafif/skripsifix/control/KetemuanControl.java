package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.firebasae.KetemuanFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wildan afif on 5/21/2017.
 */

public class KetemuanControl {
    private Context context;

    public KetemuanControl(Context context) {
        this.context = context;
    }

    public void ajakKetemuan(String email_sender, String email_receiver, String waktu, String tempat_ketemuan, double latitude, double longitude, String message, Iklan iklan){
        String message_error="";
        if (email_sender.matches("")){
            message_error="Email pengirim tidak boleh kosong";
        }else if (email_receiver.matches("")){
            message_error="Email penerima tidak boleh kosong";
        }else if (waktu.matches("")){
            message_error="Tanggal dan waktu tidak boleh kosong";
        }else if (tempat_ketemuan.matches("")){
            message_error="Tempat ketemuan tidak boleh kosong";
        }else if (latitude==0){
            message_error="Latitude tidak boleh kosong";
        }else if (longitude==0){
            message_error="Longitude tidak boleh kosong";
        }else if (message.matches("")){
            message_error="Anda harus mengatakan sesuatu pada penjual";
        }else if (iklan==null){
            message_error="Sepertinya ada masalah";
        }

        if (!message_error.matches("")){
            MessageDialog messageDialog=new MessageDialog(this.context);
            messageDialog.setMessage(message_error);
            messageDialog.show();
        }else{
            Ketemuan ketemuan= new Ketemuan(
                    email_sender,
                    email_receiver,
                    waktu,
                    tempat_ketemuan,
                    latitude,
                    longitude,
                    message,
                    iklan,
                    false
            );
            KetemuanFirebase ketemuanFirebase= new KetemuanFirebase(this.context);
            ketemuanFirebase.simpan(ketemuan);
        }

    }

    public void simpanKetemuan(Ketemuan ketemuan,HashMap<Marker, LokasiKetemuan> mHashMapMarker){
        Iterator it = mHashMapMarker.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            LokasiKetemuan lokasiKetemuan= (LokasiKetemuan) pair.getValue();
            Toast.makeText(context, ""+lokasiKetemuan.getAlamat(), Toast.LENGTH_SHORT).show();

        }
        KetemuanFirebase ketemuanFirebase= new KetemuanFirebase(this.context);
        ketemuanFirebase.simpan(ketemuan,mHashMapMarker);
    }
}
