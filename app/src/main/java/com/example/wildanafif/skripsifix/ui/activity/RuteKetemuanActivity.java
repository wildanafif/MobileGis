package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.maps.Lokasi;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class RuteKetemuanActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myLocation;
    private Intent intent;
    private Ketemuan ketemuan;
    private LatLng lokasi_tujuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        intent = getIntent();
        ketemuan= (Ketemuan) intent.getSerializableExtra("ketemuan");
        Toast.makeText(this, ""+ketemuan.getLatitude(), Toast.LENGTH_SHORT).show();
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
        this.setMyLocation();
        this.direction_api();
        // Add a marker in Sydney and move the camera

    }

    private void setMyLocation(){
        Lokasi lokasi=new Lokasi(this);
        lokasi.cariLokasi();
        if (lokasi.isLocation()){
            myLocation= new LatLng(lokasi.getLatitude(),lokasi.getLongitude());
        }else{
            myLocation = new LatLng(-34, 151);
            MessageDialog messageDialog= new MessageDialog(this);
            messageDialog.setMessage("Tidak dapat menemukan lokasi");
            messageDialog.show();
        }
        lokasi_tujuan=new LatLng(ketemuan.getLatitude(),ketemuan.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Lokasi saat ini"));
        mMap.addMarker(new MarkerOptions().position(lokasi_tujuan).title("Lokasi "+ketemuan.getIklan().getJudul_iklan()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14));
    }

    public String direction_api(){
        final String[] distance = new String[1];
        GoogleDirection.withServerKey("AIzaSyCaBKLH26hzGf4clbVOiR57xSXFX1iTJpg")
                .from(myLocation)
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
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(RuteKetemuanActivity.this, directionPositionList, 5, Color.RED);
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
