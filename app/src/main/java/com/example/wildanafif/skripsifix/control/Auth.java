package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebasae.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.example.wildanafif.skripsifix.ui.activity.LoginActivity;
import com.example.wildanafif.skripsifix.ui.activity.MainActivity;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by wildan afif on 5/12/2017.
 */

public class Auth {
    private Context context;


    public Auth(Context context) {
        this.context = context;
    }

    public void register(String nama, String email, String password, String ulangi_password){
        String message="";
        if (nama.matches("")){
            message="Nama tidak boleh kosong";
        }else if (email.matches("")) {
            message = "Email tidak boleh kosong";
        }else if (password.matches("")){
            message="Password tidak boleh kosong";
        }else if (!password.equals(ulangi_password)){
            message="Password tidak sama";
        }
        if (!message.matches("")){
            MessageDialog messageDialog=new MessageDialog(this.context);
            messageDialog.setMessage(message);
            messageDialog.show();

        }else{
            Member member=new Member(nama,email,password,"","","","","","","");

            AuthFirebase authFirebase=new AuthFirebase(this.context,this);
            authFirebase.register(member);

        }


    }

    public void login(String email,String password){
        String message="";
        if (email.matches("")){
            message="Email tidak boleh kosong";
        }else if (password.matches("")){
            message="Password tidak boleh kosong";
        }

        if (!message.matches("")){
            MessageDialog dialog= new MessageDialog(this.context);
            dialog.setMessage(message);
            dialog.show();
        }
        AuthFirebase authFirebase=new AuthFirebase(this.context,this);
        authFirebase.login(email, password);
    }

    public void loginFailed(){
        MessageDialog messageDialog=new MessageDialog(this.context);
        messageDialog.setMessage("Email atau Password salah");
        messageDialog.show();
    }

    public void logout(){
        AuthFirebase authFirebase=new AuthFirebase(this.context);
        authFirebase.logout();
        this.context.startActivity(new Intent(this.context, LoginActivity.class));
    }

    public boolean cekLogin(){
        AuthFirebase authFirebase=new AuthFirebase();
        boolean login=authFirebase.isLogin();
        Intent i;
        if (!login){
            i=new Intent(this.context,LoginActivity.class);
            this.context.startActivity(i);
        }
        return login ;
    }



    public void redirect(){
        Intent intent= new Intent(context, MainActivity.class);
        intent.putExtra("fragment","profil");
        context.startActivity(intent);
    }



}
