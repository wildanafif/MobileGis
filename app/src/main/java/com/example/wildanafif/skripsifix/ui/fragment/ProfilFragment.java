package com.example.wildanafif.skripsifix.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Auth;
import com.example.wildanafif.skripsifix.control.Profil;
import com.example.wildanafif.skripsifix.entitas.Member;

/**
 * Created by wildan afif on 5/13/2017.
 */

public class ProfilFragment extends Fragment {
    private View view;
    private TextView ET_nama;
    public String nama;
    private LinearLayout Btnlogout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Auth auth=new Auth(getContext());
        if (auth.cekLogin()){
            view=inflater.inflate(R.layout.fragment_profil, container, false);
            this.ET_nama =(TextView)view.findViewById(R.id.nama);
            Btnlogout=(LinearLayout)view.findViewById(R.id.logout);
            this.initClick();
            Profil profil=new Profil(this);
            profil.lihat();
        }




        return view;
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
}
