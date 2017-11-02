package com.example.wildanafif.skripsifix.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.Directions;
import com.example.wildanafif.skripsifix.entitas.maps.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class RuteKetemuanActivity extends AppCompatActivity implements OnMapReadyCallback {

    DatabaseReference database_daftar_ketemuan;
    private GoogleMap mMap;
    private LatLng myLocation;
    private Intent intent;
    private Ketemuan ketemuan;
    private LatLng lokasi_tujuan;
    private LocationManager locationManager;
    private LocationListener listener;
    private FirebaseUser firebaseUser;
    private Polyline [] polyline = new Polyline[2];
    private boolean status_berangkat=false;
    private Button btn_berangkat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute_ketemuan);
        this.btn_berangkat=(Button)findViewById(R.id.btn_berangkat);
        this.btn_berangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                berangkat(myLocation,lokasi_tujuan);
            }
        });
        intent = getIntent();
        ketemuan = (Ketemuan) intent.getSerializableExtra("ketemuan");
        setTitle(ketemuan.getTempat_ketemuan());
        this.lokasi_tujuan=new LatLng(ketemuan.getLatitude(),ketemuan.getLongitude());
        Timestamp timestamp = new Timestamp(ketemuan.getTimestamp_ketemuan());
        Date date = new Date(timestamp.getTime());
        String S = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(ketemuan.getTimestamp_ketemuan());
        //Toast.makeText(this, ""+S, Toast.LENGTH_SHORT).show();
        this.database_daftar_ketemuan = FirebaseDatabase.getInstance().getReference("ketemuan").child(ketemuan.getId_ketemuan());
        //Toast.makeText(this, "" + ketemuan.getLatitude(), Toast.LENGTH_SHORT).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AuthFirebase authFirebase = new AuthFirebase();
        firebaseUser = authFirebase.getUserLogin();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation=new LatLng(location.getLatitude(),location.getLongitude());
                if (firebaseUser.getEmail().matches(ketemuan.getEmail_receiver())){
                    updateLokasiReceiver(location);

                    Log.d("LocationService Receive", "onLocationChanged: ");
                }
                else if (firebaseUser.getEmail().matches(ketemuan.getEmail_sender())){
                    updateLokasiSender(location);
                    Log.d("LocationService dender", "onLocationChanged: ");
                }
                if (status_berangkat){
                    berangkat(myLocation,lokasi_tujuan);
                }

                // Toast.makeText(MapsActivity.this, "Asss" + location.getLatitude(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, INTERNET}
                    , 10);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    private void berangkat(LatLng lokasi,LatLng tujuan) {
        if (!status_berangkat){
            Directions directions= new Directions(lokasi,tujuan,RuteKetemuanActivity.this);
            directions.getInructions();
        }
        status_berangkat=true;
        mMap.clear();
        Marker lokasi_awal = null;
        if (firebaseUser.getEmail().matches(ketemuan.getEmail_receiver())){
            direction(lokasi,tujuan,"receiver");
            lokasi_awal=mMap.addMarker(new MarkerOptions().position(lokasi).title("Lokasi Anda")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
            );
        }
        else if (firebaseUser.getEmail().matches(ketemuan.getEmail_sender())){
            lokasi_awal=mMap.addMarker(new MarkerOptions().position(lokasi).title("Lokasi Anda")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
            );
            direction(lokasi,tujuan,"sender");
        }

        Marker lokasi_tujuan=mMap.addMarker(new MarkerOptions().position(tujuan).title("Lokasi Ketemuan, "+ketemuan.getTempat_ketemuan()));

        double lat1 =lokasi_awal.getPosition().latitude;
        double lng1 =lokasi_awal.getPosition().longitude;

        double lat2 =lokasi_tujuan.getPosition().latitude;
        double lng2 =lokasi_tujuan.getPosition().longitude;
        double dLon = (lng2-lng1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        brng = (360 - ((brng + 360) % 360));

        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(lokasi_awal.getPosition())
                .bearing((float) brng).tilt(65.5f).zoom(18f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi_awal.getPosition()));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi,18));




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.setMyLocation();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Add a marker in Sydney and move the camera

    }

    private void updateLokasiSender(Location location){
        database_daftar_ketemuan.child("latitude_sender").setValue(location.getLatitude());
        database_daftar_ketemuan.child("longitude_sender").setValue(location.getLongitude());
    }

    private void updateLokasiReceiver(Location location){
        database_daftar_ketemuan.child("latitude_receiver").setValue(location.getLatitude());
        database_daftar_ketemuan.child("longitude_receiver").setValue(location.getLongitude());
    }

    private void setMyLocation() {
        LocationService locationService = new LocationService(RuteKetemuanActivity.this);
        locationService.search();
        myLocation=new LatLng(locationService.getLatitude(),locationService.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationService.getLatitude(), locationService.getLongitude()),14));
    }

    @Override
    protected void onStart() {
        super.onStart();
        database_daftar_ketemuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ketemuan=dataSnapshot.getValue(Ketemuan.class);
                if (!status_berangkat){
                    mMap.clear();
                    LatLng sydney = new LatLng(ketemuan.getLatitude(), ketemuan.getLongitude());
                    lokasi_tujuan=sydney;
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Lokasi Ketemuan").snippet(ketemuan.getTempat_ketemuan())).showInfoWindow();
                    String lokasi1="Lokasi terakhir pembeli";
                    if (firebaseUser.getEmail().matches(ketemuan.getEmail_sender())){
                        lokasi1="Lokasi anda saat ini";
                    }
                    LatLng pembeli = new LatLng(ketemuan.getLatitude_sender(), ketemuan.getLongitude_sender());

                    mMap.addMarker(new MarkerOptions().position(pembeli).title(lokasi1)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                    ).showInfoWindow();

                    String lokasi2="Lokasi terakhir penjual";
                    if (firebaseUser.getEmail().matches(ketemuan.getEmail_receiver())){
                        lokasi2="Lokasi anda saat ini";
                    }
                    LatLng penjual = new LatLng(ketemuan.getLatitude_receiver(), ketemuan.getLongitude_receiver());
                    mMap.addMarker(new MarkerOptions().position(penjual).title(lokasi2)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
                    ).showInfoWindow();

                    if (ketemuan.getLongitude_sender()!=0){
                        direction(pembeli,sydney,"sender");
                    }
                    if (ketemuan.getLatitude_receiver()!=0){
                        direction(penjual,sydney,"receiver");
                    }
                }else{

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void direction(LatLng sender, LatLng lokasi_tujuan, final String status){
        int warna = Color.RED;
        int status_direction = 0;
        if (status.matches("receiver")){
            warna=Color.GREEN;
            status_direction=0;
        }else if (status.matches("sender")){
            warna=Color.RED;
            status_direction=1;
        }
        // List<stepList> = leg.getStepList();
        if (status_berangkat){
            if (polyline[0]!=null){
                polyline[0].remove();
            }
            if (polyline[1]!=null){
                polyline[1].remove();
            }
        }else{
            if (polyline[status_direction]!=null){
                polyline[status_direction].remove();
            }
        }
        Directions directions= new Directions(sender,lokasi_tujuan,warna,RuteKetemuanActivity.this,mMap);
        directions.getPolyline();


    }


}
