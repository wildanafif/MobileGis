package com.example.wildanafif.skripsifix.entitas;

/**
 * Created by wildan afif on 5/12/2017.
 */

public class Member {
    private String nama, email, password,telp,provinsi, daerah, instagram, pin_bb, foto;

    public Member(String nama, String email, String password, String telp, String provinsi, String daerah, String instagram, String pin_bb,String foto) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.telp = telp;
        this.provinsi = provinsi;
        this.daerah = daerah;
        this.instagram = instagram;
        this.pin_bb = pin_bb;
        this.foto=foto;
    }

    public Member() {
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelp() {
        return telp;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public String getDaerah() {
        return daerah;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getPin_bb() {
        return pin_bb;
    }

    public String getFoto() {
        return foto;
    }
}
