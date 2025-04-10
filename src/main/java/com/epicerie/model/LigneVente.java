package com.epicerie.model;

public class LigneVente {
    private int id;
    private Produit produit;
    private int quantite;
    
    public LigneVente() {
        this.quantite = 1;
    }
    
    public LigneVente(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }
    
    public LigneVente(int id, Produit produit, int quantite) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public double getSousTotal() {
        return produit.getPrix() * quantite;
    }
    
    @Override
    public String toString() {
        return produit.getNom() + " x" + quantite + " (" + getSousTotal() + "€)";
    }
}