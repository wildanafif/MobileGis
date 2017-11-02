package com.example.wildanafif.skripsifix.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.ChatMessage;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.google.android.gms.vision.text.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wildan afif on 6/18/2017.
 */

public class ListChatMessage extends BaseAdapter {
    private Context context;
    private List<ChatMessage> chat;
    private String mail_login;

    public ListChatMessage(Context context, List<ChatMessage> chat, String mail_login) {
        this.context = context;
        this.chat = chat;
        this.mail_login=mail_login;


    }

    @Override
    public int getCount() {
        return this.chat.size();
    }

    @Override
    public Object getItem(int position) {
        return chat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.list_chat_message, null);
        LinearLayout layout1=(LinearLayout)v.findViewById(R.id.layout1);
        LinearLayout layout2=(LinearLayout)v.findViewById(R.id.layout2);
        TextView text1= (TextView)v.findViewById(R.id.text1);
        TextView text2= (TextView)v.findViewById(R.id.text2);

        if (chat.get(position).getEmail_pengirim().matches(mail_login)){
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            text1.setText(this.chat.get(position).getMessage());
        }else{
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            text2.setText(this.chat.get(position).getMessage());
        }
        return v;
    }
}
