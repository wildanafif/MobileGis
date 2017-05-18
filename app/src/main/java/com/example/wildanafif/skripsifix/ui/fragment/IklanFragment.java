package com.example.wildanafif.skripsifix.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.IklanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.volley.Image;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import static com.example.wildanafif.skripsifix.R.id.map;

/**
 * Created by wildan afif on 5/11/2017.
 */

public class IklanFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MapView mMapView;
    private GoogleMap mMap;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private View bsm;
    private BottomNavigationView navigation;
    private Toolbar mActionBarToolbar;
    private TextView judul;
    private ImageView gambar;
    private NetworkImageView full_gambar;
    private LinearLayout header;
    private TextView nama_pengiklan;
    private ImageView userRegister;
    private TextView harga;
    private TextView telp;
    private TextView wa;
    private LinearLayout status_bb;
    private TextView pin_bb;
    private LinearLayout status_instagram;
    private TextView instagram;
    private TextView alamat;
    private TextView deskripsi;
    private NetworkImageView img_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_iklan, container, false);
        mMapView = (MapView) view.findViewById(map);
        mMapView.onCreate(savedInstanceState);
        this.bsm=view.findViewById(R.id.sheetiklan);
        this.mBottomSheetBehavior= BottomSheetBehavior.from(this.bsm);
        this.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        IklanControl iklanControl=new IklanControl(this,mMap,getContext());
        iklanControl.LihatDaftarIklan();


    }



    public void hideBottomNavigasi(){
        this.navigation.setVisibility(View.INVISIBLE);
    }
    public void showBottomNavigasi(){
        this.navigation.setVisibility(View.VISIBLE);
    }
    public void showDetailIklan(Iklan iklan){
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbarIklan);
        this.mActionBarToolbar.setVisibility(View.GONE);
        this.initDetailIklanUi();
        judul.setText(iklan.getJudul_iklan());
        nama_pengiklan.setText(iklan.getNama());
        userRegister.setVisibility(View.INVISIBLE);
        if (iklan.getUser_register()==1){
            userRegister.setVisibility(View.VISIBLE);
        }
        String nego="Tidak Nego";
        if (iklan.getNego()==1){
            nego="Nego";
        }
        harga.setText("Rp. "+iklan.getHarga()+" - "+nego);
        telp.setText(iklan.getTelp());
        GeoLocation geoLocation=new GeoLocation(iklan.getLatitude(),iklan.getLongitude(),getContext());
        geoLocation.convert();
        alamat.setText(geoLocation.GetLocation());
        deskripsi.setText(iklan.getDeskripsi_iklan());
        Image image=new Image(getContext(),"https://www.otomotifstore.com/assets/"+iklan.getTemp_foto(),this.img_title);
        image.showImage();
        image.setImg(full_gambar);
        image.showImage();
        this.initBottomShet(iklan);
        full_gambar.setVisibility(View.GONE);



    }

    private void initDetailIklanUi(){
        this.judul= (TextView) this.view.findViewById(R.id.judul_iklan);
        gambar=(ImageView)this.view.findViewById(R.id.gambar_iklan);
        full_gambar= (NetworkImageView) this.view.findViewById(R.id.full_gambar);
        header=(LinearLayout)this.view.findViewById(R.id.headerCollpase);
        nama_pengiklan= (TextView) this.view.findViewById(R.id.nama_pengiklan);
        userRegister= (ImageView) this.view.findViewById(R.id.verivikasiUser);
        harga= (TextView) this.view.findViewById(R.id.harga);
        telp= (TextView) this.view.findViewById(R.id.telp);
        wa= (TextView) this.view.findViewById(R.id.wa);
        status_bb= (LinearLayout) this.view.findViewById(R.id.status_bb);
        pin_bb= (TextView) this.view.findViewById(R.id.pin_bb);
        status_instagram= (LinearLayout) this.view.findViewById(R.id.status_instagram);
        instagram= (TextView) this.view.findViewById(R.id.instagram);
        alamat= (TextView) this.view.findViewById(R.id.alamat);
        deskripsi= (TextView) this.view.findViewById(R.id.deskripsi);
        img_title = (NetworkImageView) this.view.findViewById(R.id.gambar_iklan);
    }

    public void initBottomShet (final Iklan iklan){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //inisial background

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            int init=0;
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


                if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    header.setBackgroundColor(Color.parseColor("#21aee8"));
                    judul.setTextColor(Color.parseColor("#ffffff"));
                    full_gambar.setVisibility(View.VISIBLE);
                    final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    toolbar.setVisibility(View.GONE);


                    mActionBarToolbar.setVisibility(View.VISIBLE);

                    gambar.setVisibility(View.GONE);

                    mActionBarToolbar.setTitle(iklan.getJudul_iklan());
                    mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            mActionBarToolbar.setVisibility(View.GONE);
                            toolbar.setVisibility(View.VISIBLE);
                            header.setBackgroundColor(Color.parseColor("#ffffff"));
                            judul.setTextColor(Color.parseColor("#000000"));
                            full_gambar.setVisibility(View.GONE);
                            gambar.setVisibility(View.VISIBLE);
                        }
                    });
                    mActionBarToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);


                }else {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
//                    header.setBackgroundColor(Color.parseColor("#ffffff"));
//                    judul.setTextColor(Color.parseColor("#000000"));
//                    full_gambar.setVisibility(View.GONE);

                }
//
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Toast.makeText(getContext(), "tsedau", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
