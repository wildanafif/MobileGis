package com.example.wildanafif.skripsifix.entitas.maps;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.ui.fragment.IklanFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class Maps implements GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {
    private GoogleMap mMap;
    private Context context;
    private double latitude;
    private double longitude;
    private double radius;
    private HashMap<Marker, Iklan> mHashMap = new HashMap<>();
    private IklanFragment iklanFragment;
    private Circle circle;
    private ArrayList<Iklan> daftar_iklan;


    public Maps(IklanFragment iklanFragment, GoogleMap mMap, Context context, double latitude, double longitude) {
        this.iklanFragment=iklanFragment;
        this.mMap = mMap;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLokasi(double radius){
        //radius in km
        this.radius=radius;
        if (this.circle!=null){
            this.circle.remove();
        }
        circle=this.getCircle(radius);
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(this.latitude, this.longitude))      // Sets the center of the map to location user
            .zoom((float) (this.getZoomLevel(circle)-0.3))                 // Sets the zoom
                    // Sets the tilt of the camera to 30 degrees
            .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnCameraMoveListener(this);
    }

    public Circle getCircle(double radius){
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(this.latitude, this.longitude))
                .radius(radius*1000)//1000 meter
                .strokeWidth(0f)
                .fillColor(0x553d99f5));
        return circle;
    }

    public int getZoomLevel(Circle circle) {
        double radius = circle.getRadius();
        double scale = radius / 500;
        int zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));

        return zoomLevel;

    }

    public void showMarker(ArrayList<Iklan> daftar_iklanparams){
        this.daftar_iklan=daftar_iklanparams;
        for (Iklan iklan: daftar_iklan) {
            if (iklan.getLatitude()!=0){
                Integer icon_marker;
                if (iklan.getKategori().equals("Motor")){
                    icon_marker= R.drawable.markermotor;
                }else if (iklan.getKategori().equals("Mobil")){
                    icon_marker=R.drawable.markermobil;
                }else{
                    icon_marker=R.drawable.markerjasa;
                }
                Location startPoint=new Location("locationA");
                startPoint.setLatitude(this.latitude);
                startPoint.setLongitude(this.longitude);

                Location endPoint=new Location("locationA");
                endPoint.setLatitude(iklan.getLatitude());
                endPoint.setLongitude(iklan.getLongitude());

                double jangkauan=startPoint.distanceTo(endPoint);
                jangkauan=jangkauan/1000;
                if (jangkauan<=this.radius){
                    LatLng sydney = new LatLng(iklan.getLatitude(), iklan.getLongitude());
                    Marker marker =mMap.addMarker(
                            new MarkerOptions().position(sydney)
                                    .icon(BitmapDescriptorFactory.fromResource(icon_marker))
                    );
                    mHashMap.put(marker,iklan);
                }

            }

        }
        this.mMap.setOnMarkerClickListener(this);

    }

    public void updateMaps(double radius){
        this.mMap.clear();
        this.setLokasi(radius);

    }
    public void showUpdateMarker(){
        this.mHashMap.clear();
        for (Iklan iklan: daftar_iklan) {
            if (iklan.getLatitude()!=0){
                Integer icon_marker;
                if (iklan.getKategori().equals("Motor")){
                    icon_marker= R.drawable.markermotor;
                }else if (iklan.getKategori().equals("Mobil")){
                    icon_marker=R.drawable.markermobil;
                }else{
                    icon_marker=R.drawable.markerjasa;
                }
                Location startPoint=new Location("locationA");
                startPoint.setLatitude(this.latitude);
                startPoint.setLongitude(this.longitude);

                Location endPoint=new Location("locationA");
                endPoint.setLatitude(iklan.getLatitude());
                endPoint.setLongitude(iklan.getLongitude());

                double jangkauan=startPoint.distanceTo(endPoint);
                jangkauan=jangkauan/1000;
                if (jangkauan<=this.radius){
                    LatLng sydney = new LatLng(iklan.getLatitude(), iklan.getLongitude());
                    Marker marker =mMap.addMarker(
                            new MarkerOptions().position(sydney)
                                    .icon(BitmapDescriptorFactory.fromResource(icon_marker))
                    );
                    mHashMap.put(marker,iklan);
                }

            }

        }
        this.mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Iklan iklan=this.mHashMap.get(marker);
        this.iklanFragment.hideBottomNavigasi();
        this.iklanFragment.showDetailIklan(iklan);
        return true;
    }

    @Override
    public void onCameraMove() {
        this.iklanFragment.showBottomNavigasi();
        this.iklanFragment.hideDetailIklan();
    }
}
