package com.example.wildanafif.skripsifix.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RadioGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.IklanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;


public class PasangIklanActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] kategori = {"Mobil","Motor"};
    private String[][] subkategori = new String[][]{
            {"Mobil Bekas","Aksesoris Mobil","Audio Mobil","Velg dan Ban Mobil","Sparepart Mobil"},
            {"Motor Bekas","Aksesoris Motor","Sparepart Motor","Apparel","Helm"}

    };
    private String kondisi []={"Baru", "Bekas"};
    private int pilihan_kategori=-1;
    private int pilihan_subkategori;
    private int pilihan_kondisi;
    private double latitude_iklan;
    private double longitude_iklan;
    private Member member;

    private EditText ET_kategori_iklan;
    private EditText ET_kondisi_iklkan;
    private ImageView btn_img_upload;
    private Dialog dialog;
    private static final int RESULT_LOAD_CAMERA=0;
    private static final int RESULT_LOAD_IMAGE=1;
    private final static int PLACE_PICKER_REQUEST = 2;

    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private EditText ET_lokasi_iklan;
    private Button btn_pasang_iklan;
    private EditText ET_judul_iklan;
    private EditText ET_deskripsi_iklan;
    private EditText ET_harga_iklan;
    private String provinsi;
    private String daerah;

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("members");
    private FirebaseUser firebaseUser;
    private RadioGroup radio_group_nego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasang_iklan);
        this.getUser();
        this.ET_kategori_iklan=(EditText)findViewById(R.id.kategori_iklan);
        this.ET_kondisi_iklkan=(EditText)findViewById(R.id.kondisi_iklan);
        this.btn_img_upload=(ImageView)findViewById(R.id.img_upload);
        this.ET_lokasi_iklan=(EditText)findViewById(R.id.lokasi_iklan);
        this.btn_pasang_iklan=(Button)findViewById(R.id.btn_pasang_iklan);
        this.ET_judul_iklan=(EditText)findViewById(R.id.judul_iklan);
        this.ET_deskripsi_iklan=(EditText)findViewById(R.id.deskripsi_iklan);
        this.ET_harga_iklan=(EditText)findViewById(R.id.harga_iklan);
        this.radio_group_nego = (RadioGroup) findViewById(R.id.nego_tidak);
        setTitle("Pasang Iklan");
        this.ET_kondisi_iklkan.setOnClickListener(this);
        this.ET_kategori_iklan.setOnClickListener(this);
        this.btn_img_upload.setOnClickListener(this);
        this.ET_lokasi_iklan.setOnClickListener(this);
        this.btn_pasang_iklan.setOnClickListener(this);
        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kategori_iklan:
                final Dialog dialog = new Dialog(PasangIklanActivity.this);
                dialog.setContentView(R.layout.pilihan_kategori);
                dialog.setTitle("Kategori");
                ArrayAdapter adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, kategori);
                ListView listView = (ListView) dialog.findViewById(R.id.list_kategori);
                listView.setAdapter(adapter);
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pilihan_kategori=position;
                        dialog.dismiss();
                        showSubKategori();
                    }
                });
                break;
            case R.id.kondisi_iklan:
                this.showKondisi();
                break;
            case R.id.img_upload:
                showDialogbrowse();
                break;
            case R.id.camera:
                this.dialog.dismiss();
                openCamera();
                break;
            case R.id.galery:
                this.dialog.dismiss();
                openGalery();
                break;
            case R.id.lokasi_iklan:
                cariLokasi();
                break;
            case R.id.btn_pasang_iklan:
                pasangIklan();
                break;

        }
    }

    private void pasangIklan() {
        Bitmap image=((BitmapDrawable) this.btn_img_upload.getDrawable()).getBitmap();
        String nama_foto;
        nama_foto="uploads/--"+member.getId()+new Timestamp(System.currentTimeMillis()).getTime()+".jpg";
        int barang_nego=0;
        int selectedId = this.radio_group_nego.getCheckedRadioButtonId();

        if (selectedId==R.id.nego){
            barang_nego=1;
        }else {
            barang_nego=0;
        }
        String temp_kategori;
        String temp_subKategori;
        if (pilihan_kategori==-1){
            temp_kategori="";
            temp_subKategori="";
        }else{
            temp_kategori= this.kategori[pilihan_kategori];
            temp_subKategori=this.subkategori[pilihan_kategori][pilihan_subkategori];
        }
        Iklan iklan= new Iklan(
                String.valueOf(new Date().getTime()),
                this.latitude_iklan,
                this.longitude_iklan,
                this.ET_judul_iklan.getText().toString(),
                temp_kategori,
                temp_subKategori,
                this.ET_deskripsi_iklan.getText().toString(),
                this.ET_harga_iklan.getText().toString(),
                this.provinsi,
                member.getNama(),
                member.getTelp(),
                member.getPin_bb(),
                member.getFacebook(),
                member.getInstagram(),
                String.valueOf(new Date().getTime()),
                barang_nego,
                this.daerah,
                nama_foto,
                member.getEmail(),
                1,
                this.firebaseUser.getUid(),
                this.ET_kondisi_iklkan.getText().toString(),
                0,
                1,
                String.valueOf(new Date().getTime())
        );
        IklanControl iklanControl= new IklanControl(this,iklan, image);
        iklanControl.pasang();


    }




    private void cariLokasi() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = builder.build(PasangIklanActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void openGalery() {
        Intent galeri=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeri,RESULT_LOAD_IMAGE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,RESULT_LOAD_CAMERA);
    }

    private void showDialogbrowse() {

        dialog = new Dialog(PasangIklanActivity.this);
        dialog.setContentView(R.layout.pilihan_file);
        dialog.setTitle("Pilih Sumber Gambar");
        LinearLayout camera= (LinearLayout)dialog.findViewById(R.id.camera);
        LinearLayout galery=(LinearLayout)dialog.findViewById(R.id.galery);
        camera.setOnClickListener(this);
        galery.setOnClickListener(this);
        dialog.show();

    }

    void showSubKategori(){
        final Dialog dialog = new Dialog(PasangIklanActivity.this);
        dialog.setContentView(R.layout.pilihan_kategori);
        dialog.setTitle("Sub Kategori "+kategori[pilihan_kategori]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, subkategori[pilihan_kategori]);
        ListView listView = (ListView) dialog.findViewById(R.id.list_kategori);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pilihan_subkategori=position;
                ET_kategori_iklan.setText(kategori[pilihan_kategori]+" - "+subkategori[pilihan_kategori][pilihan_subkategori]);
                dialog.dismiss();
            }
        });


    }
    private void showKondisi(){
        final Dialog dialog = new Dialog(PasangIklanActivity.this);
        dialog.setContentView(R.layout.pilihan_kategori);
        dialog.setTitle("Kondisi");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.kondisi);
        ListView listView = (ListView) dialog.findViewById(R.id.list_kategori);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pilihan_kondisi=position;
                ET_kondisi_iklkan.setText(kondisi[pilihan_kondisi]);
                dialog.dismiss();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_CAMERA && data!=null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            this.btn_img_upload.setImageBitmap(bitmap);
        }else if (requestCode==RESULT_LOAD_IMAGE && data!=null){
            Uri selectedImage=data.getData();
            this.btn_img_upload.setImageURI(selectedImage);
        }else if (requestCode==PLACE_PICKER_REQUEST && resultCode==RESULT_OK && data != null){
            Place place = PlacePicker.getPlace(PasangIklanActivity.this, data);
            latitude_iklan=place.getLatLng().latitude;
            longitude_iklan=place.getLatLng().longitude;
            GeoLocation geoLocation= new GeoLocation(latitude_iklan,longitude_iklan,this);
            geoLocation.convert();
            provinsi=geoLocation.getProvinsi();

            String kota=geoLocation.getCountry();
            String ak=geoLocation.getCity();
            this.getDaerah();
            this.ET_lokasi_iklan.setText(""+place.getAddress());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }



    private void getUser(){
        final Loading loading= new Loading(this);
        loading.show();
        AuthFirebase authFirebase=new AuthFirebase();
        firebaseUser=authFirebase.getUserLogin();
        mDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member=dataSnapshot.getValue(Member.class);
                loading.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void getDaerah(){
        final Loading loading= new Loading(this);
        loading.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://developper.otomotifstore.com/lokasi/local/"+this.latitude_iklan+"/"+this.longitude_iklan;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    daerah=response.getString("nama_daerah");
                    loading.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
