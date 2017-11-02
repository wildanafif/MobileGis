package com.example.wildanafif.skripsifix.ui.fragment;

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
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuanDiterima;
import com.example.wildanafif.skripsifix.ui.adapter.ListViewAdapterDaftarKetemuan;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wildan afif on 5/21/2017.
 */

public class Fragment_DaftarKetemuanDiterima  extends Fragment {
    private ListView lvListKetemuan;
    private ArrayList<Ketemuan> daftarKetemuan;
    private DatabaseReference databaseReferenceListUsers= FirebaseDatabase.getInstance().getReference("ketemuan");
    private FirebaseUser datauser;
    private ListViewAdapterDaftarKetemuan adapter;
    private View view;
    private AuthFirebase auth=new AuthFirebase(getContext());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daftar_ketemuan_diterima, container, false);
        lvListKetemuan = (ListView)view.findViewById(R.id.list_ketemuan);
        lvListKetemuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().finish();
                Intent intent= new Intent(getContext(), DetailActivityKetemuanDiterima.class);
                intent.putExtra("ketemuan", daftarKetemuan.get(position));
                intent.putExtra("konfirmasi",true);
                startActivity(intent);
            }
        });
        daftarKetemuan = new ArrayList<>();
        datauser=auth.getUserLogin();
        Query query =  databaseReferenceListUsers.orderByChild("email_receiver").equalTo(datauser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarKetemuan.clear();
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    Ketemuan getUser=userSnpashot.getValue(Ketemuan.class);
                    daftarKetemuan.add(getUser);


                }
                adapter = new ListViewAdapterDaftarKetemuan("receiver",getContext(), daftarKetemuan);
                lvListKetemuan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}

