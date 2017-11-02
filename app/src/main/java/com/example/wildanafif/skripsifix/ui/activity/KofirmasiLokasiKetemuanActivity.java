package com.example.wildanafif.skripsifix.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.KetemuanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.graphics.Color.parseColor;

public class KofirmasiLokasiKetemuanActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LatLng lokasiIklan;
    private Intent intent;
    private Ketemuan ketemuan;
    private Iklan iklan;
    private HashMap<Marker, LokasiKetemuan> mHashMapMarker = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kofirmasi_lokasi_ketemuan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("Konfirmasi Lokasi Transaksi");


        intent = getIntent();
        ketemuan = (Ketemuan) intent.getSerializableExtra("ketemuan");
        this.iklan = ketemuan.getIklan();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lokasiIklan = new LatLng(iklan.getLatitude(), iklan.getLongitude());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        Marker m=mMap.addMarker(new MarkerOptions()
                .position(lokasiIklan)
                .title(iklan.getJudul_iklan())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiIklan,14));
        mMap.setOnMarkerClickListener(this);
        mHashMapMarker.put(m, null);

        getLokasiKetemuan();
    }

    private void getLokasiKetemuan() {
        final Loading loading= new Loading(this);
        loading.show();
        DatabaseReference database_member = FirebaseDatabase.getInstance().getReference("lokasi_ketemuan");
        Query query =  database_member.orderByChild("id_ketemuan").equalTo(ketemuan.getId_ketemuan());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LokasiKetemuan lokasiKetemuan = null;
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    lokasiKetemuan = userSnpashot.getValue(LokasiKetemuan.class);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lokasiKetemuan.getLatitude(),lokasiKetemuan.getLongitude()))
                            .title(lokasiKetemuan.getAlamat())

                    );
                    mHashMapMarker.put(marker, lokasiKetemuan);
                }

                loading.hide();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (this.mHashMapMarker.get(marker)!=null){
            final CharSequence[] dialogitem = {"Lihat", "Hapus"};
            AlertDialog.Builder builder = new AlertDialog.Builder(KofirmasiLokasiKetemuanActivity.this);
            builder.setTitle("Pilihan");
            builder.setMessage(this.mHashMapMarker.get(marker).getAlamat());
            builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    KetemuanControl ketemuanControl= new KetemuanControl(KofirmasiLokasiKetemuanActivity.this);
                    ketemuanControl.konfirmasi_ketemuan(mHashMapMarker.get(marker),ketemuan);
                }
            });


            AlertDialog a=builder.create();
            a.show();
            Button BN = a.getButton(DialogInterface.BUTTON_POSITIVE);
            BN.setTextColor(Color.GREEN);
        }

        return false;
    }
}
