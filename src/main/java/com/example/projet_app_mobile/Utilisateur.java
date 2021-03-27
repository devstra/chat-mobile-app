package com.example.projet_app_mobile;

public class Utilisateur {
    private String username;
    private String email;
    private String uid;

    private static final Utilisateur user = new Utilisateur();

    public Utilisateur(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }

    public Utilisateur() {
    }

    public String getUsername() {
        return username;
    }

    public static Utilisateur getInstance() {
        return user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
