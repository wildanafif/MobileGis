package com.example.wildanafif.skripsifix.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.Profil;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.entitas.volley.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_CODE = 234;
    private Button btn_ganti_foto;
    private Uri filePath;
    private NetworkImageView gambar_profil;
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("members");
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private FirebaseUser firebaseUser;
    private Member member;
    private String foto;
    private StorageReference riversRef;
    private StorageReference imgStorage;
    private String childImage;
    private EditText et_nama;
    private EditText et_telp;
    private EditText et_provinsi;
    private EditText et_daerah;
    private EditText et_pin_bb;
    private EditText et_facebook;
    private EditText et_instagram;
    private Button btn_simpan;
    private String[] provinsi = {"Jawa Timur","Jawa Barat"};
    private int pilihan_provinsi;

    private String[] daerah = {"Lowokwaru","Bwgundal"};
    private int pilihan_daerah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getUser();
        setContentView(R.layout.activity_setting_profile);

        this.btn_ganti_foto=(Button)findViewById(R.id.btn_ganti_foto);
        this.gambar_profil=(NetworkImageView)findViewById(R.id.gambar_profile);
        this.et_nama=(EditText)findViewById(R.id.nama);
        this.et_telp=(EditText)findViewById(R.id.telp);
        this.et_provinsi=(EditText)findViewById(R.id.provinsi);
        this.et_daerah=(EditText)findViewById(R.id.daerah);
        this.et_pin_bb=(EditText)findViewById(R.id.pin_bb);
        this.et_facebook=(EditText)findViewById(R.id.facebook);
        this.et_instagram=(EditText)findViewById(R.id.instagram);
        this.btn_simpan=(Button)findViewById(R.id.simpan);
        gambar_profil.setDefaultImageResId(R.drawable.loading);
        this.btn_ganti_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
            }
        });


    }

    private void setUi() {
        this.foto=member.getFoto();
        if (!member.getFoto().matches("")){
            Image image=new Image(SettingProfileActivity.this,foto,gambar_profil);
            image.showImage();
        }else{
            gambar_profil.setDefaultImageResId(R.drawable.gambar_profil);
        }
        if (!member.getNama().matches("")){
            this.et_nama.setText(member.getNama());
        }
        if (!member.getTelp().matches("")){
            this.et_telp.setText(member.getTelp());
        }
        if (!member.getProvinsi().matches("")){
            this.et_provinsi.setText(member.getProvinsi());
        }
        if (!member.getDaerah().matches("")){
            this.et_daerah.setText(member.getDaerah());
        }

        if (!member.getPin_bb().matches("")){
            this.et_pin_bb.setText(member.getPin_bb());
        }
        if (!member.getFacebook().matches("")){
            this.et_facebook.setText(member.getFacebook());
        }
        if (!member.getInstagram().matches("")){
            this.et_instagram.setText(member.getInstagram());
        }

        this.et_provinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SettingProfileActivity.this);
                dialog.setContentView(R.layout.pilihan_kategori);
                dialog.setTitle("Provinsi");
                ArrayAdapter adapter = new ArrayAdapter<String>(SettingProfileActivity.this,
                        android.R.layout.simple_list_item_1, provinsi);
                ListView listView = (ListView) dialog.findViewById(R.id.list_kategori);
                listView.setAdapter(adapter);
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pilihan_provinsi=position;
                        dialog.dismiss();
                        et_provinsi.setText(provinsi[position]);

                    }
                });
            }
        });

        this.et_daerah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SettingProfileActivity.this);
                dialog.setContentView(R.layout.pilihan_kategori);
                dialog.setTitle("Kab/Kota");
                ArrayAdapter adapter = new ArrayAdapter<String>(SettingProfileActivity.this,
                        android.R.layout.simple_list_item_1, daerah);
                ListView listView = (ListView) dialog.findViewById(R.id.list_kategori);
                listView.setAdapter(adapter);
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pilihan_daerah=position;
                        dialog.dismiss();
                        et_daerah.setText(daerah[position]);

                    }
                });
            }
        });

        this.btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMember();
            }
        });
    }

    private void updateMember() {
        Member simpanMember=new Member(member.getId(),this.et_nama.getText().toString(),member.getEmail(),member.getPassword(),this.et_telp.getText().toString(),this.et_provinsi.getText().toString(),this.et_daerah.getText().toString(),this.et_instagram.getText().toString(),this.et_pin_bb.getText().toString(),this.foto,this.et_facebook.getText().toString(),member.getTimestamp());
        Profil profil= new Profil(this);
        profil.update(simpanMember);
    }

    private void showFile(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            uploadFile();

        }
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Upload");
            progressDialog.show();

            riversRef = storageReference.child("images/"+member.getId()+".jpg");
            this.childImage="images/"+member.getId()+".jpg";
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            setUrlFoto(taskSnapshot);
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    private void getUser(){
        final Loading loading= new Loading(this);
        loading.show();
        AuthFirebase authFirebase=new AuthFirebase();
        firebaseUser=authFirebase.getUserLogin();
        mDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member=dataSnapshot.getValue(Member.class);
                setUi();
                loading.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setUrlFoto(final UploadTask.TaskSnapshot taskSnapshot){
        storageReference.child(this.childImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                foto = downloadUri.toString(); /// The string(file link) that you need
                Image image=new Image(SettingProfileActivity.this,foto,gambar_profil);
                image.showImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
