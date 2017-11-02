package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.firebase.IklanFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.LocationService;
import com.example.wildanafif.skripsifix.entitas.maps.Maps;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.example.wildanafif.skripsifix.ui.activity.IklanSayactivity;
import com.example.wildanafif.skripsifix.ui.fragment.IklanFragment;
import com.google.android.gms.maps.GoogleMap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.wildanafif.skripsifix.entitas.request.Url_request._SAVE_IMAGE;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class IklanControl {
    private GoogleMap mMap;
    private Context context;
    private LocationService locationService;
    private Maps maps;
    private IklanFragment iklanFragment;
    private IklanFirebase iklanfirebase;
    private Iklan iklan;
    private Bitmap img;


    public IklanControl(Context context, Iklan iklan, Bitmap img) {
        this.context = context;
        this.iklan = iklan;
        this.img=img;
    }

    public IklanControl(IklanFragment iklanFragment, GoogleMap mMap, Context context) {
        this.iklanFragment=iklanFragment;
        this.mMap = mMap;
        this.context = context;
//        IntentFilter filter = new IntentFilter("com.tutorialspoint.CUSTOM_INTENT");
//        BroadcastReceiver smsBroadcastReceiver=new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //Toast.makeText(context, "asdad", Toast.LENGTH_SHORT).show();
//                double lat=intent.getDoubleExtra("latitude",0);
//                double longitude=intent.getDoubleExtra("lonitude",0);
//
//                Toast.makeText(context, lat+"", Toast.LENGTH_SHORT).show();
//            }
//        };
//        context.registerReceiver(smsBroadcastReceiver, filter);
    }

    public void showListIklan(double radius){
//        GPSTracker gps = new GPSTracker(context);
//        if(gps.canGetLocation()){
//
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            // \n is for new line
//            Toast.makeText(context, "Your Location is - \nLat: "
//                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        }else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
        locationService =new LocationService(this.context);
        locationService.search();
        if (locationService.isLocation()){
            maps=new Maps(this,this.iklanFragment,this.mMap,this.context, locationService.getLatitude(), locationService.getLongitude());
            maps.setLokasi(radius);
            iklanfirebase=new IklanFirebase(context,this);
            iklanfirebase.getIklan(locationService.getLatitude(), locationService.getLongitude());
        }else{
            MessageDialog messageDialog=new MessageDialog(this.context);
            messageDialog.setMessage("Lokasi tidak dapat ditemukan");
            messageDialog.show();
        }


    }

    public void filterDaftarIklan(double radius, double latitude, double longitude){

       // maps=new Maps(this,this.iklanFragment,this.mMap,this.context, latitude, longitude);
        maps.setLatitude(latitude);
        maps.setLongitude(longitude);
        maps.updateMaps(radius);
        maps.showUpdateMarker();

    }


    public void filterDaftarIklan(double radius,String kategori){
        maps.updateMaps(radius);
        maps.showUpdateMarker(kategori);
    }

    public void showIklan(ArrayList<Iklan> daftar_iklan){
        maps.showMarker(daftar_iklan);
    }


    public void pasang(){
        String message="";
        if (iklan.getJudul_iklan().matches("")){
            message="Judul iklan tidak boleh kosong";
        }else if (iklan.getKategori().matches("")){
            message="Kategori tidak boleh kosong";
        }else if (iklan.getLatitude()==0){
            message="Lokasi iklan tidak boleh kosong";
        }else if (iklan.getKondisi().matches("")){
            message="Kondisi iklan tidak boleh kosong";
        }else if (iklan.getHarga().matches("")){
            message="Harga tidak boleh kosong";
        }else if (iklan.getDeskripsi_iklan().matches("")){
            message="Deskripsi tidak boleh kosong";
        }

        if (!message.matches("")){
            MessageDialog messageDialog= new MessageDialog(context);
            messageDialog.setMessage(message);
            messageDialog.show();
        }else{
            if (!iklan.getTemp_foto().matches("")){
                ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                this.img.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
                final String sendImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                String url = _SAVE_IMAGE;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response

                                iklanfirebase=new IklanFirebase(context,IklanControl.this);
                                iklanfirebase.pasangiklan(iklan);
                                context.startActivity(new Intent(context,IklanSayactivity.class));
                                Toast.makeText(context, "Berhasil Memasang Iklan", Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("filename", iklan.getTemp_foto());
                        params.put("gambar", sendImage);

                        return params;
                    }
                };
                requestQueue.add(postRequest);
            }

        }

    }

    public void showDetailIklan(Iklan iklan){
        this.iklanFragment.hideBottomNavigasi();
        this.iklanFragment.showDetailIklan(iklan);
    }

}
