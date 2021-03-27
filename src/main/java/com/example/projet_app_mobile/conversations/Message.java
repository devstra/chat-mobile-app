package com.example.projet_app_mobile.conversations;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String msg;
    private String auteur;
    @ServerTimestamp
    private Timestamp timestamp;
    private String media;

    public Message() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media){
        this.media = media;
    }

    public boolean hasMedia(){
        return media == "";
    }
}
