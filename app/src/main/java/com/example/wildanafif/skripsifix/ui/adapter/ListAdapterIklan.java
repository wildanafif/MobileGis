package com.example.wildanafif.skripsifix.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Iklan;
import com.example.wildanafif.skripsifix.entitas.volley.Image;

import java.util.List;

/**
 * Created by wildan afif on 6/10/2017.
 */

public class ListAdapterIklan extends BaseAdapter {
    Context mContext;
    private List<Iklan> iklanList;

    public ListAdapterIklan(Context mContext, List<Iklan> iklanList) {
        this.mContext = mContext;
        this.iklanList = iklanList;
    }

    @Override
    public int getCount() {
        return iklanList.size();
    }

    @Override
    public Object getItem(int position) {
        return iklanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_iklan, null);
        TextView judul_iklan = (TextView)v.findViewById(R.id.text_judul_iklan);
        TextView harga_iklan = (TextView)v.findViewById(R.id.text_harga_iklan);
        String uri= iklanList.get(position).getTemp_foto().replace(" ","%20");
        NetworkImageView image = (NetworkImageView) v.findViewById(R.id.image_iklan);
        Image image_dowload=new Image(this.mContext,"https://www.otomotifstore.com/assets/"+uri,image);
        image_dowload.showImage();
        judul_iklan.setText(iklanList.get(position).getJudul_iklan());
        harga_iklan.setText(iklanList.get(position).getHarga());
        return v;
    }
}
