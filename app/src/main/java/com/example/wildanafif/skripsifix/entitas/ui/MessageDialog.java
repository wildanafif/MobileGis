package com.example.wildanafif.skripsifix.entitas.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;


/**
 * Created by wildan afif on 5/12/2017.
 */

public class MessageDialog {
    private Context context;
    private String message;

    public MessageDialog(Context context) {
        this.context = context;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void show(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
        alertDialog.setMessage(this.message);
        alertDialog.show();
    }
}
