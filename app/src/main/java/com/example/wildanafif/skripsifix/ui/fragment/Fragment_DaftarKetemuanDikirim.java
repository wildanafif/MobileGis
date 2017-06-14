package com.example.wildanafif.skripsifix.ui.fragment;

/**
 * Created by wildan afif on 5/21/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.firebasae.AuthFirebase;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuan;
import com.example.wildanafif.skripsifix.ui.adapter.ListViewAdapterDaftarKetemuan;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_DaftarKetemuanDikirim extends Fragment {

    private View view;
//    private Auth auth= new Auth(getContext());
    private ListView lvProduct;
    private ArrayList<Ketemuan> daftarKetemuan;
    private DatabaseReference databaseReferenceListUsers=FirebaseDatabase.getInstance().getReference("ketemuan");
    private FirebaseUser datauser;
    private ListViewAdapterDaftarKetemuan adapter;
    private AuthFirebase auth=new AuthFirebase(getContext());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_daftar_ketemuan_dikirim,container,false);
        datauser=auth.getUserLogin();
        lvProduct = (ListView)view.findViewById(R.id.list_ketemuan);
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().finish();
                Intent intent= new Intent(getContext(), DetailActivityKetemuan.class);
                intent.putExtra("ketemuan",daftarKetemuan.get(position));
                intent.putExtra("konfirmasi",false);
                startActivity(intent);
            }
        });
        daftarKetemuan = new ArrayList<>();


        databaseReferenceListUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarKetemuan.clear();
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    Ketemuan getUser=userSnpashot.getValue(Ketemuan.class);
                    if (datauser.getEmail().equals(getUser.getEmail_sender())){
                        daftarKetemuan.add(getUser);
                    }

                    adapter = new ListViewAdapterDaftarKetemuan(getContext(), daftarKetemuan);
                    lvProduct.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  view;
    }
}

