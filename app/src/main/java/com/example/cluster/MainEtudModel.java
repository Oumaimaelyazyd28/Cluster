package com.example.cluster;

public class MainEtudModel {

    String nom,prenom,cne,tele,date,uri,email;

    MainEtudModel(){

    }

    public MainEtudModel(String nom, String prenom, String cne, String tele, String date, String uri,String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.cne = cne;
        this.tele = tele;
        this.date = date;
        this.uri = uri;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
