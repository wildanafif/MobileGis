package com.example.wildanafif.skripsifix.ui.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by wildan afif on 5/21/2017.
 */
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.entitas.Ketemuan;

import java.util.List;

public class ListViewAdapterDaftarKetemuan extends BaseAdapter {
    Context mContext;
    private List<Ketemuan> mProductList;

    public ListViewAdapterDaftarKetemuan(Context mContext, List<Ketemuan> mProductList) {
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
        View v = View.inflate(mContext, R.layout.list_daftar_ketemuan, null);
        TextView namaPemilikIklan = (TextView)v.findViewById(R.id.nama_pemilik_iklan);
        TextView judulIklan = (TextView)v.findViewById(R.id.judul_iklan_list   );
        TextView status = (TextView) v.findViewById(R.id.status_konfirmasi);

        //Set text for TextView
        namaPemilikIklan.setText(mProductList.get(position).getIklan().getNama());
        judulIklan.setText(mProductList.get(position).getIklan().getJudul_iklan());
        boolean status_konfirmasi=mProductList.get(position).isConfirmReceiver();
        if (status_konfirmasi){
            status.setText("Disetujui");
            status.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.ketemuan_disetujui) );
        }else{
            status.setText("Belum di Setujui/Ditolak");
            status.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.iklan_belum_disetujui) );
        }


        //Save product id to tag
        v.setTag(mProductList.get(position).getEmail_receiver());

        return v;
    }
}
