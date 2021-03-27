package com.example.projet_app_mobile.channels;

public class Channel {
    private String nomChannel;
    private String description;
    private int nbConnecter;

    public Channel(){}

    public Channel(String nomChannel,String description,int nbConnecter){
        this.description = description;
        this.nomChannel = nomChannel;
        this.nbConnecter = nbConnecter;
    }

    public String getNomChannel() {
        return nomChannel;
    }

    public String getDescription() {
        return description;
    }

    public int getNbConnecter() {
        return nbConnecter;
    }
}
