package com.example.wildanafif.skripsifix.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.InfoMessage;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.ui.activity.ChatActivity;
import com.example.wildanafif.skripsifix.ui.adapter.ListChat;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wildan afif on 7/10/2017.
 */

public class ChatFragment extends Fragment {
    private View view;
    private ListView lv_chat;
    private ArrayList<InfoMessage> infoMessages;
    private ListChat adapter;
    private FirebaseUser firebaseUser;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chat,container,false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setTitle("Chat");
        this.lv_chat=(ListView)view.findViewById(R.id.list_chat);
        AuthFirebase authFirebase=new AuthFirebase();
        firebaseUser=authFirebase.getUserLogin();




        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("chat");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                infoMessages=new ArrayList<>();
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){

                    String string = userSnpashot.getKey();
                    String[] parts = string.split("-");
                    if (parts[0].matches(firebaseUser.getUid())||parts[1].matches(firebaseUser.getUid())){
                        InfoMessage infoMessageChat= userSnpashot.child("infomessage").getValue(InfoMessage.class);;
//                        if (infoMessageChat.getWaktu()!=0){
//
//                        }
                        String id_cari;
                        if (parts[0].matches(firebaseUser.getUid())){
                            id_cari=parts[1];
                        }else{
                            id_cari=parts[0];
                        }

                        infoMessageChat.setId_sender(id_cari);
                        infoMessages.add(infoMessageChat);
                       // Toast.makeText(getContext(),m, Toast.LENGTH_SHORT).show();
                    }


                }
                adapter = new ListChat(getContext(), infoMessages);
                lv_chat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoMessage msg= infoMessages.get(position);
                Ketemuan ketemuan= new Ketemuan(msg.getId_ketemuan(),msg.getEmail1(),msg.getEmail2());
                Intent intent= new Intent(getContext(), ChatActivity.class);
                intent.putExtra("ketemuan", ketemuan);

                startActivity(intent);
            }
        });

        return view;
    }
}
