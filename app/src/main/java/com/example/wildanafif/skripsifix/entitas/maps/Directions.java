package com.example.wildanafif.skripsifix.entitas.maps;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.ui.activity.RuteKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.adapter.ListAdapterIntruksi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wildan afif on 8/3/2017.
 */

public class Directions {
    private LatLng origin;
    private LatLng destination;
    private static final String key="AIzaSyD3tKEtL6jn-iFY0caiTl18Xjx7PK4C6sQ";
    private String url;
    private int color;
    private Context context;
    private GoogleMap mMap;
    private ArrayList<String> panduan_arah= new ArrayList<>();
    private final  String lang="id";

    public Directions(LatLng origin, LatLng destination, int color,Context context, GoogleMap mMap) {
        this.origin = origin;
        this.destination = destination;
        this.color = color;
        this.context=context;
        this.mMap=mMap;
    }

    public Directions(LatLng origin, LatLng destination,Context context  ) {
        this.origin = origin;
        this.destination = destination;

        this.context=context;

    }

    public void getPolyline(){
        this.url="https://maps.googleapis.com/maps/api/directions/json?origin="+this.origin.latitude+","+this.origin.longitude+"&destination="+this.destination.latitude+","+this.destination.longitude+"&key="+this.key+"&language="+this.lang;

        final PolylineOptions options = new PolylineOptions().width(5).color(this.color).geodesic(true);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray routeArray = response.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(0);
                    JSONArray overviewPolylines = routes
                            .getJSONArray("legs");
                    JSONObject steps_obj = overviewPolylines.getJSONObject(0);
                    JSONArray step = steps_obj
                            .getJSONArray("steps");

                    for(int i=0;i<step.length();i++){

                        JSONObject json_rute = null;
                        try {
                            json_rute = step.getJSONObject(i);
                            JSONObject polyline=json_rute.getJSONObject("polyline");
                            List<LatLng> list = decodePoly(polyline.getString("points"));
                            for (int z = 0; z < list.size(); z++) {
                                LatLng point = list.get(z);
                                options.add(point);
                            }
                           // panduan_arah.add(json_rute.getString("html_instructions"));
                            //Log.d("intruksi", "onResponse: "+json_rute.getString("html_instructions"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Polyline line3 = mMap.addPolyline(options);

                    Log.d("rute", "onResponse: "+step);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.d("rute", "onResponse: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void getInructions(){
        this.url="https://maps.googleapis.com/maps/api/directions/json?origin="+this.origin.latitude+","+this.origin.longitude+"&destination="+this.destination.latitude+","+this.destination.longitude+"&key="+this.key+"&language="+this.lang;


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray routeArray = response.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(0);
                    JSONArray overviewPolylines = routes
                            .getJSONArray("legs");
                    JSONObject steps_obj = overviewPolylines.getJSONObject(0);
                    JSONArray step = steps_obj
                            .getJSONArray("steps");

                    for(int i=0;i<step.length();i++){

                        JSONObject json_rute = null;
                        try {
                            json_rute = step.getJSONObject(i);
                            JSONObject polyline=json_rute.getJSONObject("polyline");
                            List<LatLng> list = decodePoly(polyline.getString("points"));
                            for (int z = 0; z < list.size(); z++) {
                                LatLng point = list.get(z);

                            }
                            panduan_arah.add(json_rute.getString("html_instructions"));
                            Log.d("intruksi", "onResponse: "+json_rute.getString("html_instructions"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Dialog dialog2 = new Dialog(context);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog2.setContentView(R.layout.dialog_intruksi_lokasi);
                    ListView lv_panduan= (ListView) dialog2.findViewById(R.id.list_panduan_intruksi);
                    ListAdapterIntruksi adapter = new ListAdapterIntruksi(context, panduan_arah);
                    lv_panduan.setAdapter(adapter);
                    dialog2.show();


                    Log.d("rute", "onResponse: "+step);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.d("rute", "onResponse: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
    private List<LatLng> decodePoly(String encoded) {
        Log.d("sf", "decodePoly: "+encoded);
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public ArrayList<String> getPanduan_arah() {
        return panduan_arah;
    }

    public void setPanduan_arah(ArrayList<String> panduan_arah) {
        this.panduan_arah = panduan_arah;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

