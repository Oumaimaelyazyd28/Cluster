package com.example.cluster;

public class ReadWriteUserDetails {

    public String nom,prenom,cne,tele,date,email,uri;

    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String nom, String prenom, String cne,  String date,String tele, String email,String uri) {
        this.nom = nom;
        this.prenom = prenom;
        this.cne = cne;
        this.tele = tele;
        this.date = date;
        this.email = email;
        this.uri = uri;
    }
}
