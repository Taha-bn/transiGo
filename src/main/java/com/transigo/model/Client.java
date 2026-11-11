package com.transigo.model;

public class Client {
    private int id ;
    private String nom;
    private String telephone;
    private String email;
    private String adress;
    private boolean actif;

    public Client(){
        this.actif = true;
    }

    public Client(int id , String nom , String telephone , String email , String adress , boolean actif){
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.adress = adress;
        this.actif = actif;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String t) { this.telephone = t; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAdresse() { return adress; }
    public void setAdresse(String adresse) { this.adress = adresse; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    @Override
    public String toString(){
        return "Nom : "+ nom + ", telephone : "+ telephone + ", email : "+email; 
    }
}