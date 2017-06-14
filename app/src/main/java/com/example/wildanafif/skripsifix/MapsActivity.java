package com.example.wildanafif.skripsifix;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.maps.Lokasi;
import com.example.wildanafif.skripsifix.ui.activity.RuteKetemuanActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , LocationListener {

    private GoogleMap mMap;
    private double lat_sender;
    private double long_sender;
    DatabaseReference database_daftar_ketemuan = FirebaseDatabase.getInstance().getReference("ketemuan").child("-KmX6pRDM08aQfW3Efas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        setlokasisaya();
    }

    private void setlokasisaya() {
        Lokasi lokasi= new Lokasi(MapsActivity.this);
        lokasi.cariLokasi();
        this.lat_sender=lokasi.getLatitude();
        this.long_sender=lokasi.getLongitude();
        database_daftar_ketemuan.child("latitude_sender").setValue(lokasi.getLatitude());
        database_daftar_ketemuan.child("longitude_sender").setValue(lokasi.getLongitude());
    }

    @Override
    protected void onStart() {
        super.onStart();
        database_daftar_ketemuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                Ketemuan ketemuan=dataSnapshot.getValue(Ketemuan.class);
                Toast.makeText(MapsActivity.this, ""+ketemuan.getLatitude(), Toast.LENGTH_SHORT).show();
                LatLng sydney = new LatLng(ketemuan.getLatitude(), ketemuan.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

                LatLng saya = new LatLng(ketemuan.getLatitude_sender(), ketemuan.getLongitude_sender());
                mMap.addMarker(new MarkerOptions().position(saya).title("Marker in Sydney"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                direction_api(saya,sydney);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String direction_api(LatLng sender, LatLng lokasi_tujuan){
        final String[] distance = new String[1];
        GoogleDirection.withServerKey("AIzaSyCaBKLH26hzGf4clbVOiR57xSXFX1iTJpg")
                .from(sender)
                .to(lokasi_tujuan)
                .alternativeRoute(true)
                .execute(
                        new DirectionCallback() {


                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                //Toast.makeText(mMap.this, "Oyi", Toast.LENGTH_SHORT).show();

//                                List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
//                                //ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
                                //DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                Info distanceInfo=leg.getDistance();
                                distance[0] = distanceInfo.getText();

                                // List<stepList> = leg.getStepList();
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED);
                                mMap.addPolyline(polylineOptions);


//                                for (int i = 0; i < direction.getRouteList().size(); i++) {
//                                    Toast.makeText(MapsActivity.this, direction.getRouteList().size()+"", Toast.LENGTH_SHORT).show();
//                                    Route route2 = (Route) direction.getRouteList().get(i);
//                                    String color = colors[i % colors.length];
//                                    mMap.addPolyline(DirectionConverter.createPolyline(getApplicationContext(), ((Leg) route2.getLegList().get(0)).getDirectionPoint(), 5, Color.parseColor(color)));
//                                }

                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {

                            }
                        }
                );
        return distance[0];
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "ok"+location.getLatitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
