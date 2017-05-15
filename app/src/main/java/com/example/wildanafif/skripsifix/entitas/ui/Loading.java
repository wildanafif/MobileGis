package com.example.wildanafif.skripsifix.entitas.ui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by wildan afif on 5/13/2017.
 */

public class Loading {
    private Context context;
    private ProgressDialog progress;

    public Loading(Context context) {
        this.context = context;

    }
    public void show(){
        progress = ProgressDialog.show(this.context , "Loading",
                "Please Wait ...", true);
        progress.setCancelable(false);
    }
    public void hide(){
        progress.dismiss();
    }
}
