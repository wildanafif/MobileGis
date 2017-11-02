package com.example.wildanafif.skripsifix.entitas.firebase;

import android.content.Context;
import android.content.Intent;

import com.example.wildanafif.skripsifix.entitas.Member;
import com.example.wildanafif.skripsifix.entitas.ui.Loading;
import com.example.wildanafif.skripsifix.ui.activity.DetailActivityKetemuanDiterima;
import com.example.wildanafif.skripsifix.ui.activity.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wildan afif on 6/15/2017.
 */

public class MemberFirebase {
    DatabaseReference database_member = FirebaseDatabase.getInstance().getReference("members");
    private Context context;

    public MemberFirebase(Context context) {
        this.context = context;
    }

    public void getMemberEmail(final DetailActivityKetemuanDiterima detail, String email){
        final Loading loading= new Loading(this.context);
        loading.show();

        Query query =  database_member.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = null;
                for (DataSnapshot userSnpashot : dataSnapshot.getChildren()){
                    member = userSnpashot.getValue(Member.class);
                }

                loading.hide();
                detail.setMember(member);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void update(Member member){
        database_member.child(member.getId()).setValue(member);
        Intent intent= new Intent(context, MainActivity.class);
        intent.putExtra("fragment","profil");
        context.startActivity(intent);
    }

}
