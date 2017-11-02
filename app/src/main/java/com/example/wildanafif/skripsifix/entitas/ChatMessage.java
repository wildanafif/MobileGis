package com.example.wildanafif.skripsifix.entitas;

/**
 * Created by wildan afif on 6/18/2017.
 */

public class ChatMessage {
    private String nama_pengirim;
    private String email_pengirim;
    private String message;
    private long timestamp;

    public ChatMessage(String nama_pengirim, String email_pengirim, String message, long timestamp) {
        this.nama_pengirim = nama_pengirim;
        this.email_pengirim = email_pengirim;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }

    public String getNama_pengirim() {
        return nama_pengirim;
    }

    public void setNama_pengirim(String nama_pengirim) {
        this.nama_pengirim = nama_pengirim;
    }

    public String getEmail_pengirim() {
        return email_pengirim;
    }

    public void setEmail_pengirim(String email_pengirim) {
        this.email_pengirim = email_pengirim;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
