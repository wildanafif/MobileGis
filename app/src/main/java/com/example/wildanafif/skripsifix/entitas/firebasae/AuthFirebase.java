package com.example.wildanafif.skripsifix.entitas.firebasae;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.wildanafif.skripsifix.control.Auth;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by wildan afif on 5/12/2017.
 */

public class AuthFirebase {
    private Context context;
    private Member member;
    private Auth auth;
    private static final DatabaseReference database_members= FirebaseDatabase.getInstance().getReference("members");
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    public AuthFirebase() {
    }

    public AuthFirebase(Context context) {
        this.context = context;
    }

    public AuthFirebase(Context context, Auth auth) {
        this.context = context;
        this.auth = auth;
    }

    public void login(String email, String password){
        final Loading loading=new Loading(this.context);
        loading.show();
        this.firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loading.hide();
                            auth.redirect();
                        }else{
                            loading.hide();
                            auth.loginFailed();
                        }
                    }
                });
    }

    public void register(Member m_member){
        final Loading loading= new Loading(this.context);
        loading.show();
        final boolean[] status = {false};
        this.member=m_member;
        this.firebaseAuth.createUserWithEmailAndPassword(member.getEmail(),member.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            firebaseAuth=FirebaseAuth.getInstance();
                            String id =database_members.push().getKey();
                            database_members.child(id).setValue(member);
                        }
                        loading.hide();

                        auth.redirect();
                    }
                });


    }

    public FirebaseUser getUserLogin(){
        FirebaseUser user_f = firebaseAuth.getCurrentUser();
        return user_f;
    };

    public void logout(){
        firebaseAuth.signOut();
    }

    public boolean isLogin(){

        if(firebaseAuth.getCurrentUser() == null){

            return false;
        }else{
            return true;
        }
    }



}
