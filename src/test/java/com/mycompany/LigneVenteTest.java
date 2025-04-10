
package com.mycompany;

import com.epicerie.model.LigneVente;
import com.epicerie.model.Produit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LigneVenteTest {

    @Test
    void testConstructeurSansParametres() {
        LigneVente ligneVente = new LigneVente();
        assertEquals(1, ligneVente.getQuantite());
        assertNull(ligneVente.getProduit());
        assertEquals(0, ligneVente.getId());
    }

    @Test
    void testConstructeurAvecProduitEtQuantite() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(produit, 3);

        assertEquals(produit, ligneVente.getProduit());
        assertEquals(3, ligneVente.getQuantite());
        assertEquals(0, ligneVente.getId()); 
    }

    @Test
    void testConstructeurAvecIdProduitEtQuantite() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(10, produit, 3);

        assertEquals(10, ligneVente.getId());
        assertEquals(produit, ligneVente.getProduit());
        assertEquals(3, ligneVente.getQuantite());
    }

    @Test
    void testSettersEtGetters() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente();

        ligneVente.setId(20);
        ligneVente.setProduit(produit);
        ligneVente.setQuantite(4);

        assertEquals(20, ligneVente.getId());
        assertEquals(produit, ligneVente.getProduit());
        assertEquals(4, ligneVente.getQuantite());
    }

    @Test
    void testGetSousTotal() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(produit, 3);

        double sousTotal = ligneVente.getSousTotal();
        assertEquals(15.0, sousTotal); 
    }

    @Test
    void testToString() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(produit, 3);

        String result = ligneVente.toString();
        assertEquals("Produit Test x3 (15.0€)", result);
    }
}