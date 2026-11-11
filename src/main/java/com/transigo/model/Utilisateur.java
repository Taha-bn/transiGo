package com.transigo.model;

public class Utilisateur {
    private int id ;
    private String nom;
    private String email;
    private String motPasseHash;
    private Role role;
    private boolean actif;

    public Utilisateur(){
        this.actif = true;
    }

    public Utilisateur(int id , String nom , String email , String motPasseHash , Role role , boolean actif){
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motPasseHash = motPasseHash;
        this.role = role;
        this.actif = actif;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public int getNom(){ return nom;}
    public void setNom(String nom){ this.nom = nom;}

    public int getEmail(){ return email;}
    public void setEmail(String email){ this.email = email;}

    public int getmotPasseHash(){ return motPasseHash;}
    public void setmotPasseHash(String motPasseHash){ this.motPasseHash = motPasseHash;}

    public int getRole(){ return role;}
    public void setRole(Role role){ this.role = role;}

    public int getActif(){ return actif;}
    public void setActif(boolean actif){ this.actif = actif;}

    public boolean estAdministrateur(){
        return role == Role.ADMINISTRATEUR;
    }

    @Override
    public String toString(){
        return nom +" (" + role + ")";
    }

}