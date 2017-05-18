package com.example.wildanafif.skripsifix.entitas.volley;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.wildanafif.skripsifix.R;

/**
 * Created by wildan afif on 5/18/2017.
 */

public class Image {
    private Context context;
    private String urlImage;
    private NetworkImageView img;
    private ImageLoader mImageLoader;

    public Image(Context context, String urlImage, NetworkImageView image) {
        this.context = context;
        this.urlImage = urlImage;
        this.img = image;
    }

    public void showImage(){
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.context).getImageLoader();
        mImageLoader.get(this.urlImage, ImageLoader.getImageListener(img, R.drawable.loading, R.mipmap.ic_launcher));
        img.setImageUrl(this.urlImage,mImageLoader);
    }

    public void setImg(NetworkImageView img) {
        this.img = img;
    }
}
