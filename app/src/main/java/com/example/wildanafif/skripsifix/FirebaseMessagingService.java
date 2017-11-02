package com.example.wildanafif.skripsifix;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.wildanafif.skripsifix.entitas.Ketemuan;
import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.ui.activity.ChatActivity;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuan;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuanDiterima;
import com.example.wildanafif.skripsifix.ui.activity.LoginActivity;
import com.example.wildanafif.skripsifix.ui.activity.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by wildan afif on 6/15/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String status=remoteMessage.getData().get("status");
        if (status.matches("request_ketemuan")){
            showNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("id_ketemuan"),remoteMessage.getData().get("nama"));
        }else if (status.matches("konfirmasi")){
            showNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("id_ketemuan"));
        }else if(status.matches("chat")){
            showNotification_message(remoteMessage.getData().get("nama"),remoteMessage.getData().get("message"),remoteMessage.getData().get("id_ketemuan"));
        }

    }

    private void showNotification(final String message, String id_ketemuan) {



        final Ketemuan[] ketemuan = new Ketemuan[1];
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("ketemuan");
        Query query =  mDatabaseReference.orderByChild("id_ketemuan").equalTo(id_ketemuan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    ketemuan[0] = userSnpashot.getValue(Ketemuan.class);
                }


                Intent i = new Intent(FirebaseMessagingService.this,DetailActivityKetemuan.class);
                i.putExtra("ketemuan", ketemuan[0]);
                i.putExtra("konfirmasi",false);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseMessagingService.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMessagingService.this)
                        .setAutoCancel(true)
                        .setContentTitle("Lokasi Transaksi di Setujui")
                        .setContentText(message)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setSound(alarmSound)
                        .setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                manager.notify(0,builder.build());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void showNotification(final String message, String id_ketemuan, String nama) {
        final Ketemuan[] ketemuan = new Ketemuan[1];
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("ketemuan");
        Query query =  mDatabaseReference.orderByChild("id_ketemuan").equalTo(id_ketemuan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    ketemuan[0] = userSnpashot.getValue(Ketemuan.class);
                }


                Intent i = new Intent(FirebaseMessagingService.this,DetailActivityKetemuanDiterima.class);
                i.putExtra("ketemuan", ketemuan[0]);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseMessagingService.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMessagingService.this)
                        .setAutoCancel(true)
                        .setContentTitle("Permintaan Transaksi")
                        .setContentText(message)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setSound(alarmSound)
                        .setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                manager.notify(0,builder.build());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void showNotification_message(final String nama, final String message, String id_ketemuan) {



        final Ketemuan[] ketemuan = new Ketemuan[1];
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("ketemuan");
        Query query =  mDatabaseReference.orderByChild("id_ketemuan").equalTo(id_ketemuan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    ketemuan[0] = userSnpashot.getValue(Ketemuan.class);
                }


                Intent i = new Intent(FirebaseMessagingService.this,ChatActivity.class);
                i.putExtra("ketemuan", ketemuan[0]);
                i.putExtra("konfirmasi",false);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseMessagingService.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMessagingService.this)
                        .setAutoCancel(true)
                        .setContentTitle(nama)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setSound(alarmSound)
                        .setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                manager.notify(0,builder.build());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
