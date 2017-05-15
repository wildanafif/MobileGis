package com.example.wildanafif.skripsifix.entitas.maps;

import android.content.Context;
import android.location.Location;

import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class Maps {
    private GoogleMap mMap;
    private Context context;
    private double latitude;
    private double longitude;
    private double radius;


    public Maps(GoogleMap mMap, Context context, double latitude, double longitude) {
        this.mMap = mMap;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLokasi(double radius){
        //radius in km
        this.radius=radius;
        Circle circle=this.getCircle(radius);
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(this.latitude, this.longitude))      // Sets the center of the map to location user
            .zoom((float) (this.getZoomLevel(circle)-0.3))                 // Sets the zoom
                    // Sets the tilt of the camera to 30 degrees
            .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    public void showMarker(ArrayList<Iklan> daftar_iklan){
        for (Iklan object: daftar_iklan) {
            if (object.getLatitude()!=0){

                Location startPoint=new Location("locationA");
                startPoint.setLatitude(this.latitude);
                startPoint.setLongitude(this.longitude);

                Location endPoint=new Location("locationA");
                endPoint.setLatitude(object.getLatitude());
                endPoint.setLongitude(object.getLongitude());

                double jangkauan=startPoint.distanceTo(endPoint);
                jangkauan=jangkauan/1000;
                if (jangkauan<=this.radius){
                    LatLng sydney = new LatLng(object.getLatitude(), object.getLongitude());
                    Marker marker =mMap.addMarker(
                            new MarkerOptions()
                                    .position(sydney)

                    );
                }

            }

        }

    }
}
