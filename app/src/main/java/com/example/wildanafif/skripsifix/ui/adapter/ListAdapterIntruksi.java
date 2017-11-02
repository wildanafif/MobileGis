package com.example.wildanafif.skripsifix.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;

import java.util.List;

/**
 * Created by wildan afif on 8/8/2017.
 */

public class ListAdapterIntruksi extends BaseAdapter {
    Context mContext;
    private List<String> intruksi;

    public ListAdapterIntruksi(Context mContext, List<String> intruksi) {
        this.mContext = mContext;
        this.intruksi = intruksi;
    }

    @Override
    public int getCount() {
        return this.intruksi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.intruksi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_panduan_lokasi, null);
        TextView text_intruksi = (TextView)v.findViewById(R.id.text_instruksi);
        String intruksi_jalan;
        if (position==0){
            intruksi_jalan="Dari lokasi anda saat ini menuju "+this.intruksi.get(position);
        }else{
            intruksi_jalan=this.intruksi.get(position);
        }
        text_intruksi.setText(Html.fromHtml(intruksi_jalan));
        return v;
    }
}
