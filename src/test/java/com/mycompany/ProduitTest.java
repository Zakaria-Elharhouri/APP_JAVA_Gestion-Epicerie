
package com.mycompany;

import com.epicerie.model.Produit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {

    @Test
    void testConstructeurSansParametres() {
        Produit produit = new Produit();
        assertEquals(0, produit.getId());
        assertNull(produit.getNom()); 
        assertEquals(0.0, produit.getPrix()); 
    }

    @Test
    void testConstructeurAvecIdNomPrix() {
        Produit produit = new Produit(1, "Produit Test", 9.99);

        assertEquals(1, produit.getId());
        assertEquals("Produit Test", produit.getNom());
        assertEquals(9.99, produit.getPrix());
    }

    @Test
    void testConstructeurAvecNomPrix() {
        Produit produit = new Produit("Produit Test", 5.99);

        assertEquals(0, produit.getId()); 
        assertEquals("Produit Test", produit.getNom());
        assertEquals(5.99, produit.getPrix());
    }

    @Test
    void testSettersEtGetters() {
        Produit produit = new Produit();

        produit.setId(10);
        produit.setNom("Produit Modifié");
        produit.setPrix(15.49);

        assertEquals(10, produit.getId());
        assertEquals("Produit Modifié", produit.getNom());
        assertEquals(15.49, produit.getPrix());
    }

    @Test
    void testToString() {
        Produit produit = new Produit(1, "Produit Test", 19.99);

        String result = produit.toString();
        assertEquals("Produit Test - 19.99€", result);
    }
}