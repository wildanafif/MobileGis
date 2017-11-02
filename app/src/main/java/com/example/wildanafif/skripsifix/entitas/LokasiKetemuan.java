package com.example.wildanafif.skripsifix.entitas;

import java.io.Serializable;

/**
 * Created by wildan afif on 6/13/2017.
 */

public class LokasiKetemuan implements Serializable {
    private double latitude;
    private double longitude;
    private String alamat;
    private String id_lokasi_ketemuan;
    private String id_ketemuan;


    public LokasiKetemuan(double latitude, double longitude, String alamat) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
    }

    public LokasiKetemuan(String id_lokasi_ketemuan,double latitude, double longitude, String alamat, String id_ketemuan) {
        this.id_lokasi_ketemuan=id_lokasi_ketemuan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
        this.id_ketemuan = id_ketemuan;
    }

    public LokasiKetemuan() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getId_lokasi_ketrmuan() {
        return id_lokasi_ketemuan;
    }

    public void setId_lokasi_ketrmuan(String id_lokasi_ketrmuan) {
        this.id_lokasi_ketemuan = id_lokasi_ketrmuan;
    }

    public String getId_ketemuan() {
        return id_ketemuan;
    }

    public void setId_ketemuan(String id_ketemuan) {
        this.id_ketemuan = id_ketemuan;
    }
}
