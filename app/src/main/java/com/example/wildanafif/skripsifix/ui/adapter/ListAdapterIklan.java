package com.example.wildanafif.skripsifix.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;

import java.util.List;

/**
 * Created by wildan afif on 6/10/2017.
 */

public class ListAdapterIklan extends BaseAdapter {
    Context mContext;
    private List<Iklan> mProductList;

    public ListAdapterIklan(Context mContext, List<Iklan> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_iklan, null);
        TextView judul_iklan = (TextView)v.findViewById(R.id.text_judul_iklan);
        TextView harga_iklan = (TextView)v.findViewById(R.id.text_harga_iklan   );
        ImageView image = (ImageView) v.findViewById(R.id.image_iklan);
        judul_iklan.setText(mProductList.get(position).getJudul_iklan());
        harga_iklan.setText(mProductList.get(position).getHarga());
        return v;
    }
}
