package com.example.wildanafif.skripsifix.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.entitas.volley.CircularNetworkImageView;
import com.example.wildanafif.skripsifix.entitas.volley.CustomVolleyRequestQueue;
import com.example.wildanafif.skripsifix.ui.activity.DaftarKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.activity.IklanSayactivity;
import com.example.wildanafif.skripsifix.ui.activity.PasangIklanActivity;
import com.example.wildanafif.skripsifix.ui.activity.SettingProfileActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wildan afif on 5/13/2017.
 */

public class ProfilFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView ET_nama;
    public String nama;
    private LinearLayout Btnlogout;
    private LinearLayout btn_daftar_ketemuan;
    private Button btn_pasang_iklan;
    private LinearLayout btn_iklan_saya;
    private LinearLayout pengaturan;
    private FirebaseUser firebaseUser;
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("members");
    private Member member;
    private CircularNetworkImageView gambar_profil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Auth auth=new Auth(getContext());
        if (auth.cekLogin()){
            view=inflater.inflate(R.layout.fragment_profil, container, false);
            this.ET_nama =(TextView)view.findViewById(R.id.nama);
            Btnlogout=(LinearLayout)view.findViewById(R.id.logout);
            this.pengaturan=(LinearLayout)view.findViewById(R.id.pengaturan);
            this.btn_daftar_ketemuan=(LinearLayout)view.findViewById(R.id.btn_daftar_ketemuan);
            this.btn_pasang_iklan=(Button)view.findViewById(R.id.btn_pasang_iklan_profil);
            this.btn_iklan_saya=(LinearLayout)view.findViewById(R.id.btn_iklan_saya);
            this.btn_daftar_ketemuan.setOnClickListener(this);
            this.btn_pasang_iklan.setOnClickListener(this);
            this.btn_iklan_saya.setOnClickListener(this);
            this.pengaturan.setOnClickListener(this);
            gambar_profil = (CircularNetworkImageView) view.findViewById(R.id.gambar_profile);
            this.initClick();

            getUser();

        }




        return view;
    }



    public void initClick(){
        this.Btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth=new Auth(getContext());
                auth.logout();
                getActivity().finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_daftar_ketemuan:
                getActivity().finish();
                Intent i =new Intent(getContext(), DaftarKetemuanActivity.class);
                startActivity(i);
                break;
            case R.id.btn_pasang_iklan_profil:
                Intent intent= new Intent(getContext(), PasangIklanActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_iklan_saya:
                Intent intent1= new Intent(getContext(),IklanSayactivity.class);
                startActivity(intent1);
                break;
            case R.id.pengaturan:
                startActivity(new Intent(getContext(), SettingProfileActivity.class));
                break;

        }
    }

    private void getUser(){
        final Loading loading= new Loading(getContext());
        loading.show();
        AuthFirebase authFirebase=new AuthFirebase();
        firebaseUser=authFirebase.getUserLogin();
        final ImageLoader netImageLoader= CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        mDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member=dataSnapshot.getValue(Member.class);
                getActivity().setTitle(member.getNama());
                ET_nama.setText(member.getNama());
                if (!member.getFoto().matches("")){
                    gambar_profil.setImageResource(R.drawable.loading);
                    gambar_profil.setImageUrl(member.getFoto(), netImageLoader);
                }else {
                    gambar_profil.setImageResource(R.drawable.ic_account_circle_black_48dp);
                }
                loading.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
