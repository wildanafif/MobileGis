package com.example.wildanafif.skripsifix.ui.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.KetemuanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.entitas.volley.Image;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class AjakKetemuanActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int PLACE_PICKER_REQUEST = 1;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private EditText et_date;
    private EditText et_lokasi;
    private double latitude=0;
    private double longitude=0;
    private NetworkImageView img_location;
    private LinearLayout layout_tempat_ketemuan;
    private Iklan iklan;
    private Button btn_ajak_ketemuan;
    private EditText et_message;
    private FirebaseUser me;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajak_ketemuan);
        this.iklan = (Iklan) getIntent().getSerializableExtra("iklan");
        setTitle(iklan.getNama()+" - "+iklan.getJudul_iklan());
        this.et_date=(EditText)findViewById(R.id.et_date);
        this.et_lokasi=(EditText)findViewById(R.id.et_lokasi);
        this.et_message=(EditText)findViewById(R.id.et_katakan_sesuatu);
        this.img_location = (NetworkImageView)findViewById(R.id.place_map);
        this.layout_tempat_ketemuan=(LinearLayout)findViewById(R.id.layout_tempat_ketemuan);
        this.layout_tempat_ketemuan.setVisibility(View.GONE);
        this.btn_ajak_ketemuan=(Button)findViewById(R.id.btn_ajak_ketemuan);
        this.btn_ajak_ketemuan.setOnClickListener(this);
        this.et_date.setOnClickListener(this);
        this.et_lokasi.setOnClickListener(this);
        AuthFirebase authFirebase=new AuthFirebase(getApplicationContext());
        me=authFirebase.getUserLogin();
        getMember();
        requestPermission();
    }

    private void getMember() {
        final Loading loading= new Loading(this);
        loading.show();
        DatabaseReference database_member = FirebaseDatabase.getInstance().getReference("members");

        Query query =  database_member.orderByChild("email").equalTo(me.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member = null;
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    member = userSnpashot.getValue(Member.class);
                }

                loading.hide();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            case R.id.et_date:
                updateDate();
                break;
            case R.id.et_lokasi:
                cariLokasi();
                break;
            case R.id.btn_ajak_ketemuan:
                ketemuan();
        }
    }

    private void ketemuan() {

//        KetemuanControl ketemuanControl=new KetemuanControl(this);
//        ketemuanControl.ketemuan(
//                me.getEmail(),
//                this.iklan.getMail(),
//                this.et_date.getText().toString(),
//                this.et_lokasi.getText().toString(),
//                this.latitude,
//                this.longitude,
//                this.et_message.getText().toString(),
//                this.iklan
//
//        );
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = dateFormat.parse("23/09/2007");
        Ketemuan ketemuan= new Ketemuan(
                member.getNama(),
                dateTime.getTimeInMillis(),
                me.getEmail(),
                this.iklan.getMail(),
                this.et_date.getText().toString(),
                "",
                0,
                0,
                this.et_message.getText().toString(),
                this.iklan,
                false
        );

        KetemuanControl ketemuanControl= new KetemuanControl(this);
        ketemuanControl.ajakKetemuan(ketemuan);
    }

    public void redirect(){

    }

    private void cariLokasi() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = builder.build(AjakKetemuanActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void updateTime() {
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //updateTextLabel();
            updateTime();
        }
    };
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTextLabel();
        }
    };
    private void updateTextLabel(){
        //this.et_date.setText(dateTime.get(Calendar.DAY_OF_MONTH)+"-"+dateTime.get(Calendar.MONTH)+"-"+dateTime.get(Calendar.YEAR));
        this.et_date.setText(formatDateTime.format(dateTime.getTime()));

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK){


                Place place = PlacePicker.getPlace(AjakKetemuanActivity.this, data);
                latitude=place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;

                Image image=new Image(AjakKetemuanActivity.this,"http://maps.googleapis.com/maps/api/staticmap?zoom=17&size=1200x200&maptype=roadmap&markers=color:red%7C"+latitude+","+longitude+"&key=AIzaSyCEZFJ3d8Ky8pNd9fJSf33v2wR29_TOpJw",img_location);
                image.showImage();
                layout_tempat_ketemuan.setVisibility(View.VISIBLE);
                this.et_lokasi.setText(place.getName()+", "+place.getAddress());
                //placeAddressText.setText(place.getAddress());

            }
        }
    }
}
