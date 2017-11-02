package com.example.wildanafif.skripsifix.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.InfoMessage;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.volley.CircularNetworkImageView;
import com.example.wildanafif.skripsifix.entitas.volley.CustomVolleyRequestQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wildan afif on 7/10/2017.
 */

public class ListChat extends BaseAdapter {
    Context mContext;
    private List<InfoMessage> chat;

    public ListChat(Context mContext, List<InfoMessage> chat) {
        this.mContext = mContext;
        this.chat = chat;
     }

    @Override
    public int getCount() {
        return this.chat.size();
    }

    @Override
    public Object getItem(int position) {
        return this.chat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_chat, null);
        final TextView nama_pengirim=(TextView)v.findViewById(R.id.nama_pengirim);
        TextView isi_pesan=(TextView)v.findViewById(R.id.isi_pesan);
        TextView waktu=(TextView)v.findViewById(R.id.waktu);
        Timestamp timestamp = new Timestamp(chat.get(position).getWaktu());
        String date = new Date(timestamp.getTime()).toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(System.currentTimeMillis()).getTime());

        String cetak_waktu;
        if (date.matches(timeStamp)){
            cetak_waktu = new SimpleDateFormat("HH:mm").format(new Timestamp(chat.get(position).getWaktu()));

        }else{
            cetak_waktu=date;
        }
        waktu.setText(cetak_waktu);
        final CircularNetworkImageView gambar_profil = (CircularNetworkImageView) v.findViewById(R.id.gambar_profile_chat);
        final ImageLoader netImageLoader= CustomVolleyRequestQueue.getInstance(this.mContext).getImageLoader();

        DatabaseReference database_member = FirebaseDatabase.getInstance().getReference("members");

        database_member.child(chat.get(position).getId_sender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                nama_pengirim.setText(member.getNama());
                gambar_profil.setImageUrl(member.getFoto(), netImageLoader);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        String pecahPesan=this.chat.get(position).getTemp_chat();
        if (pecahPesan.length()<23){
            isi_pesan.setText(pecahPesan);
        }else{
            isi_pesan.setText(pecahPesan.substring(0,22)+"...");
        }

        return v;
    }
}
