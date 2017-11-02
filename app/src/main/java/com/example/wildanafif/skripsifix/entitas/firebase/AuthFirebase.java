package com.example.wildanafif.skripsifix.entitas.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.wildanafif.skripsifix.control.Auth;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.wildanafif.skripsifix.entitas.request.Url_request._UPDATE_TOKEN;

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
                            saveUser();
                        }
                        loading.hide();

                        auth.redirect();
                    }
                });


    }

    private void saveUser(){
        FirebaseUser us=getUserLogin();
        String id =us.getUid();
        String g= FirebaseInstanceId.getInstance().getToken();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token",g)
                .add("email",us.getEmail())
                .build();

        Request request = new Request.Builder()
                .url(_UPDATE_TOKEN)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Member simpanMember=new Member(id,member.getNama(),member.getEmail(),member.getPassword(),member.getTelp(),member.getProvinsi(),member.getDaerah(),member.getInstagram(),member.getPin_bb(),member.getFoto(),member.getFacebook(),new Timestamp(System.currentTimeMillis()).getTime());

        database_members.child(id).setValue(simpanMember);
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

    public Member getMember(){
        FirebaseUser firebaseUser = getUserLogin();
        database_members.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member=dataSnapshot.getValue(Member.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return member;
    }



}
