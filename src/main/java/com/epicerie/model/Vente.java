package com.epicerie.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vente {
    private int id;
    private Date date;
    private double montantTotal;
    private List<LigneVente> lignesVente;
    
    public Vente() {
        this.date = new Date();
        this.montantTotal = 0.0;
        this.lignesVente = new ArrayList<>();
    }
    
    public Vente(int id, Date date, double montantTotal) {
        this.id = id;
        this.date = date;
        this.montantTotal = montantTotal;
        this.lignesVente = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public List<LigneVente> getLignesVente() {
        return lignesVente;
    }
    
    public void setLignesVente(List<LigneVente> lignesVente) {
        this.lignesVente = lignesVente;
    }
    
    public void ajouterLigneVente(LigneVente ligneVente) {
        this.lignesVente.add(ligneVente);
        calculerTotal();
    }
    
    public void retirerLigneVente(LigneVente ligneVente) {
        this.lignesVente.remove(ligneVente);
        calculerTotal();
    }
    
    public void calculerTotal() {
        this.montantTotal = 0.0;
        for (LigneVente ligne : lignesVente) {
            this.montantTotal += ligne.getProduit().getPrix() * ligne.getQuantite();
        }
    }
}