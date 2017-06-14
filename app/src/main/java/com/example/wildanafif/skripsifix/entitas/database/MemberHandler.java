package com.example.wildanafif.skripsifix.entitas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wildanafif.skripsifix.entitas.Member;

import static android.provider.BaseColumns._ID;
import static com.example.wildanafif.skripsifix.entitas.database.DatabaseContract._DATABASE_VERSION;
import static com.example.wildanafif.skripsifix.entitas.database.DatabaseContract._DB_NAME;
import static com.example.wildanafif.skripsifix.entitas.database.DatabaseContract._TABLE_MEMBER;

/**
 * Created by wildan afif on 6/6/2017.
 */

public class MemberHandler extends SQLiteOpenHelper {
    private Context context;
    private static final String CREATE_TABLE_MEMBER="CREATE TABLE "+_TABLE_MEMBER+"("+_ID+" VARCHAR(255) PRIMARY KEY , member_id VARCHAR(255), name VARCHAR(255), email VARCHAR(255) , password VARCHAR(255));";
    private static final String DROP_TABLE_MEBER="DROP TABLE  IF EXISTS "+_TABLE_MEMBER;
    public MemberHandler(Context context) {
        super(context, _DB_NAME, null, _DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_TABLE_MEMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(this.DROP_TABLE_MEBER);
        onCreate(db);
    }

    public void create(Member member){

        SQLiteDatabase db = this.getWritableDatabase();
        this.onUpgrade(db,1,2);

        ContentValues values = new ContentValues();
        values.put("name", member.getNama());
        values.put("email",member.getEmail());
        values.put("password",member.getPassword());
        values.put("member_id",member.getId());
        values.put(_ID, member.getId());


        // Inserting Row
        db.insert(_TABLE_MEMBER, null, values);
        db.close(); // Closing database connection
    }

    public void create_member(Member member){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", member.getNama());
        values.put("email",member.getEmail());
        values.put("password",member.getPassword());
        values.put("member_id",member.getId());
        values.put(_ID, member.getId());


        // Inserting Row
        db.insert(_TABLE_MEMBER, null, values);
        db.close(); // Closing database connection
    }

    public Member getMember(){
        Member m=null;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+_TABLE_MEMBER,null);
        if (cursor.moveToFirst()){
            m=new Member(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        return m;
    }
    public void deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(_TABLE_MEMBER,null,null);
        db.close();
    }
}

