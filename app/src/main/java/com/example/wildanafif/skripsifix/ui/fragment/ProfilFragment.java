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

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;
import com.example.wildanafif.skripsifix.control.Profil;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebasae.AuthFirebase;
import com.example.wildanafif.skripsifix.ui.activity.DaftarKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.activity.IklanSayactivity;
import com.example.wildanafif.skripsifix.ui.activity.MainActivity;
import com.example.wildanafif.skripsifix.ui.activity.PasangIklanActivity;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Auth auth=new Auth(getContext());
        if (auth.cekLogin()){
            view=inflater.inflate(R.layout.fragment_profil, container, false);
            this.ET_nama =(TextView)view.findViewById(R.id.nama);
            Btnlogout=(LinearLayout)view.findViewById(R.id.logout);
            this.btn_daftar_ketemuan=(LinearLayout)view.findViewById(R.id.btn_daftar_ketemuan);
            this.btn_pasang_iklan=(Button)view.findViewById(R.id.btn_pasang_iklan_profil);
            this.btn_iklan_saya=(LinearLayout)view.findViewById(R.id.btn_iklan_saya);
            this.btn_daftar_ketemuan.setOnClickListener(this);
            this.btn_pasang_iklan.setOnClickListener(this);
            this.btn_iklan_saya.setOnClickListener(this);
            this.initClick();

            Profil profil=new Profil(this);
            profil.lihat();
            profil.setTitle();
        }




        return view;
    }
    public void setTitlebar(String titlebar){
        getActivity().setTitle(titlebar);


    }

    public void setUI(){
        this.ET_nama.setText(this.nama);
    }
    public void initClick(){
        this.Btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth=new Auth(getContext());
                auth.logout();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_daftar_ketemuan:
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

        }
    }
}
