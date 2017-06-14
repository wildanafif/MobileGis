package com.example.wildanafif.skripsifix.entitas;

import java.io.Serializable;

/**
 * Created by wildan afif on 5/21/2017.
 */

public class Ketemuan implements Serializable {
    private String id_ketemuan;
    private String email_sender;
    private String email_receiver;
    private String waktu;
    private String tempat_ketemuan;
    private double latitude;
    private double longitude;
    private String message;
    private Iklan iklan;
    private boolean confirmReceiver;
    private double latitude_sender;
    private double longitude_sender;

    private double latitude_receiver;
    private double longitude_receiver;

    public Ketemuan(String id_ketemuan, String email_sender, String email_receiver, String waktu, String tempat_ketemuan, double latitude, double longitude, String message, Iklan iklan, boolean confirmReceiver, double latitude_sender, double longitude_sender, double latitude_receiver, double longitude_receiver) {
        this.id_ketemuan = id_ketemuan;
        this.email_sender = email_sender;
        this.email_receiver = email_receiver;
        this.waktu = waktu;
        this.tempat_ketemuan = tempat_ketemuan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.iklan = iklan;
        this.confirmReceiver = confirmReceiver;
        this.latitude_sender = latitude_sender;
        this.longitude_sender = longitude_sender;
        this.latitude_receiver = latitude_receiver;
        this.longitude_receiver = longitude_receiver;
    }

    public Ketemuan(String email_sender, String email_receiver, String waktu, String tempat_ketemuan, double latitude, double longitude, String message, Iklan iklan, boolean confirmReceiver) {
        this.email_sender = email_sender;
        this.email_receiver = email_receiver;
        this.waktu = waktu;
        this.tempat_ketemuan = tempat_ketemuan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.iklan = iklan;
        this.confirmReceiver=confirmReceiver;
    }

    public Ketemuan(String id_ketemuan, String email_sender, String email_receiver, String waktu, String tempat_ketemuan, double latitude, double longitude, String message, Iklan iklan, boolean confirmReceiver) {
        this.id_ketemuan = id_ketemuan;
        this.email_sender = email_sender;
        this.email_receiver = email_receiver;
        this.waktu = waktu;
        this.tempat_ketemuan = tempat_ketemuan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.iklan = iklan;
        this.confirmReceiver = confirmReceiver;
    }

    public Ketemuan() {
    }

    public double getLatitude_sender() {
        return latitude_sender;
    }

    public void setLatitude_sender(double latitude_sender) {
        this.latitude_sender = latitude_sender;
    }

    public double getLongitude_sender() {
        return longitude_sender;
    }

    public void setLongitude_sender(double longitude_sender) {
        this.longitude_sender = longitude_sender;
    }

    public double getLatitude_receiver() {
        return latitude_receiver;
    }

    public void setLatitude_receiver(double latitude_receiver) {
        this.latitude_receiver = latitude_receiver;
    }

    public double getLongitude_receiver() {
        return longitude_receiver;
    }

    public void setLongitude_receiver(double longitude_receiver) {
        this.longitude_receiver = longitude_receiver;
    }

    public String getId_ketemuan() {
        return id_ketemuan;
    }

    public void setId_ketemuan(String id_ketemuan) {
        this.id_ketemuan = id_ketemuan;
    }

    public String getEmail_sender() {
        return email_sender;
    }

    public void setEmail_sender(String email_sender) {
        this.email_sender = email_sender;
    }

    public String getEmail_receiver() {
        return email_receiver;
    }

    public void setEmail_receiver(String email_receiver) {
        this.email_receiver = email_receiver;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTempat_ketemuan() {
        return tempat_ketemuan;
    }

    public void setTempat_ketemuan(String tempat_ketemuan) {
        this.tempat_ketemuan = tempat_ketemuan;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Iklan getIklan() {
        return iklan;
    }

    public void setIklan(Iklan iklan) {
        this.iklan = iklan;
    }

    public boolean isConfirmReceiver() {
        return confirmReceiver;
    }

    public void setConfirmReceiver(boolean confirmReceiver) {
        this.confirmReceiver = confirmReceiver;
    }
}
