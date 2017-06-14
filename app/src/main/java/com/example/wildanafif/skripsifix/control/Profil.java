package com.example.wildanafif.skripsifix.control;

import android.app.ProgressDialog;
import android.view.Window;

import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebasae.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.fragment.ProfilFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wildan afif on 5/13/2017.
 */

public class Profil {
    private Member member;
    ProfilFragment fragment;
    private Loading loading;

    public Profil(ProfilFragment fragment) {
        this.fragment = fragment;
        this.loading=new Loading(this.fragment.getContext());
    }

    public Member lihat(){

        loading.show();

        AuthFirebase authFirebase=new AuthFirebase();
        FirebaseUser user=authFirebase.getUserLogin();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("members");
        Query query =  mDatabaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    member = userSnpashot.getValue(Member.class);
                }

                fragment.nama=member.getNama();
                fragment.setUI();
                loading.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        return member;

    }

    public void setTitle(){
        FirebaseUser firebaseUser= new AuthFirebase(this.fragment.getContext()).getUserLogin();
        DatabaseReference database_members= FirebaseDatabase.getInstance().getReference("members");
        database_members.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                fragment.setTitlebar(member.getNama());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
