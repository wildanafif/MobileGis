package com.example.wildanafif.skripsifix.entitas;

/**
 * Created by wildan afif on 5/12/2017.
 */

public class Member {
    private String nama, email, password,telp,provinsi, daerah, instagram, pin_bb, foto;
    private String id;
    private String facebook;

    public Member(String nama, String email, String password, String telp, String provinsi, String daerah, String instagram, String pin_bb, String foto, String id, String facebook) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.telp = telp;
        this.provinsi = provinsi;
        this.daerah = daerah;
        this.instagram = instagram;
        this.pin_bb = pin_bb;
        this.foto = foto;
        this.id = id;
        this.facebook=facebook;
    }

    public Member(String nama, String email, String password, String telp, String provinsi, String daerah, String instagram, String pin_bb, String foto, String facebook) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.telp = telp;
        this.provinsi = provinsi;
        this.daerah = daerah;
        this.instagram = instagram;
        this.pin_bb = pin_bb;
        this.foto=foto;
        this.facebook=facebook;
    }

    public Member() {
    }

    public Member(String id, String nama, String email, String password) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public void setDaerah(String daerah) {
        this.daerah = daerah;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setPin_bb(String pin_bb) {
        this.pin_bb = pin_bb;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
