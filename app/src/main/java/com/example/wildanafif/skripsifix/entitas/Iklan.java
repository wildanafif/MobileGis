package com.example.wildanafif.skripsifix.entitas;

import java.io.Serializable;

/**
 * Created by wildan afif on 5/14/2017.
 */

public class Iklan implements Serializable {
    private String id_iklan;
    private double latitude;
    private double longitude;
    private String judul_iklan;
    private String kategori;
    private String sub_kategori;
    private String deskripsi_iklan;
    private String harga;
    private String provinsi;
    private String nama;
    private String telp;
    private String pin_bb;
    private String facebook;
    private String instagram;
    private String waktu;
    private int nego;
    private String daerah;
    private String temp_foto;
    private String mail;
    private int user_register;
    private String id_user;
    private String kondisi;
    private int dilihat;
    private int wa;
    private String tgl;

    public Iklan() {
    }

    public Iklan(String id_iklan, double latitude, double longitude, String judul_iklan, String kategori, String sub_kategori, String deskripsi_iklan, String harga, String provinsi, String nama, String telp, String pin_bb, String facebook, String instagram, String waktu, int nego, String daerah, String temp_foto, String mail, int user_register, String id_user, String kondisi, int dilihat, int wa, String tgl) {
        this.id_iklan = id_iklan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.judul_iklan = judul_iklan;
        this.kategori = kategori;
        this.sub_kategori = sub_kategori;
        this.deskripsi_iklan = deskripsi_iklan;
        this.harga = harga;
        this.provinsi = provinsi;
        this.nama = nama;
        this.telp = telp;
        this.pin_bb = pin_bb;
        this.facebook = facebook;
        this.instagram = instagram;
        this.waktu = waktu;
        this.nego = nego;
        this.daerah = daerah;
        this.temp_foto = temp_foto;
        this.mail = mail;
        this.user_register = user_register;
        this.id_user = id_user;
        this.kondisi = kondisi;
        this.dilihat = dilihat;
        this.wa = wa;
        this.tgl = tgl;
    }

    public String getId_iklan() {
        return id_iklan;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getJudul_iklan() {
        return judul_iklan;
    }

    public String getKategori() {
        return kategori;
    }

    public String getSub_kategori() {
        return sub_kategori;
    }

    public String getDeskripsi_iklan() {
        return deskripsi_iklan;
    }

    public String getHarga() {
        return harga;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public String getNama() {
        return nama;
    }

    public String getTelp() {
        return telp;
    }

    public String getPin_bb() {
        return pin_bb;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getWaktu() {
        return waktu;
    }

    public int getNego() {
        return nego;
    }

    public String getDaerah() {
        return daerah;
    }

    public String getTemp_foto() {
        return temp_foto;
    }

    public String getMail() {
        return mail;
    }

    public int getUser_register() {
        return user_register;
    }

    public String getId_user() {
        return id_user;
    }

    public String getKondisi() {
        return kondisi;
    }

    public int getDilihat() {
        return dilihat;
    }

    public int getWa() {
        return wa;
    }

    public String getTgl() {
        return tgl;
    }
}
