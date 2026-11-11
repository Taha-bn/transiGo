package com.transigo.model;
import java.time.LocalDate;

public class CommandeTransport{
    private int id;
    private String reference;
    private LocalDate dateCreation;
    private String adresseDepart;
    private String adresseArrivee;
    private String natureMarchandise;
    private double poidsKg;
    private LocalDate dateSouhaitee;
    private StatusCommande statut;
    private int clientId;
    private int utilisateurId;

    public CommandeTransport(){
        this.dateCreation = LocalDate.now();
        this.statut = StatusCommande.EN_ATTENTE;
    }

    public CommandeTransport(int id, String reference , LocalDate dateCreation,
                             String adresseDepart, String adresseArrivee, String natureMarchandise,
                             double poidsKg, LocalDate dateSouhaitee, StatusCommande statut,
                             int clientId, int utilisateurId){
        this.id = id;
        this.reference = reference;
        this.dateCreation = dateCreation;
        this.adresseDepart = adresseDepart;
        this.adresseArrivee = adresseArrivee;
        this.natureMarchandise = natureMarchandise;
        setPoidsKg(poidsKg);
        this.dateSouhaitee = dateSouhaitee;
        this.statut = statut;
        this.clientId = clientId;
        this.utilisateurId = utilisateurId;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getReference() { return reference; }
    public void setReference(String r) { this.reference = r; }
    
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate d) { this.dateCreation = d; }
    
    public String getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(String a) { this.adresseDepart = a; }
    
    public String getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(String a) { this.adresseArrivee = a; }
    
    public String getNatureMarchandise() { return natureMarchandise; }
    public void setNatureMarchandise(String n) { this.natureMarchandise = n; }
    
    public double getPoidsKg() { return poidsKg; }
    public void setPoidsKg(double poidsKg) {
        if (poidsKg <= 0) {
            throw new IllegalArgumentException("Le poids doit etre strictement positif!");
        }
        this.poidsKg = poidsKg;
    }

    public LocalDate getDateSouhaitee() { return dateSouhaitee; }
    public void setDateSouhaitee(LocalDate d) { this.dateSouhaitee = d; }
    
    public StatusCommande getStatut() { return statut; }
    public void setStatut(StatusCommande statut) { this.statut = statut; }
    
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int id) { this.utilisateurId = id; }

    @Override 
    public String toString(){
        return reference + "[ "+ statut + " ]";
    }
}