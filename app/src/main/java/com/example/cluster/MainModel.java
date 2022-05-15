package com.example.cluster;

public class MainModel {

    String nom,module,tele,url,email;

    MainModel() {

    }

    public MainModel(String nom, String module, String tele,String url,String email) {
        this.nom = nom;
        this.module = module;
        this.tele = tele;
        this.url = url;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String prenom) {
        this.module = prenom;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
