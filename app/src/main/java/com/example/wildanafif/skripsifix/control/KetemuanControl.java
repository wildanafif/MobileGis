package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.content.Intent;

import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.firebase.KetemuanFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuan;
import com.example.wildanafif.skripsifix.ui.activity.KofirmasiLokasiKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.activity.PilihLokasiKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.activity.RuteKetemuanActivity;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wildan afif on 5/21/2017.
 */

public class KetemuanControl {
    private Context context;

    public KetemuanControl(Context context) {
        this.context = context;
    }

    public void ajakKetemuan(Ketemuan ketemuan){
        if (ketemuan.getWaktu().matches("")){
            MessageDialog messageDialog= new MessageDialog(this.context);
            messageDialog.setMessage("Anda harus mengatur waktu ketemuan");
            messageDialog.show();
        }else{

            Intent inten_pilih_lokasi= new Intent(context,PilihLokasiKetemuanActivity.class);
            inten_pilih_lokasi.putExtra("ketemuan",ketemuan);
            context.startActivity(inten_pilih_lokasi);

        }
    }

    public void simpanKetemuan(Ketemuan ketemuan,HashMap<Marker, LokasiKetemuan> mHashMapMarker){
        KetemuanFirebase ketemuanFirebase= new KetemuanFirebase(this.context);
        ketemuanFirebase.simpan(ketemuan,mHashMapMarker);
    }

    public void konfirmasi_ketemuan(LokasiKetemuan lokasiKetemuan, Ketemuan ketemuan){
        KetemuanFirebase ketemuanFirebase= new KetemuanFirebase(this.context);
        ketemuanFirebase.simpan_konfirmasi_lokasi_ketemuan(lokasiKetemuan);


        String g= FirebaseInstanceId.getInstance().getToken();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email_sender",ketemuan.getEmail_sender())
                .add("id_ketemuan",ketemuan.getId_ketemuan())
                .add("email_receiver",ketemuan.getEmail_receiver())
                .add("alamat",lokasiKetemuan.getId_lokasi_ketrmuan())

                .build();

        Request request = new Request.Builder()
                .url("http://developper.otomotifstore.com/notifikasi/konfirmasi_permintaan?key=1234")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            ketemuanFirebase.showDetailKetemuan(ketemuan.getId_ketemuan());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void detailKetemuan(Ketemuan ketemuan){
        Intent intent= new Intent(this.context, DetailActivityKetemuan.class);
        intent.putExtra("ketemuan",ketemuan);
        intent.putExtra("konfirmasi",false);
        this.context.startActivity(intent);
    }

    public void konfirmasiLokasi(Ketemuan ketemuan){

        Intent i_pilih_lokasi= new Intent(this.context,KofirmasiLokasiKetemuanActivity.class);
        i_pilih_lokasi.putExtra("ketemuan",ketemuan);
        context.startActivity(i_pilih_lokasi);
    }
    public void lihatRuteLokasi(Ketemuan ketemuan){

        Intent i= new Intent(context, RuteKetemuanActivity.class);
        i.putExtra("ketemuan",ketemuan);
        context.startActivity(i);
    }

}
