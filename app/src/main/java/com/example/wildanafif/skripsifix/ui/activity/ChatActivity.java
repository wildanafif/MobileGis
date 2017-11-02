package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.control.ChatControl;
import com.example.wildanafif.skripsifix.entitas.ChatMessage;
import com.example.wildanafif.skripsifix.entitas.InfoMessage;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.adapter.ListChatMessage;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ChatMessage> chatMessages;
    private ListView lv_chat;
    private ListChatMessage adapter;
    private String id_chat_message;
    DatabaseReference database_member = FirebaseDatabase.getInstance().getReference("members");

    DatabaseReference mDatabaseReference ;
    DatabaseReference mDatabaseReference_chatMessage ;
    private Ketemuan ketemuan;
    private Intent intent;
    private int banyak_data=0;
    private Member member_login;
    private FirebaseUser firebaseUser;
    Loading loading= new Loading(this);
    private ImageView btn_send_message;
    private EditText et_text_message;
    private Member cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        ketemuan= (Ketemuan) intent.getSerializableExtra("ketemuan");
        AuthFirebase authFirebase=new AuthFirebase();
        firebaseUser=authFirebase.getUserLogin();
        getDataSenderLogin();

        this.chatMessages=new ArrayList<>();
        lv_chat=(ListView)findViewById(R.id.list_chat_message);

        adapter = new ListChatMessage(ChatActivity.this, this.chatMessages,firebaseUser.getEmail());
        this.lv_chat.setAdapter(adapter);


        btn_send_message=(ImageView)findViewById(R.id.send_message);
        btn_send_message.setOnClickListener(this);

        et_text_message=(EditText)findViewById(R.id.textMessage);
        this.et_text_message.setOnClickListener(this);

    }



    private void getDataSenderLogin() {

        loading.show();
        Query query =  database_member.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member_login = null;
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    member_login = userSnpashot.getValue(Member.class);
                }


                setIdreference();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }

    private void setIdreference() {
        String email_cari;


        if (ketemuan.getEmail_sender().matches(firebaseUser.getEmail())){
            email_cari=ketemuan.getEmail_receiver();
        }else {
            email_cari=ketemuan.getEmail_sender();
        }



        Query query =  database_member.orderByChild("email").equalTo(email_cari);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cari = null;
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    cari = userSnpashot.getValue(Member.class);
                }

                getSupportActionBar().setTitle(cari.getNama());

                loading.hide();

                if (cari.getTimestamp()>member_login.getTimestamp()){
                    id_chat_message=member_login.getId()+"-"+cari.getId();
                }else{
                    id_chat_message=cari.getId()+"-"+member_login.getId();
                }
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("chat").child(id_chat_message).child("infomessage");
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("chat");
                final Member finalCari = cari;
                final Member finalCari1 = cari;
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.hasChild(id_chat_message)) {
                            mDatabaseReference.setValue(new InfoMessage(id_chat_message,ketemuan.getId_ketemuan(),"",member_login.getEmail(), finalCari1.getEmail()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabaseReference_chatMessage=FirebaseDatabase.getInstance().getReference("chat").child(id_chat_message).child("list_chat");
                show_message();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void show_message(){

        Query query = mDatabaseReference_chatMessage.orderByChild("timestamp");
        mDatabaseReference_chatMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ChatMessage> set= new ArrayList<>();
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    ChatMessage chat = userSnpashot.getValue(ChatMessage.class);
                    set.add(chat);
                }
                chatMessages.clear();
                chatMessages.addAll(set);
                adapter.notifyDataSetChanged();
                banyak_data=set.size()-1;
                lv_chat.smoothScrollToPosition(banyak_data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_message:
                if (!et_text_message.getText().toString().matches("")) {
                   send();
                }
                break;
            case R.id.textMessage:

                lv_chat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_chat.setSelection(banyak_data);
                        lv_chat.smoothScrollToPosition(banyak_data);
                    }
                }, 200);
                break;

        }
    }

    private void send() {
        String IdChat = mDatabaseReference_chatMessage.push().getKey();
        mDatabaseReference_chatMessage.child(IdChat).setValue(new ChatMessage(member_login.getNama(), member_login.getEmail(), this.et_text_message.getText().toString(), new Timestamp(System.currentTimeMillis()).getTime()));
        DatabaseReference updatDb = FirebaseDatabase.getInstance().getReference("chat").child(id_chat_message).child("infomessage");
        updatDb.child("temp_chat").setValue(this.et_text_message.getText().toString());
        updatDb.child("waktu").setValue(new Timestamp(System.currentTimeMillis()).getTime());

        ChatControl chatControl= new ChatControl(ChatActivity.this);
        chatControl.sendMessage(member_login.getNama(),ketemuan.getId_ketemuan(),cari.getEmail(),et_text_message.getText().toString());
        et_text_message.setText("");
        banyak_data++;
    }
}
