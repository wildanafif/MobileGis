package com.example.wildanafif.skripsifix.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.IklanControl;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.example.wildanafif.skripsifix.entitas.volley.Image;
import com.example.wildanafif.skripsifix.ui.activity.AjakKetemuanActivity;
import com.example.wildanafif.skripsifix.ui.adapter.ListAdapterIklan;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseUser;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.wildanafif.skripsifix.R.id.map;

/**
 * Created by wildan afif on 5/11/2017.
 */

public class IklanFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
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
    private LinearLayout btn_ketemuan;
    private Iklan iklan;
    private ImageButton btn_filter;
    private View bsm_filter;
    private BottomSheetBehavior<View> filter;
    private BottomSheetBehavior<View> listIklan;
    DiscreteSeekBar discreteSeekBar1;
    private IklanControl iklanControl;
    private int radius=1;
    private EditText et_cariIklan;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String kategori="Semua Kategori";
    MaterialSpinner spinner;
    private ArrayList<Iklan> daftar_iklan;
    private View bsm_list_iklan;
    private ListView lv_list_iklan;
    private ListAdapterIklan adapter;
    private LinearLayout sms;
    private LinearLayout telephone;
    private LinearLayout whatsapp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_iklan, container, false);
        setHasOptionsMenu(true);
        mMapView = (MapView) view.findViewById(map);
        mMapView.onCreate(savedInstanceState);
        this.bsm=view.findViewById(R.id.sheetiklan);
        this.mBottomSheetBehavior= BottomSheetBehavior.from(this.bsm);
        this.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.bsm_filter=view.findViewById(R.id.layout_filter);
        this.filter= BottomSheetBehavior.from(this.bsm_filter);
        this.filter.setState(BottomSheetBehavior.STATE_HIDDEN);

        this.bsm_list_iklan=view.findViewById(R.id.sheetiklanlist);
        this.listIklan= BottomSheetBehavior.from(this.bsm_list_iklan);
        this.listIklan.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.lv_list_iklan=(ListView)view.findViewById(R.id.lv_list_iklan);

        this.btn_filter=(ImageButton)this.view.findViewById(R.id.filter);
        this.btn_filter.setOnClickListener(this);
        this.navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        this.et_cariIklan=(EditText)view.findViewById(R.id.cari_iklan);
        getActivity().setTitle("Iklan");
        et_cariIklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        discreteSeekBar1 = (DiscreteSeekBar) this.view.findViewById(R.id.discrete1);
        discreteSeekBar1.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                radius=value;
                iklanControl.filterDaftarIklan(radius,kategori);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }

        });

        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("Semua Kategori","Motor", "Mobil");
        this.spinnerAction();


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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_iklan, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.list_iklan){
            //Toast.makeText(getContext(), listIklan.getState()+"", Toast.LENGTH_SHORT).show();
            if (listIklan.getState()!=BottomSheetBehavior.STATE_HIDDEN){
                listIklan.setState(BottomSheetBehavior.STATE_HIDDEN);
            }else{
                //Toast.makeText(getContext(), "Oke", Toast.LENGTH_SHORT).show();
                adapter = new ListAdapterIklan(getContext(), daftar_iklan);
                this.lv_list_iklan.setAdapter(adapter);
                this.listIklan.setState(BottomSheetBehavior.STATE_EXPANDED);
                listIklan.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (listIklan.getState()==BottomSheetBehavior.STATE_DRAGGING){
                            listIklan.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });
                hideBottomNavigasi();
            }
            lv_list_iklan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listIklan.setState(BottomSheetBehavior.STATE_HIDDEN);
                    showDetailIklan(daftar_iklan.get(position));
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
            });

        }else if (id==R.id.search_lokasi){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(getActivity());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void spinnerAction() {
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
                iklanControl.filterDaftarIklan(radius,item);
                kategori=item;

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        iklanControl=new IklanControl(this,mMap,getContext());
        iklanControl.showListIklan(radius);

        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 150);
    }



    public void hideBottomNavigasi(){
        this.navigation.setVisibility(View.INVISIBLE);
    }
    public void showBottomNavigasi(){

        if (this.filter.getState()==BottomSheetBehavior.STATE_COLLAPSED){
            this.hideBottomNavigasi();
        }else {
            this.navigation.setVisibility(View.VISIBLE);
        }

    }
    public void showDetailIklan(Iklan iklan){
        this.iklan=iklan;
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
        deskripsi.setText(Html.fromHtml(iklan.getDeskripsi_iklan()));
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

        btn_ketemuan=(LinearLayout)this.view.findViewById(R.id.layout_ketemuan);
        this.sms=(LinearLayout)view.findViewById(R.id.sms);
        this.telephone=(LinearLayout)view.findViewById(R.id.telephone);
        this.whatsapp=(LinearLayout)view.findViewById(R.id.whatsapp);

        btn_ketemuan.setOnClickListener(this);
        this.sms.setOnClickListener(this);
        this.telephone.setOnClickListener(this);
        this.whatsapp.setOnClickListener(this);



    }

    public void initBottomShet (final Iklan iklan){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //inisial background

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            int init=0;
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                filter.setState(BottomSheetBehavior.STATE_HIDDEN);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_ketemuan:
                AuthFirebase authFirebase=new AuthFirebase();
                boolean login=authFirebase.isLogin();
                if (login){
                    FirebaseUser firebaseUser=authFirebase.getUserLogin();
                    if (iklan.getMail().equals(firebaseUser.getEmail())){
                        MessageDialog messageDialog=new MessageDialog(getContext());
                        messageDialog.setMessage("Mohon maaf, iklan ini adalah iklan anda");
                        messageDialog.show();
                    }else{
                        getActivity().finish();
                        Intent i=new Intent(getContext(), AjakKetemuanActivity.class);
                        i.putExtra("iklan", iklan);
                        startActivity(i);
                    }

                }else{
                    MessageDialog messageDialog=new MessageDialog(getContext());
                    messageDialog.setMessage("Fitur ini membutuhkan akses login, silahkan login terlebih dahulu");
                    messageDialog.show();
                }

                break;
            case R.id.filter:

                this.filter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                this.hideBottomNavigasi();
                break;
            case R.id.sms:
                Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + this.iklan.getTelp() ) );
                intentsms.putExtra( "sms_body", "" );
                startActivity( intentsms );
                break;
            case R.id.telephone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+this.iklan.getTelp()));
                startActivity(callIntent);
                break;
            case R.id.whatsapp:
                String smsNumber = this.iklan.getTelp(); //without '+'
                try {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch(Exception e) {
                    Toast.makeText(getContext(), "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void hideDetailIklan(){
        this.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getName());
                spinner.refreshDrawableState();
                spinner.setSelectedIndex(0);
                this.spinnerAction();

                kategori="Semua Kategori";
                iklanControl.filterDaftarIklan(radius,place.getLatLng().latitude,place.getLatLng().longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void setDaftar_iklan(ArrayList<Iklan> daftar_iklan) {
        this.daftar_iklan = daftar_iklan;
    }
}
