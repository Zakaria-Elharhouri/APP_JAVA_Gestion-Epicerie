
package com.mycompany;

import com.epicerie.model.LigneVente;
import com.epicerie.model.Produit;
import com.epicerie.model.Vente;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class VenteTest {

    @Test
    void testConstructeurSansParametres() {
        Vente vente = new Vente();
        assertEquals(0, vente.getId());
        assertNotNull(vente.getDate());
        assertEquals(0.0, vente.getMontantTotal());
        assertNotNull(vente.getLignesVente());
        assertEquals(0, vente.getLignesVente().size());
    }

    @Test
    void testConstructeurAvecParametres() {
        Date date = new Date();
        Vente vente = new Vente(1, date, 100.0);

        assertEquals(1, vente.getId());
        assertEquals(date, vente.getDate());
        assertEquals(100.0, vente.getMontantTotal());
        assertNotNull(vente.getLignesVente());
        assertEquals(0, vente.getLignesVente().size());
    }

    @Test
    void testSettersEtGetters() {
        Vente vente = new Vente();
        Date date = new Date();
        ArrayList<LigneVente> lignes = new ArrayList<>();

        vente.setId(10);
        vente.setDate(date);
        vente.setMontantTotal(50.0);
        vente.setLignesVente(lignes);

        assertEquals(10, vente.getId());
        assertEquals(date, vente.getDate());
        assertEquals(50.0, vente.getMontantTotal());
        assertEquals(lignes, vente.getLignesVente());
    }

    @Test
    void testAjouterLigneVente() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(produit, 3);
        Vente vente = new Vente();

        vente.ajouterLigneVente(ligneVente);

        assertEquals(1, vente.getLignesVente().size());
        assertEquals(15.0, vente.getMontantTotal()); // 5 * 3
    }

    @Test
    void testRetirerLigneVente() {
        Produit produit = new Produit(1, "Produit Test", 5.0);
        LigneVente ligneVente = new LigneVente(produit, 3);
        Vente vente = new Vente();

        vente.ajouterLigneVente(ligneVente);
        vente.retirerLigneVente(ligneVente);

        assertEquals(0, vente.getLignesVente().size());
        assertEquals(0.0, vente.getMontantTotal());
    }

    @Test
    void testCalculerTotal() {
        Produit produit1 = new Produit(1, "Produit 1", 5.0);
        Produit produit2 = new Produit(2, "Produit 2", 10.0);
        LigneVente ligne1 = new LigneVente(produit1, 2); 
        LigneVente ligne2 = new LigneVente(produit2, 3); 
        Vente vente = new Vente();

        vente.ajouterLigneVente(ligne1);
        vente.ajouterLigneVente(ligne2);
        vente.calculerTotal();

        assertEquals(40.0, vente.getMontantTotal());
    }
}