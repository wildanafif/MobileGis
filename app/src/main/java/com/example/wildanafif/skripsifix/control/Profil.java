package com.example.wildanafif.skripsifix.control;

import android.content.Context;

import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.MemberFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.fragment.ProfilFragment;

/**
 * Created by wildan afif on 5/13/2017.
 */

public class Profil {
    private Member member;
    ProfilFragment fragment;
    private Loading loading;
    private Context context;

    public Profil(ProfilFragment fragment) {
        this.fragment = fragment;
        this.loading=new Loading(this.fragment.getContext());
    }

    public Profil(Context context) {
        this.context = context;
    }





    public void update(Member member){
        MemberFirebase memberFirebase=new MemberFirebase(context);
        memberFirebase.update(member);
    }
}
