package com.example.wildanafif.skripsifix.entitas.maps;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class GeoLocation {
    private double latitude;
    private  double longitude;
    Context context;
    Geocoder geocoder ;

    private String provinsi;
    private String addrs;
    private String city;
    private String country;
    private String postalCode;
    private String knownName;

    public GeoLocation(double latitude, double longitude, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    public void convert(){
        Locale locale = new Locale("id");
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config);
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        geocoder = new Geocoder(this.context, locale);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addrs = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        knownName = addresses.get(0).getFeatureName();
        provinsi=addresses.get(0).getAdminArea();
    }

    public String getProvinsi() {
        return provinsi;
    }
    public String GetLocation(){
        String location = addrs+" "+city+" "+provinsi+" "+country+" "+postalCode+" "+knownName;
        return location;
    }

}
