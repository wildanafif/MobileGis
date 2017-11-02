package com.example.wildanafif.skripsifix;

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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.maps.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat_sender;
    private double long_sender;
    DatabaseReference database_daftar_ketemuan = FirebaseDatabase.getInstance().getReference("ketemuan").child("-KmX6pRDM08aQfW3Efas");
    private LocationManager locationManager;
    private LocationListener listener;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // Toast.makeText(MapsActivity.this, "Asss" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                database_daftar_ketemuan.child("latitude_sender").setValue(location.getLatitude());
                database_daftar_ketemuan.child("longitude_sender").setValue(location.getLongitude());
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
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
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


    }

    private void setlokasisaya() {
        LocationService locationService = new LocationService(MapsActivity.this);
        locationService.search();
        this.lat_sender= locationService.getLatitude();
        this.long_sender= locationService.getLongitude();
//        database_daftar_ketemuan.child("latitude_sender").setValue(locationService.getLatitude());
//        database_daftar_ketemuan.child("longitude_sender").setValue(locationService.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationService.getLatitude(), locationService.getLongitude()),15));
    }

    @Override
    protected void onStart() {
        super.onStart();
        database_daftar_ketemuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                Ketemuan ketemuan=dataSnapshot.getValue(Ketemuan.class);
               // Toast.makeText(MapsActivity.this, ""+ketemuan.getLatitude(), Toast.LENGTH_SHORT).show();
                LatLng sydney = new LatLng(ketemuan.getLatitude(), ketemuan.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

                LatLng saya = new LatLng(ketemuan.getLatitude_sender(), ketemuan.getLongitude_sender());
                mMap.addMarker(new MarkerOptions().position(saya).title("Nama Penjual")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.people_marker))
                );


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



}
