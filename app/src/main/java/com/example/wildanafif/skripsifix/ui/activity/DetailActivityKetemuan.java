package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.MapsActivity;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.firebasae.KetemuanFirebase;
import com.example.wildanafif.skripsifix.entitas.maps.GeoLocation;
import com.example.wildanafif.skripsifix.entitas.volley.Image;

public class DetailActivityKetemuan extends AppCompatActivity implements View.OnClickListener, View.OnScrollChangeListener {

    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private View bsm;
    private TextView judul;
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
    private ImageView gambar;
    private Iklan iklan;
    private Toolbar mActionBarToolbar;
    private TextView nama_ketemuan;
    private TextView lokasi_ketemuan;
    private TextView waktu_ketemuan;
    private TextView pesan_ketemuan;
    private NetworkImageView gambar_lokasi_ketemuan;
    private ScrollView scrl;
    private Button btn_lihat_lokasi;
    private LinearLayout layout_konfirmasi;
    private Button btn_terima;
    private Button btn_tolak;
    private KetemuanFirebase ketemuanFirebase;
    private TextView et_status_konfirmasi;
    private Ketemuan ketemuan;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ketemuan);
        intent = getIntent();
        ketemuan= (Ketemuan) intent.getSerializableExtra("ketemuan");
        this.iklan=ketemuan.getIklan();
        this.ketemuanFirebase= new KetemuanFirebase(this);

        getSupportActionBar().setTitle("Ketemuan Dengan "+iklan.getNama());


        this.bsm=findViewById(R.id.sheet_detail_iklan);
        this.mBottomSheetBehavior= BottomSheetBehavior.from(this.bsm);
        this.nama_ketemuan=(TextView)findViewById(R.id.nama_ketemuan);
        this.lokasi_ketemuan=(TextView)findViewById(R.id.lokasi_ketemuan);
        this.waktu_ketemuan=(TextView)findViewById(R.id.waktu_ketemuan);
        this.pesan_ketemuan=(TextView)findViewById(R.id.pesan_ketemuan);
        this.gambar_lokasi_ketemuan=(NetworkImageView)findViewById(R.id.gambar_lokasi_ketemuan);
        this.btn_lihat_lokasi=(Button)findViewById(R.id.btn_lihat_lokasi);
        this.et_status_konfirmasi=(TextView)findViewById(R.id.status_konfirmasi);
        layout_konfirmasi=(LinearLayout)findViewById(R.id.layout_konfirmasi);
        scrl= (ScrollView)findViewById(R.id.scrl);
        scrl.setOnScrollChangeListener(this);
        this.nama_ketemuan.setText(iklan.getNama());
        this.lokasi_ketemuan.setText(ketemuan.getTempat_ketemuan());
        this.waktu_ketemuan.setText(ketemuan.getWaktu());
        this.pesan_ketemuan.setText(ketemuan.getMessage());
        this.showDetailIklan();
        btn_terima=(Button)findViewById(R.id.terima);
        btn_tolak=(Button)findViewById(R.id.tolak);
        this.init_ui();
    }

    private void init_ui(){
        boolean status_confirm=this.ketemuan.isConfirmReceiver();
        if (status_confirm){
            et_status_konfirmasi.setText("Disetujui");
            et_status_konfirmasi.setBackgroundDrawable( getResources().getDrawable(R.drawable.ketemuan_disetujui) );
        }else{
            et_status_konfirmasi.setText("Belum di setujui");
            et_status_konfirmasi.setBackgroundDrawable( getResources().getDrawable(R.drawable.iklan_belum_disetujui) );
        }
        btn_terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ketemuan.setConfirmReceiver(true);
                ketemuanFirebase.konfirmasi(ketemuan);
                redirect();
            }
        });
        btn_tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ketemuan.setConfirmReceiver(false);
                ketemuanFirebase.konfirmasi(ketemuan);
                redirect();
            }
        });
        boolean iskonfirmasi=intent.getExtras().getBoolean("konfirmasi");
        if (iskonfirmasi){
            layout_konfirmasi.setVisibility(View.VISIBLE);

        }else{

            layout_konfirmasi.setVisibility(View.INVISIBLE);
        }

        this.btn_lihat_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMaps();
            }
        });

        Image image=new Image(DetailActivityKetemuan.this,"http://maps.googleapis.com/maps/api/staticmap?zoom=17&size=1200x800&maptype=roadmap&markers=color:red%7C"+ketemuan.getLatitude()+","+ketemuan.getLongitude()+"&key=AIzaSyCEZFJ3d8Ky8pNd9fJSf33v2wR29_TOpJw",gambar_lokasi_ketemuan);
        image.showImage();
    }

    public void showDetailIklan(){
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarIklan);
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
        GeoLocation geoLocation=new GeoLocation(iklan.getLatitude(),iklan.getLongitude(),this);
        geoLocation.convert();
        alamat.setText(geoLocation.GetLocation());
        deskripsi.setText(iklan.getDeskripsi_iklan());
        Image image=new Image(this,"https://www.otomotifstore.com/assets/"+iklan.getTemp_foto(),this.img_title);
        image.showImage();
        image.setImg(full_gambar);
        image.showImage();
        this.initBottomShet();
        full_gambar.setVisibility(View.GONE);

        mActionBarToolbar.setNavigationOnClickListener(this);


    }

    private void initDetailIklanUi(){
        this.judul= (TextView) this.findViewById(R.id.judul_iklan);
        gambar=(ImageView)this.findViewById(R.id.gambar_iklan);
        full_gambar= (NetworkImageView) this.findViewById(R.id.full_gambar);
        header=(LinearLayout)this.findViewById(R.id.headerCollpase);
        nama_pengiklan= (TextView) this.findViewById(R.id.nama_pengiklan);
        userRegister= (ImageView) this.findViewById(R.id.verivikasiUser);
        harga= (TextView) this.findViewById(R.id.harga);
        telp= (TextView) this.findViewById(R.id.telp);
        wa= (TextView) this.findViewById(R.id.wa);
        status_bb= (LinearLayout) this.findViewById(R.id.status_bb);
        pin_bb= (TextView) this.findViewById(R.id.pin_bb);
        status_instagram= (LinearLayout) this.findViewById(R.id.status_instagram);
        instagram= (TextView) this.findViewById(R.id.instagram);
        alamat= (TextView) this.findViewById(R.id.alamat);
        deskripsi= (TextView) this.findViewById(R.id.deskripsi);
        img_title = (NetworkImageView) this.findViewById(R.id.gambar_iklan);

    }


    public void initBottomShet (){
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


                    getSupportActionBar().hide();
                    mActionBarToolbar.setVisibility(View.VISIBLE);

                    gambar.setVisibility(View.GONE);
                    mActionBarToolbar.setTitle(iklan.getJudul_iklan());

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
        hide_bottom();
        hide_bottom();

    }

    private void goMaps(){
        Intent i= new Intent(DetailActivityKetemuan.this, RuteKetemuanActivity.class);
        i.putExtra("ketemuan",ketemuan);
        startActivity(i);
    }

    private void hide_bottom(){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                            mActionBarToolbar.setVisibility(View.GONE);
        getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            header.setBackground(getResources().getDrawable(R.drawable.border_top));
        }
        judul.setTextColor(Color.parseColor("#000000"));
                            full_gambar.setVisibility(View.GONE);
        gambar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY>=oldScrollY){
            //Toast.makeText(this, "sss", Toast.LENGTH_SHORT).show();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else{
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void redirect(){
        Intent intent=new Intent(DetailActivityKetemuan.this,DaftarKetemuanActivity.class);
        startActivity(intent);
    }
}
