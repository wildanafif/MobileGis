package com.example.wildanafif.skripsifix.entitas;

/**
 * Created by wildan afif on 6/18/2017.
 */

public class InfoMessage {
    private String id_chat;
    private String id_ketemuan;
    private String temp_chat;
    private String id_sender;
    private long waktu;
    private String url_gambar_user;
    private String email1;
    private String email2;

    public InfoMessage(String id_chat, String id_ketemuan, String temp_chat, String email1, String email2) {
        this.id_chat = id_chat;
        this.id_ketemuan = id_ketemuan;
        this.temp_chat = temp_chat;
        this.email1=email1;
        this.email2=email2;

    }


    public InfoMessage() {
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getUrl_gambar_user() {
        return url_gambar_user;
    }

    public void setUrl_gambar_user(String url_gambar_user) {
        this.url_gambar_user = url_gambar_user;
    }

    public String getId_chat() {
        return id_chat;
    }

    public void setId_chat(String id_chat) {
        this.id_chat = id_chat;
    }

    public String getId_ketemuan() {
        return id_ketemuan;
    }

    public void setId_ketemuan(String id_ketemuan) {
        this.id_ketemuan = id_ketemuan;
    }

    public String getTemp_chat() {
        return temp_chat;
    }

    public void setTemp_chat(String temp_chat) {
        this.temp_chat = temp_chat;
    }

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }

    public long getWaktu() {
        return waktu;
    }

    public void setWaktu(long waktu) {
        this.waktu = waktu;
    }
}
