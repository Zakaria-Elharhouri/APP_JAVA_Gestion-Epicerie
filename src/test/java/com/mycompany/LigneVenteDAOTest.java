package com.mycompany;

import com.epicerie.dao.LigneVenteDAO;
import com.epicerie.dao.ProduitDAO;
import com.epicerie.model.LigneVente;
import com.epicerie.model.Produit;
import com.epicerie.util.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LigneVenteDAOTest {

    private static Connection connection;
    private LigneVenteDAO ligneVenteDAO;
    private ProduitDAO produitDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException, ClassNotFoundException {
       
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        connection = DriverManager.getConnection(url, "sa", "");

       
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE PRODUIT (id_produit INT PRIMARY KEY, nom VARCHAR(255), prix DOUBLE);");
            stmt.execute("CREATE TABLE LIGNEVENTE (id_ligne INT PRIMARY KEY AUTO_INCREMENT, id_produit INT, id_vente INT, quantite INT);");
        }

        DBConnection.setConnection(connection); 
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP ALL OBJECTS;"); 
            }
            connection.close();
        }
    }

    @BeforeEach
    void setUp() {
        ligneVenteDAO = new LigneVenteDAO();
        produitDAO = new ProduitDAO();

        
        ligneVenteDAO.produitDAO = produitDAO;
    }

    @Test
    void testAjouterLigneVente() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO PRODUIT (id_produit, nom, prix) VALUES (1, 'Produit Test', 10.5);");
        }

        Produit produit = produitDAO.trouverParId(1); 
        LigneVente ligneVente = new LigneVente(0, produit, 3);
        boolean result = ligneVenteDAO.ajouter(ligneVente, 2);

        assertTrue(result);
        assertTrue(ligneVente.getId() > 0); 
    }

    @Test
    void testTrouverParVente() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO PRODUIT (id_produit, nom, prix) VALUES (1, 'Produit Test', 10.5);");
            stmt.execute("INSERT INTO LIGNEVENTE (id_produit, id_vente, quantite) VALUES (1, 2, 3);");
        }

        List<LigneVente> lignesVente = ligneVenteDAO.trouverParVente(2);

        assertNotNull(lignesVente);
        assertEquals(1, lignesVente.size());
        assertEquals("Produit Test", lignesVente.get(0).getProduit().getNom());
        assertEquals(3, lignesVente.get(0).getQuantite());
    }

    @Test
    void testSupprimerLigneVente() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO PRODUIT (id_produit, nom, prix) VALUES (1, 'Produit Test', 10.5);");
            stmt.execute("INSERT INTO LIGNEVENTE (id_ligne, id_produit, id_vente, quantite) VALUES (1, 1, 2, 3);");
        }

        boolean result = ligneVenteDAO.supprimer(1);

        assertTrue(result);

        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM LIGNEVENTE WHERE id_ligne = 1")) {
            rs.next();
            assertEquals(0, rs.getInt(1)); // Ensure the row is deleted
        }
    }

    @Test
    void testSupprimerParVente() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO PRODUIT (id_produit, nom, prix) VALUES (1, 'Produit Test', 10.5);");
            stmt.execute("INSERT INTO LIGNEVENTE (id_produit, id_vente, quantite) VALUES (1, 2, 3);");
            stmt.execute("INSERT INTO LIGNEVENTE (id_produit, id_vente, quantite) VALUES (1, 2, 4);");
        }

        boolean result = ligneVenteDAO.supprimerParVente(2);

        assertTrue(result);

        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM LIGNEVENTE WHERE id_vente = 2")) {
            rs.next();
            assertEquals(0, rs.getInt(1)); 
        }
    }
}
