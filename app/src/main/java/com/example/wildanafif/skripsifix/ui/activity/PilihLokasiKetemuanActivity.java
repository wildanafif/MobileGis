package com.example.wildanafif.skripsifix.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.KetemuanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.LokasiKetemuan;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.maps.Lokasi;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class PilihLokasiKetemuanActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LatLng myLocation;
    private int jumlah_lokasi_yg_dipilih=0;
    private HashMap<Marker, LokasiKetemuan> mHashMapMarker = new HashMap<>();
    private Ketemuan ketemuan;
    private View simpan_ketemuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_lokasi_ketemuan);
        ketemuan = (Ketemuan) getIntent().getSerializableExtra("ketemuan");
        this.simpan_ketemuan=findViewById(R.id.btn_simpan_ketemuan);
        this.simpan_ketemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHashMapMarker.isEmpty()){
                    MessageDialog messageDialog= new MessageDialog(PilihLokasiKetemuanActivity.this);
                    messageDialog.setMessage("Anda minimal harus memilih satu lokasi ketemuan");
                    messageDialog.show();
                }else{
                    KetemuanControl ketemuanControl= new KetemuanControl(PilihLokasiKetemuanActivity.this);
                    ketemuanControl.simpanKetemuan(ketemuan,mHashMapMarker);
                }

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("Pilih Lokasi Ketemuan");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMaps();
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    private void setMaps() {
        Lokasi lokasi = new Lokasi(this);
        lokasi.cariLokasi();
        if (lokasi.isLocation()) {
            myLocation = new LatLng(lokasi.getLatitude(), lokasi.getLongitude());
        } else {
            myLocation = new LatLng(-34, 151);
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.setMessage("Tidak dapat menemukan lokasi");
            messageDialog.show();
        }

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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,16));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.jumlah_lokasi_yg_dipilih++;
        GeoLocation geoLocation=new GeoLocation(latLng.latitude,latLng.longitude,this);
        geoLocation.convert();
        String alamat=geoLocation.GetLocation();
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Lokasi "+this.jumlah_lokasi_yg_dipilih)
                .snippet(alamat)
        );
        marker.showInfoWindow();

        this.mHashMapMarker.put(marker, new LokasiKetemuan(latLng.latitude,latLng.longitude,alamat));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        LokasiKetemuan ketemuan=this.mHashMapMarker.get(marker);
        final Dialog dialog = new Dialog(PilihLokasiKetemuanActivity.this);
        dialog.setContentView(R.layout.dialog_pilihan_marker_lokasi_ketemuan);
        dialog.setTitle("Lokasi Ketemuan");
        TextView textViewLokasi=(TextView)dialog.findViewById(R.id.lokasi_pilihan) ;
        textViewLokasi.setText(ketemuan.getAlamat());
        Button btn_hapus_lokasi=(Button)dialog.findViewById(R.id.btn_hapus_lokasi_ketemuan);
        btn_hapus_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                mHashMapMarker.remove(marker);
                dialog.dismiss();
                Toast.makeText(PilihLokasiKetemuanActivity.this, "Lokasi Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

        return false;
    }
}
